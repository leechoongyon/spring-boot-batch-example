package com.example.batch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @Column(name="member_id")
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Builder
    public Member(String name) {
        this.name = name;
    }
}
