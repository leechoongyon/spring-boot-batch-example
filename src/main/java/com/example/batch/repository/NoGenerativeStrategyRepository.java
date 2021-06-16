package com.example.batch.repository;

import com.example.batch.domain.NoGenerativeStrategy;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoGenerativeStrategyRepository extends JpaRepository<NoGenerativeStrategy, Long> {
}
