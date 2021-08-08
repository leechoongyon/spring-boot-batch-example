package com.example.batch.reader;

import com.example.batch.domain.Member;
import com.example.batch.domain.QMember;
import com.example.batch.reader.QuerydslNoOffsetPagingItemReader;
import com.example.batch.reader.expression.Expression;
import com.example.batch.reader.option.QuerydslNoOffsetNumberOptions;
import com.example.batch.reader.option.QuerydslNoOffsetStringOptions;
import com.example.batch.repository.MemberRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuerydslNoOffsetPagingItemReaderTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MemberRepository memberRepository;

    private final int pageSize = 1;

    @After
    public void after() throws Exception {
        memberRepository.deleteAllInBatch();
    }

    @Test
    public void NUMBER_ID_테스트() throws Exception {
        Member member = Member.builder()
                .name("test1")
                .build();
        Member member2 = Member.builder()
                .name("test2")
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        QuerydslNoOffsetNumberOptions<Member, Long> options =
                new QuerydslNoOffsetNumberOptions<>(QMember.member.id, Expression.ASC);

        // 데이터 2개니 1개씩만 읽기
        int chunkSize = 1;

        QuerydslNoOffsetPagingItemReader<Member> reader =
                new QuerydslNoOffsetPagingItemReader<>(entityManagerFactory, chunkSize, options,
                        queryFactory -> queryFactory
                                .selectFrom(QMember.member)
                );

        reader.open(new ExecutionContext());


        Assert.assertThat(reader.read().getName(), is("test1"));
        Assert.assertThat(reader.read().getName(), is("test2"));
        Assert.assertNull(reader.read());
    }

    @Test
    public void String_테스트() throws Exception {
        Member member = Member.builder()
                .name("test1")
                .build();
        Member member2 = Member.builder()
                .name("test2")
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        QuerydslNoOffsetStringOptions<Member> options =
                new QuerydslNoOffsetStringOptions<>(QMember.member.name, Expression.ASC);

        // 데이터 2개니 1개씩만 읽기
        int chunkSize = 1;

        QuerydslNoOffsetPagingItemReader<Member> reader =
                new QuerydslNoOffsetPagingItemReader<>(entityManagerFactory, chunkSize, options,
                        queryFactory -> queryFactory
                                .selectFrom(QMember.member)
                );

        reader.open(new ExecutionContext());


        Assert.assertThat(reader.read().getName(), is("test1"));
        Assert.assertThat(reader.read().getName(), is("test2"));
        Assert.assertNull(reader.read());
    }

    @Test
    public void GROUP_BY_ASC_테스트() throws Exception {
        Member member = Member.builder()
                .name("test1")
                .build();
        Member member2 = Member.builder()
                .name("test2")
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        QuerydslNoOffsetStringOptions<Member> options =
                new QuerydslNoOffsetStringOptions<>(QMember.member.name, Expression.ASC);

        // 데이터 2개니 1개씩만 읽기
        int chunkSize = 1;

        QuerydslNoOffsetPagingItemReader<Member> reader =
                new QuerydslNoOffsetPagingItemReader<>(entityManagerFactory, chunkSize, options,
                        queryFactory -> queryFactory
                                .selectFrom(QMember.member)
                                .groupBy(QMember.member.id)
                );

        reader.open(new ExecutionContext());

        Assert.assertThat(reader.read().getName(), is("test1"));
        Assert.assertThat(reader.read().getName(), is("test2"));
        Assert.assertNull(reader.read());
    }

    /**
     * ORDER BY 를 적용하면, querydsl 에 적용한게 먼저 적용되고 그 뒤에 Options 에 적용한 필드가 order by 적용됨.
     * 그렇기에 NoOffSetPagingItemReader 가 실제로 적용되는지 안되는지는 test 해봐야할듯
     * @throws Exception
     */
    @Test
    public void GROUP_BY_PLUS_ORDER_BY_ASC_테스트() throws Exception {
        Member member = Member.builder()
                .name("test1")
                .build();
        Member member2 = Member.builder()
                .name("test2")
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        QuerydslNoOffsetStringOptions<Member> options =
                new QuerydslNoOffsetStringOptions<>(QMember.member.name, Expression.ASC);

        // 데이터 2개니 1개씩만 읽기
        int chunkSize = 1;

        QuerydslNoOffsetPagingItemReader<Member> reader =
                new QuerydslNoOffsetPagingItemReader<>(entityManagerFactory, chunkSize, options,
                        queryFactory -> queryFactory
                                .selectFrom(QMember.member)
                                .groupBy(QMember.member.id)
                                .orderBy(QMember.member.id.asc())
                );

        reader.open(new ExecutionContext());

        Assert.assertNotNull(reader.read());
    }
}