package com.example.batch.job;

import com.example.batch.bean.BulkInsertTestBean;
import com.example.batch.domain.Student;
import com.example.batch.dto.FlatFileSampleDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Created by LeeJunGyun.
 * Date: 2020-07-06
 * Time: 16:26
 * Desc:
 */

@Configuration
public class FlatFileSampleJob {

    private static final String INPUT_FILE_PATH = "./file/InputFlatFileSample.txt";
    private static final String OUTPUT_FILE_PATH = "./file/OutputFlatFileSample.txt";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private final int CHUNK_SIZE = 100;

    @Bean
    public Job flatFileSampleJob01() {
        return jobBuilderFactory.get("flatFileSampleJob01")
                .start(flatFileSampleStep01())
                .build()
                ;
    }

    @Bean
    public Step flatFileSampleStep01() {
        return stepBuilderFactory.get("flatFileSampleStep01")
                .<FlatFileSampleDTO, FlatFileSampleDTO>chunk(CHUNK_SIZE)
                .reader(flatFileSampleItemReader())
                .writer(flatFileSampleItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<FlatFileSampleDTO> flatFileSampleItemReader() {
        return new FlatFileItemReaderBuilder<FlatFileSampleDTO>()
                .name("flatFileSampleItemReader")
                .resource(new FileSystemResource(INPUT_FILE_PATH))
                .delimited().delimiter(",")
                .names(new String[] {"sample01", "sample02"})
                .targetType(FlatFileSampleDTO.class)
                .build()
                ;
    }

    @Bean
    public FlatFileItemWriter<FlatFileSampleDTO> flatFileSampleItemWriter() {
        return new FlatFileItemWriterBuilder<FlatFileSampleDTO>()
                .name("flatFileSampleItemWriter")
                .resource(new FileSystemResource(OUTPUT_FILE_PATH))
                .delimited().delimiter("|")
                .names("sample01", "sample02")
                .build();
    }
}
