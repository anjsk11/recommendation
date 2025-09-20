package com.sensingbros.recommendation.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.sensingbros.recommendation.model.ResponseDTO;
import com.sensingbros.recommendation.service.FlaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;
import java.util.UUID;

@RestController
public class FlaskController {

    private final FlaskService  flaskService;

    public FlaskController(FlaskService flaskService) {
        this.flaskService = flaskService;
    }

    @PostMapping("/me/recommend")
    public ResponseEntity<?> recommend(@AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));

            // FlaskService 호출: 추천 결과를 Map<String, Double> 형태로 받음
            Map<String, Double> recommendations = flaskService.getRecommendationFromFlask(userId);

            // 성공 응답 생성
            ResponseDTO<Map<String, Double>> responseDto =
                    new ResponseDTO<>(true, recommendations);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            // 실패 응답 생성
            ResponseDTO<String> errorDto = new ResponseDTO<>(false, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorDto);
        }
    }

}