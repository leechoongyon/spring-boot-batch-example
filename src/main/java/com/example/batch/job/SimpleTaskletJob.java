package com.example.batch.job;

import com.example.batch.bean.SimpleTaskletBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SimpleTaskletJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private SimpleTaskletBean simpleTaskletBean;

    public SimpleTaskletJob(SimpleTaskletBean simpleTaskletBean) {
        this.simpleTaskletBean = simpleTaskletBean;
    }

    @Bean
    public Job simpleTaskletJob01() {
        return jobBuilderFactory.get("simpleTaskletJob01")
                .incrementer(new RunIdIncrementer())
                .flow(simpleTaskletStep01())
                .end()
                .build();
    }

    @Bean
    public Step simpleTaskletStep01() {
        return stepBuilderFactory.get("simpleTaskletStep01")
                .tasklet(simpleTaskletBean)
                .build();
    }
}