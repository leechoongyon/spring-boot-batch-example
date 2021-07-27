package com.example.batch.repository;

import com.example.batch.domain.NoGenerativeStrategy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class NoGenerativeStrategyRepositoryTest {

    @Autowired
    private NoGenerativeStrategyRepository noGenerativeStrategyRepository;

    /**
     * select 한 번 한 후 조회 함.
     * @throws Exception
     */
    @Test
    public void ID생성전략이_없으며_애플리케이션에서생성한경우_SAVE_테스트() throws Exception {
        // given
        NoGenerativeStrategy noGenerativeStrategy =
                NoGenerativeStrategy.builder()
                        .id(2L)
                        .name("test")
                        .build();

        // when
        noGenerativeStrategyRepository.save(noGenerativeStrategy);
        Optional<NoGenerativeStrategy> result = noGenerativeStrategyRepository.findById(2L);

        // then
        Assert.assertThat(result.get().getName(), is("test"));
    }

    /**
     * 생성전략이 없으니 select 한 후, insert 함. 즉, 오래 걸림.
     * @throws Exception
     */
    @Test
    public void ID생성전략이_없으며_대량SAVE_테스트() throws Exception {
        List<NoGenerativeStrategy> list = new ArrayList<>();
        for (int i = 0 ; i < 1000 ; i++) {
            NoGenerativeStrategy noGenerativeStrategy =
                    NoGenerativeStrategy.builder()
                            .id(Long.valueOf(i + 1))
                            .name("test").build();
            list.add(noGenerativeStrategy);
        }
        noGenerativeStrategyRepository.saveAll(list);
    }
}