package com.example.batch.job;

import com.example.batch.bean.PartitionerExampleBean;
import com.example.batch.bean.SimplePartitioner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class PartitionerExampleJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private PartitionerExampleBean partitionerExampleBean;

    @Bean
    public SimplePartitioner simplePartitioner() {
        return new SimplePartitioner();
    }

    @Bean
    public Job partitionerExampleJob01() {
        return jobBuilderFactory.get("partitionerExampleJob01")
                .start(partitionerExampleMasterStep())
                .build();
    }

    @Bean
    public Step partitionerExampleMasterStep() {
        return stepBuilderFactory.get("partitionerExampleMasterStep")
                .partitioner(partitionerExampleSlaveStep().getName(), simplePartitioner())
                .step(partitionerExampleSlaveStep())
                .gridSize(3)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step partitionerExampleSlaveStep() {
        return stepBuilderFactory.get("partitionerExampleSlaveStep")
                .<String, String>chunk(100)
                .reader(partitionerExampleBean)
                .writer(partitionerExampleBean)
                .build();
    }

}
