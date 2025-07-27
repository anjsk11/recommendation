package com.sensingbros.recommendation.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ReviewResponseDTO {
    private Integer id;
    private String name;
    private BigDecimal score;
    private String body;

    public ReviewResponseDTO(Integer id, String name, BigDecimal score, String body) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.body = body;
    }
}
