package com.example.batch.bean;

import com.example.batch.domain.Student;
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
public class BulkInsertTestBean implements ItemReader<Student>, ItemWriter<Student> {

    private final StudentRepository studentRepository;

    // Thread not safe
    private int readCount = 100;

    private int currentCount = 0;

    @Override
    public Student read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currentCount == readCount) {
            return null;
        } else {
            currentCount++;
//            return new Student(Long.valueOf(currentCount),"test");
            return new Student("test");
        }
    }

    @Override
    public void write(List<? extends Student> items) throws Exception {
        studentRepository.saveAll(items);
    }
}
