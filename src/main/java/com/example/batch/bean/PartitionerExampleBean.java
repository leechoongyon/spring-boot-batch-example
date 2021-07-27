package com.example.batch.bean;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@StepScope
public class PartitionerExampleBean implements ItemStream, ItemReader<String>, ItemWriter<String> {

    private int start;
    private int end;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        start = executionContext.getInt("start");
        end = executionContext.getInt("end");

//        if (start == 34) {
//            log.error("partitioner thread exception test");
//            throw new RuntimeException("partitioner thread exception test");
//        }

        log.info("start : {} , end : {}", start, end);

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }

    @Override
    public void write(List<? extends String> items) throws Exception {

    }
}
