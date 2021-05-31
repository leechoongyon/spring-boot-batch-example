package com.example.batch.job;

import com.example.batch.bean.BulkInsertTestBean;
import com.example.batch.domain.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by LeeJunGyun.
 * Date: 2020-07-06
 * Time: 16:26
 * Desc:
 */

@Configuration
public class BulkInsertTestJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private final int CHUNK_SIZE = 100;

    private BulkInsertTestBean bulkInsertTestBean;

    public BulkInsertTestJob(BulkInsertTestBean bulkInsertTestBean) {
        this.bulkInsertTestBean = bulkInsertTestBean;
    }

    @Bean
    public Job bulkInsertTestJob01() {
        return jobBuilderFactory.get("bulkInsertTestJob01")
                .start(bulkInsertTestStep01())
                .build()
                ;
    }

    @Bean
    public Step bulkInsertTestStep01() {
        return stepBuilderFactory.get("bulkInsertTestStep01")
                .<Student, Student>chunk(CHUNK_SIZE)
                .reader(bulkInsertTestBean)
                .writer(bulkInsertTestBean)
                .build();
    }
}
