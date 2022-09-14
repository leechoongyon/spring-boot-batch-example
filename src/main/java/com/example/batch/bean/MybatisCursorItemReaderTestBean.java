package com.example.batch.bean;

import com.example.batch.dto.TestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class MybatisCursorItemReaderTestBean implements ItemProcessor<TestDto, TestDto>, ItemWriter<TestDto> {

    @Override
    public TestDto process(TestDto item) throws Exception {
        return item;
    }

    @Override
    public void write(List<? extends TestDto> items) throws Exception {
        items.forEach(item -> {
            log.info("item : {}", item);
        });
    }
}
