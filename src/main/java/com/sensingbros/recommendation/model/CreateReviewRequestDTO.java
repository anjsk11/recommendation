package com.sensingbros.recommendation.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateReviewRequestDTO {
    private Integer placeId;
    private BigDecimal score;
    private String body;
}
