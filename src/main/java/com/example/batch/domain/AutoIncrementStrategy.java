package com.example.batch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class AutoIncrementStrategy {
    @Id

    private Long id;

    @Column
    private String name;

    @Builder
    public AutoIncrementStrategy(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
