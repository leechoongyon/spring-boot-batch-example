package com.example.batch.job;

import com.example.batch.domain.flatFile.FlatFileExampleBean;
import com.example.batch.domain.flatFile.FlatFileExampleInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FlatFileExampleJob {

    private static final int CHUNK_SIZE = 100;
    private static final int FETCH_SIZE = 100;
    private static final String ENCODING = "UTF-8";
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private DataSource dataSource;
    private FlatFileExampleBean flatFileExampleBean;

    public FlatFileExampleJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                              DataSource dataSource, FlatFileExampleBean flatFileExampleBean) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.flatFileExampleBean = flatFileExampleBean;
    }

    @Bean
    public Job flatFileExampleJob01() {
        return jobBuilderFactory.get("flatFileExampleJob01")
                .start(flatFileExampleStep01())
                .build()
                ;
    }

    @Bean
    public Step flatFileExampleStep01() {
        return stepBuilderFactory.get("flatFileExampleStep01")
                .<FlatFileExampleInfo.Base, FlatFileExampleInfo.Base>chunk(CHUNK_SIZE)
                .reader(flatFileExampleReader())
                .processor(flatFileExampleBean)
                .writer(flatFileExampleWriter(""))
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<FlatFileExampleInfo.Base> flatFileExampleReader() {
        // Sort Keys
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        return new JdbcPagingItemReaderBuilder()
                .name("flatFileExampleReader")
                .dataSource(this.dataSource)
                .fetchSize(FETCH_SIZE)
                .rowMapper(new FlatFileExampleInfoMapper())
                .queryProvider(getMySqlPagingQueryProvider(sortKeys))
                .saveState(true)    // ExecutionContext 에 Reader 의 상태를 저장할지를 결정하는 옵션. 기본 값 true. multi thread 돌릴 때는 이 옵션 꺼야 함.
                .build();
    }

    @Bean
    @StepScope  // step 기간동안 빈 lifecycle 관리.
    public FlatFileItemWriter<FlatFileExampleInfo.Base> flatFileExampleWriter(@Value("#{jobParameters[path]}") String path) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path is null");
        }

        return new FlatFileItemWriterBuilder()
                .name("flatFileExampleWriter")
                .encoding(ENCODING)
                .headerCallback(getHeaderCallBack())
                .footerCallback(getFooterCallback())
                .resource(new FileSystemResource(path))
                .formatted()    // 자바의 formatter 를 적용합니다.
                .format(FlatFileExampleInfo.Base.getFormat())   // 사용되는 format template 입니다.
                .names(FlatFileExampleInfo.Base.getNames())
                .build();
    }

    private FlatFileFooterCallback getFooterCallback() {
        return new FlatFileFooterCallback() {
            @Override
            public void writeFooter(Writer writer) throws IOException {
                writer.write("================END_LINE================");
            }
        };
    }

    private FlatFileHeaderCallback getHeaderCallBack() {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write("================FIRST_LINE================");
            }
        };
    }

    private class FlatFileExampleInfoMapper implements RowMapper<FlatFileExampleInfo.Base> {
        @Override
        public FlatFileExampleInfo.Base mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FlatFileExampleInfo.Base.builder()
                    .id(rs.getString("id"))
                    .name(rs.getString("name"))
                    .age(rs.getInt("age"))
                    .build();
        }
    }

    private MySqlPagingQueryProvider getMySqlPagingQueryProvider(Map<String, Order> sortKeys) {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, age");
        queryProvider.setFromClause("from TEST");
        queryProvider.setSortKeys(sortKeys);
        return queryProvider;
    }
}
