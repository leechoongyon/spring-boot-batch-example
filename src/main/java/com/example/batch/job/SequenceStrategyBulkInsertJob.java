package com.example.batch.job;

import com.example.batch.bean.SequenceStrategyBulkInsertBean;
import com.example.batch.domain.SequenceStrategy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SequenceStrategyBulkInsertJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private final int CHUNK_SIZE = 10;

    private SequenceStrategyBulkInsertBean sequenceStrategyBulkInsertBean;

    public SequenceStrategyBulkInsertJob(SequenceStrategyBulkInsertBean sequenceStrategyBulkInsertBean) {
        this.sequenceStrategyBulkInsertBean = sequenceStrategyBulkInsertBean;
    }

    @Bean
    public Job sequenceStrategyBulkInsertJob01() {
        return jobBuilderFactory.get("sequenceStrategyBulkInsertJob01")
                .start(sequenceStrategyBulkInsertStep01())
                .build()
                ;
    }

    @Bean
    public Step sequenceStrategyBulkInsertStep01() {
        return stepBuilderFactory.get("sequenceStrategyBulkInsertStep01")
                .<SequenceStrategy, SequenceStrategy>chunk(CHUNK_SIZE)
                .reader(sequenceStrategyBulkInsertBean)
                .writer(sequenceStrategyBulkInsertBean)
                .build();
    }
}
