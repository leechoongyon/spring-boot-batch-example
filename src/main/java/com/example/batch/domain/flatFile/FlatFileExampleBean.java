package com.example.batch.domain.flatFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class FlatFileExampleBean implements ItemProcessor<FlatFileExampleInfo.Base, FlatFileExampleInfo.Base> {
    @Override
    public FlatFileExampleInfo.Base process(FlatFileExampleInfo.Base item) throws Exception {
        log.info("item : {}", item);
        return item;
    }
}
