package com.sensingbros.recommendation.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendResponse {
    private boolean success_code;
    private Map<String, Double> data; // "10": 4.51234 형태의 추천 데이터
}
