package com.example.batch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlatFileSampleDTO {
    String sample01;
    String sample02;

    public FlatFileSampleDTO(String sample01, String sample02) {
        this.sample01 = sample01;
        this.sample02 = sample02;
    }
}
