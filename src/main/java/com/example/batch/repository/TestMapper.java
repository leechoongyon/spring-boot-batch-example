package com.example.batch.repository;

import com.example.batch.dto.TestDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TestMapper {
    List<TestDto> findTest(Map<String, Object> map);
}
