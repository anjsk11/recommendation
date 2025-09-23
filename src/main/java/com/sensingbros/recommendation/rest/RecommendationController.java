package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.domain.Place;
import com.sensingbros.recommendation.model.ResponseDTO;
import com.sensingbros.recommendation.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/me/recommend")
    public ResponseEntity<ResponseDTO<?>> getRecommendations(@AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            List<Place> recommendedPlaces = recommendationService.getTop20IdsByCombinedScore(userId);
            return ResponseEntity.ok(new ResponseDTO<>(true, recommendedPlaces));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(false, e.getMessage()));
        }
    }
}