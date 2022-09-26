package com.example.batch.job;

import com.example.batch.domain.classifierItemWriter.ClassifierItemWriterExampleBean;
import com.example.batch.domain.classifierItemWriter.ClassifierItemWriterExampleInfo;
import com.example.batch.domain.classifierItemWriter.ClassifierItemWriterExampleInfo.Data;
import com.example.batch.domain.classifierItemWriter.ClassifierItemWriterExampleInfo.Header;
import com.example.batch.domain.classifierItemWriter.ClassifierItemWriterExampleInfo.MultiDto;
import com.example.batch.domain.classifierItemWriter.ClassifierItemWriterExampleInfo.Tailer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ClassifierItemWriterExampleJob {
    private static final int CHUNK_SIZE = 100;
    private static final int FETCH_SIZE = 100;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ClassifierItemWriterExampleBean classifierItemWriterExampleBean;
    private final DataSource dataSource;

    @Bean
    public Job classifierItemWriterExampleJob01() {
        return jobBuilderFactory.get("classifierItemWriterExampleJob01")
                .start(classifierItemWriterExampleStep01())
                .build()
                ;
    }

    @Bean
    public Step classifierItemWriterExampleStep01() {
        return stepBuilderFactory.get("classifierItemWriterExampleStep01")
                .<ClassifierItemWriterExampleInfo.Base, MultiDto>chunk(CHUNK_SIZE)
                .reader(classifierItemWriterExamplePagingItemReader())
                .processor(classifierItemWriterExampleBean)
                .writer(classifierCompositeItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<ClassifierItemWriterExampleInfo.Base> classifierItemWriterExamplePagingItemReader() {
        // Sort Keys
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        return new JdbcPagingItemReaderBuilder()
                .dataSource(this.dataSource)
                .fetchSize(FETCH_SIZE)
                .rowMapper(new ClassifierItemWriterExampleInfoMapper())
                .queryProvider(getMySqlPagingQueryProvider(sortKeys))
                .build();
    }

    @Bean
    public ClassifierCompositeItemWriter<MultiDto> classifierCompositeItemWriter() {
        return new ClassifierCompositeItemWriterBuilder()
                .classifier(new ClassifierItemWriterExampleClassifier(headerItemWriter(),
                        dataItemWriter(), tailerItemWriter()))
                .build();
    }

    @Bean
    public FlatFileItemWriter headerItemWriter() {
        return  new FlatFileItemWriterBuilder<Header>()
                .name("headerItemWriter")
                .resource(new FileSystemResource("output/output.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

    @Bean
    public FlatFileItemWriter dataItemWriter() {
        return  new FlatFileItemWriterBuilder<Data>()
                .name("dataItemWriter")
                .resource(new FileSystemResource("output/output.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

    @Bean
    public FlatFileItemWriter tailerItemWriter() {
        return  new FlatFileItemWriterBuilder<Tailer>()
                .name("tailerItemWriter")
                .resource(new FileSystemResource("output/output.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

    private MySqlPagingQueryProvider getMySqlPagingQueryProvider(Map<String, Order> sortKeys) {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, age");
        queryProvider.setFromClause("from TEST");
        queryProvider.setSortKeys(sortKeys);
        return queryProvider;
    }

    private class ClassifierItemWriterExampleInfoMapper implements RowMapper<ClassifierItemWriterExampleInfo.Base> {
        @Override
        public ClassifierItemWriterExampleInfo.Base mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ClassifierItemWriterExampleInfo.Base.builder()
                    .id(rs.getString("id"))
                    .name(rs.getString("name"))
                    .age(rs.getInt("age"))
                    .build();
        }
    }

    private static class ClassifierItemWriterExampleClassifier implements Classifier<MultiDto, ItemWriter<? super MultiDto>> {
        private ItemWriter<Header> headerItemWriter;
        private ItemWriter<Data> dataItemWriter;
        private ItemWriter<Tailer> tailerItemWriter;

        public ClassifierItemWriterExampleClassifier(ItemWriter<Header> headerItemWriter,
                                                     ItemWriter<Data> dataItemWriter,
                                                     ItemWriter<Tailer> tailerItemWriter) {
            this.headerItemWriter = headerItemWriter;
            this.dataItemWriter = dataItemWriter;
            this.tailerItemWriter = tailerItemWriter;
        }

        @Override
        public ItemWriter<? super MultiDto> classify(MultiDto multiDto) {
//            if (ObjectUtils.isEmpty(multiDto.getHeader())) {
//                return headerItemWriter;
//            }
            return null;
        }
    }

}
