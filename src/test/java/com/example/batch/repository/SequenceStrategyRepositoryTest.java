package com.example.batch.repository;

import com.example.batch.domain.SequenceStrategy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * RunWith 는 테스트를 실행시켜주는 것이고. SpringRunner 는 빈들을 올린다. SpringBootTest 와는 다르게 다 올리지는 않음.
 * DataJpaTest 는 인메모리 데이터베이스로 설정하고 엔티티 스캔.
 * 2개 Annotation 은 같이 써야 함. 스프링 공식문서에 나와있음.
 * reference : https://4whomtbts.tistory.com/128
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class SequenceStrategyRepositoryTest {

    @Autowired
    private SequenceStrategyRepository sequenceStrategyRepository;

    @Test
    public void SEQUENCE_방식_성능테스트() throws Exception {
        long start = System.currentTimeMillis();
        List<SequenceStrategy> list = new ArrayList<>();
        // 테스트 통과가 오래 걸려 횟수 줄임
//        for (int i = 0 ; i < 10000 ; i++) {
        for (int i = 0 ; i < 100 ; i++) {
            SequenceStrategy sequenceStrategy =
                    SequenceStrategy.builder().name("test").build();
            list.add(sequenceStrategy);
        }
        List<SequenceStrategy> result = sequenceStrategyRepository.saveAll(list);
        Assert.assertThat(result.size(), is(100));
        System.out.println("elapsed time : " + (System.currentTimeMillis() - start) );
    }
}