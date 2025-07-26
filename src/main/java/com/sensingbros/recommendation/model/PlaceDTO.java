package com.sensingbros.recommendation.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceDTO {
    private Integer id;
    private String name;
    private Integer scoreCount;
    private BigDecimal avgScore;
    private String category;
    private String url;
    private String address;
    private Double x;
    private Double y;
}