package com.example.batch.reader;

import com.example.batch.domain.Member;
import com.example.batch.domain.QMember;
import com.example.batch.reader.expression.Expression;
import com.example.batch.reader.option.QuerydslNoOffsetNumberOptions;
import com.example.batch.repository.MemberRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuerydslNoOffsetPagingItemReaderAsyncTest {

    private static final int TOTAL_COUNT = 10;
    private static Log logger = LogFactory.getLog(QuerydslNoOffsetPagingItemReaderAsyncTest.class);

    private static final int THREAD_COUNT = 3;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MemberRepository memberRepository;

    private final int pageSize = 1;

    @Before
    public void setUp() {
        Member member = Member.builder()
                .name("test1")
                .build();
        Member member2 = Member.builder()
                .name("test2")
                .build();
        Member member3 = Member.builder()
                .name("test3")
                .build();
        Member member4 = Member.builder()
                .name("test4")
                .build();
        Member member5 = Member.builder()
                .name("test5")
                .build();
        Member member6 = Member.builder()
                .name("test6")
                .build();
        Member member7 = Member.builder()
                .name("test7")
                .build();
        Member member8 = Member.builder()
                .name("test8")
                .build();
        Member member9 = Member.builder()
                .name("test9")
                .build();
        Member member10 = Member.builder()
                .name("test10")
                .build();

        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);
        memberRepository.save(member7);
        memberRepository.save(member8);
        memberRepository.save(member9);
        memberRepository.save(member10);
    }

    @Test
    public void querydslNoOffsetPagingItemReaderAsyncTest() throws Exception {
        List<Throwable> throwables = new ArrayList<>();
        int max = 10;
        for (int i = 0; i < max; i++) {
            try {
                asyncTest();
            }
            catch (Throwable e) {
                throwables.add(e);
            }
        }
        if (!throwables.isEmpty()) {
            throw new IllegalStateException(String.format("Failed %d out of %d", throwables.size(), max), throwables
                    .get(0));
        }
    }

    private void asyncTest() throws InterruptedException, ExecutionException {
        QuerydslNoOffsetNumberOptions<Member, Long> options =
                new QuerydslNoOffsetNumberOptions<>(QMember.member.id, Expression.ASC);

        int chunkSize = 1;

        QuerydslNoOffsetPagingItemReader<Member> reader =
                new QuerydslNoOffsetPagingItemReader<>(entityManagerFactory, chunkSize, options,
                        queryFactory -> queryFactory
                                .selectFrom(QMember.member)
                );

        reader.open(new ExecutionContext());

        CompletionService<List<Member>> completionService =
                new ExecutorCompletionService<>(Executors.newFixedThreadPool(THREAD_COUNT));
        for (int i = 0 ; i < THREAD_COUNT; i++) {
            completionService.submit(new Callable<List<Member>>() {
                @Override
                public List<Member> call() throws Exception {
                    List<Member> list = new ArrayList<>();
                    Member nextMember = null;
                    do {
                        nextMember = reader.read();
                        Thread.sleep(10L);
                        logger.debug("item: " + nextMember);
                        if (nextMember != null) {
                            list.add(nextMember);
                        }
                    } while (nextMember != null);
                    return list;
                }
            });
        }
        int count = 0;
        Set<Member> results = new HashSet<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            List<Member> items = completionService.take().get();
            count += items.size();
            logger.debug("total items count: " + items.size());
            logger.debug("items: " + items);
            assertNotNull(items);
            results.addAll(items);
        }
        assertEquals(TOTAL_COUNT, count);
        assertEquals(TOTAL_COUNT, results.size());

        reader.close();
    }
}