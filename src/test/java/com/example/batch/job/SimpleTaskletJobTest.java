package com.example.batch.job;

import com.example.batch.TestConfig;
import com.example.batch.bean.SimpleTaskletBean;
import com.example.batch.domain.Member;
import com.example.batch.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;

/**
 * 통합 테스트 환경 구성 (Tasklet)
 */

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes={SimpleTaskletJob.class, SimpleTaskletBean.class, TestConfig.class})
public class SimpleTaskletJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void simpleTaskletJobTest() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("test", "testData123")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertThat(jobExecution.getStatus(),  is(BatchStatus.COMPLETED));
        Assert.assertThat(jobExecution.getExitStatus(),  is(ExitStatus.COMPLETED));
    }

    @Test
    public void 배치통합테스트환경에서_repositryTest() throws Exception {
        Member member = Member.builder()
                .name("test")
                .build();
        memberRepository.save(member);
    }
}