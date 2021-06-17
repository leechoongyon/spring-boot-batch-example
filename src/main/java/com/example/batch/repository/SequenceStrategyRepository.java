package com.example.batch.repository;


import com.example.batch.domain.SequenceStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceStrategyRepository extends JpaRepository<SequenceStrategy, Long> {
}
