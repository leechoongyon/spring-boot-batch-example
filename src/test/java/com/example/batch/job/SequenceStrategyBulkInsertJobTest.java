package com.example.batch.job;

import com.example.batch.TestConfig;
import com.example.batch.bean.SequenceStrategyBulkInsertBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes={SequenceStrategyBulkInsertJob.class, SequenceStrategyBulkInsertBean.class, TestConfig.class})
public class SequenceStrategyBulkInsertJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    /**
     * 채번을 먼저 배치 처리해서 불러오고, 그 뒤에 insert 를 배치 처리.
     * @throws Exception
     */
    @Test
    public void SEQUENCE_생성전략_BULK_INSERT_테스트() throws Exception {
        long start = System.currentTimeMillis();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        Assert.assertThat(jobExecution.getStatus(),  is(BatchStatus.COMPLETED));
        Assert.assertThat(jobExecution.getExitStatus(),  is(ExitStatus.COMPLETED));
        System.out.println("elapsed time : " + (System.currentTimeMillis() - start) );
    }
}