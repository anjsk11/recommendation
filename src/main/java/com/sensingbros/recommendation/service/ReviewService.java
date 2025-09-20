package com.sensingbros.recommendation.service;

import com.sensingbros.recommendation.domain.Place;
import com.sensingbros.recommendation.domain.Review;
import com.sensingbros.recommendation.domain.Users;
import com.sensingbros.recommendation.model.CreateReviewRequestDTO;
import com.sensingbros.recommendation.model.ReviewResponseDTO;
import com.sensingbros.recommendation.repository.PlaceRepository;
import com.sensingbros.recommendation.repository.ReviewRepository;
import com.sensingbros.recommendation.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public ReviewService(ModelMapper modelMapper, ReviewRepository reviewRepository, UsersRepository usersRepository, PlaceRepository placeRepository) {
        this.modelMapper = modelMapper;
        this.reviewRepository = reviewRepository;
        this.usersRepository = usersRepository;
        this.placeRepository = placeRepository;
    }

    @Transactional
    public void saveReview(CreateReviewRequestDTO reviewDTO, UUID id) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setId(null);

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));

        Place place = placeRepository.findById(reviewDTO.getPlaceId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 장소"));

        review.setUser(user);
        review.setPlace(place);

        reviewRepository.save(review);

        // 통계 갱신
        Integer reviewCount = reviewRepository.countByPlaceId(place.getId());
        BigDecimal reviewAvg = reviewRepository.avgRatingByPlaceId(place.getId());

        place.setScoreCount(reviewCount);
        place.setAvgScore(reviewAvg != null ? reviewAvg : BigDecimal.valueOf(0));  // null 처리
        placeRepository.save(place);
    }

    public List<ReviewResponseDTO> getReviewsByPlaceId(Integer placeId, Jwt jwt) {
        List<Review> reviews = reviewRepository.findByPlaceId(placeId);

        return reviews.stream()
                .map(review -> new ReviewResponseDTO(
                        review.getId(),
                        review.getUser().getName(),
                        review.getScore(),
                        review.getBody()
                ))
                .collect(Collectors.toList());
    }

    // 부분 수정 메서드
    public void patchReview(Integer reviewId, CreateReviewRequestDTO dto, UUID userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰를 찾을 수 없습니다."));
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("리뷰 수정 권한이 없습니다.");
        }

        if (dto.getScore() != null) {
            review.setScore(dto.getScore());
        }
        if (dto.getBody() != null) {
            review.setBody(dto.getBody());
        }
        reviewRepository.save(review);
    }

}
