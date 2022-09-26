package com.example.batch.job;

import com.example.batch.TestConfig;
import com.example.batch.config.utils.FileUtils;
import com.example.batch.domain.flatFile.FlatFileExampleBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(classes={FlatFileExampleJob.class, FlatFileExampleBean.class, TestConfig.class})
public class FlatFileExampleJobTest {

    private final String PATH = "src/test/resources/output/output.dat";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @SqlGroup(
            {
                    @Sql(scripts = "/sql/FLAT_FILE_EXAMPLE_BEFORE_TEST_SQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
                    @Sql(scripts = "/sql/FLAT_FILE_EXAMPLE_AFTER_TEST_SQL.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
            }
    )
    @Test
    public void DB_읽어서_FLAT_FILE_쓰기_테스트() throws Exception {
        // when
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("dummy", "testData123123")
                .addString("path", PATH)
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);


        // then
        Assertions.assertEquals(jobExecution.getStatus(),  BatchStatus.COMPLETED);
        Assertions.assertEquals(jobExecution.getExitStatus(),  ExitStatus.COMPLETED);

        int fileLineCount = FileUtils.getFileLineCount(new File(PATH));
        Assertions.assertEquals(4,  fileLineCount);
    }
}