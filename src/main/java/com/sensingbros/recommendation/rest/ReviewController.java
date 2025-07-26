package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.model.CreateReviewRequestDTO;
import com.sensingbros.recommendation.model.ResponseDTO;
import com.sensingbros.recommendation.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 생성 API
    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createReview(@RequestBody CreateReviewRequestDTO reviewDTO, @AuthenticationPrincipal Jwt jwt) {
        try {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            reviewService.saveReview(reviewDTO, userId);
            return ResponseEntity.ok(new ResponseDTO<>(true));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseDTO<>(false, e.getMessage()));
        }
    }
}
