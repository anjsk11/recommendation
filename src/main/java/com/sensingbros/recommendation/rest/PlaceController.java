package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.model.*;
import com.sensingbros.recommendation.service.PlaceService;
import com.sensingbros.recommendation.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final ReviewService reviewService;

    // UserService 주입
    public PlaceController(PlaceService placeService,  ReviewService reviewService) {
        this.placeService = placeService;
        this.reviewService = reviewService;
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<ResponseDTO<?>> getPlaceById(@PathVariable Integer placeId) {
        try {
            Optional<PlaceDTO> placeDTOOptional = placeService.getPlaceById(placeId);
            if (placeDTOOptional.isPresent()) {
                PlaceDTO placeDTO = placeDTOOptional.get();
                return ResponseEntity.ok(new ResponseDTO<>(true, placeDTO));
            } else {
                // 장소가 없는 경우
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(false, null));
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(false, e.getMessage()));
        }
    }

    @GetMapping("/{placeId}/reviews")
    public ResponseDTO<List<ReviewResponseDTO>> getReviewsByPlace(
            @PathVariable Integer placeId,
            @AuthenticationPrincipal Jwt jwt) {
        try {
            List<ReviewResponseDTO> reviews = reviewService.getReviewsByPlaceId(placeId, jwt);
            return new ResponseDTO<>(true, reviews);
        } catch (Exception e) {
            // 예외 발생 시 success_code는 false, data는 null 또는 에러 메시지로 처리
            return new ResponseDTO<>(false);
        }
    }
}
