package com.sensingbros.recommendation.model;

import java.math.BigDecimal;

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
