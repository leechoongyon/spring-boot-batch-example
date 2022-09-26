package com.example.batch.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TestDto {
    private String test;
    private String content;

    @Builder
    public TestDto(String test, String content) {
        this.test = test;
        this.content = content;
    }
}
