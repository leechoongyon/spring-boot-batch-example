package com.example.batch.domain.classifierItemWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class ClassifierItemWriterExampleBean implements ItemProcessor<ClassifierItemWriterExampleInfo.Base, ClassifierItemWriterExampleInfo.MultiDto> {
    @Override
    public ClassifierItemWriterExampleInfo.MultiDto process(ClassifierItemWriterExampleInfo.Base item)
            throws Exception {
        return null;
    }
}
