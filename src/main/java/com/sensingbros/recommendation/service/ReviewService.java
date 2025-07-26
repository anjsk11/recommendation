package com.sensingbros.recommendation.service;

import com.sensingbros.recommendation.domain.Place;
import com.sensingbros.recommendation.domain.Review;
import com.sensingbros.recommendation.domain.Users;
import com.sensingbros.recommendation.model.CreateReviewRequestDTO;
import com.sensingbros.recommendation.model.ReviewResponseDTO;
import com.sensingbros.recommendation.repository.PlaceRepository;
import com.sensingbros.recommendation.repository.ReviewRepository;
import com.sensingbros.recommendation.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public void saveReview(CreateReviewRequestDTO reviewDTO, UUID id) {
        Review review = modelMapper.map(reviewDTO, Review.class);

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));

        Place place = placeRepository.findById(reviewDTO.getPlaceId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 장소"));

        review.setUser(user);
        review.setPlace(place);

        reviewRepository.save(review);
    }

//    public List<ReviewResponseDTO> getReviewByUserId(UUID id) {
//
//    }

}
