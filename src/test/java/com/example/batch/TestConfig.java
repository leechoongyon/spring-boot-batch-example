package com.example.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@ComponentScan(basePackages = "com.example.batch.config")
@MapperScan(value = {"com.example.batch.infrastructure.*"}) // MyBatis 쓸 때 Scan
@TestConfiguration
public class TestConfig {
}
