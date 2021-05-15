package com.example.batch.reader;

import com.example.batch.domain.Member;
import com.example.batch.domain.QMember;
import com.example.batch.repository.MemberRepository;
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
public class QuerydslPagingItemReaderTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MemberRepository memberRepository;

    private final int pageSize = 1;


    @Test
    public void querydslPagingItemReaderTest() throws Exception {
        Member member = Member.builder()
                .name("test1")
                .build();
        Member member2 = Member.builder()
                .name("test2")
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        QuerydslPagingItemReader<Member> reader = new QuerydslPagingItemReader<>(entityManagerFactory, pageSize, queryFactory -> queryFactory
                .selectFrom(QMember.member))
                ;

        reader.open(new ExecutionContext());

        Assert.assertThat(reader.read().getName(), is("test1"));
        Assert.assertThat(reader.read().getName(), is("test2"));
    }
}