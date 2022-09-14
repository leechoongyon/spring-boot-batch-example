package com.example.batch.job;

import com.example.batch.TestConfig;
import com.example.batch.bean.MybatisCursorItemReaderTestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(classes={MybatisCursorItemReaderTestJob.class, MybatisCursorItemReaderTestBean.class, TestConfig.class})
public class MybatisCursorItemReaderJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Sql(scripts = "/sql/TEST_DDL.sql")
    @Test
    public void MybatisCursorItemReader_테스트() throws Exception {


        // when
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("str", "testData123")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);


        // then
        Assertions.assertEquals(jobExecution.getStatus(),  BatchStatus.COMPLETED);
        Assertions.assertEquals(jobExecution.getExitStatus(),  ExitStatus.COMPLETED);
    }
}