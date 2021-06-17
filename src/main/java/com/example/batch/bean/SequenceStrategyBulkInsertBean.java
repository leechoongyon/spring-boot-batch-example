package com.example.batch.bean;

import com.example.batch.domain.SequenceStrategy;
import com.example.batch.domain.Student;
import com.example.batch.repository.SequenceStrategyRepository;
import com.example.batch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class SequenceStrategyBulkInsertBean implements ItemReader<SequenceStrategy>, ItemWriter<SequenceStrategy> {

    private final SequenceStrategyRepository sequenceStrategyRepository;

    // Thread not safe
    private int readCount = 100;

    private int currentCount = 0;

    @Override
    public SequenceStrategy read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currentCount == readCount) {
            return null;
        } else {
            currentCount++;
            return new SequenceStrategy("test");
        }
    }

    @Override
    public void write(List<? extends SequenceStrategy> items) throws Exception {
        sequenceStrategyRepository.saveAll(items);
    }
}
