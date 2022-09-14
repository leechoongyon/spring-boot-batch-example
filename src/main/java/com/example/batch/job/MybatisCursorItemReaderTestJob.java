package com.example.batch.job;

import com.example.batch.bean.MybatisCursorItemReaderTestBean;
import com.example.batch.dto.TestDto;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class MybatisCursorItemReaderTestJob {
    private static final int CHUNK_SIZE = 100;
    private static final String TEST_QUERY_ID = "com.example.batch.repository.TestMapper.findTest";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;
    private final MybatisCursorItemReaderTestBean mybatisCursorItemReaderTestBean;

    @Bean
    public Job mybatisCursorItemReaderTestJob01() {
        return jobBuilderFactory.get("mybatisCursorItemReaderTestJob01")
                .start(mybatisCursorItemReaderTestStep01())
                .build()
                ;
    }

    @Bean
    public Step mybatisCursorItemReaderTestStep01() {
        return stepBuilderFactory.get("mybatisCursorItemReaderTestStep01")
                .<TestDto, TestDto>chunk(CHUNK_SIZE)
                .reader(testReader(""))
                .processor(mybatisCursorItemReaderTestBean)
                .writer(mybatisCursorItemReaderTestBean)
                .build();
    }

    // bean 으로 등록해야 open --> read --> close 의 사이클이 수행
    @Bean
    @StepScope
    public MyBatisCursorItemReader<TestDto> testReader(@Value("#{jobParameters[str]}") String str) {
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("str", StringUtils.isEmpty(str) ? "default" : str);
        }};
        return new MyBatisCursorItemReaderBuilder<TestDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId(TEST_QUERY_ID)
                .parameterValues(map)
                .build()
                ;
    }


}
