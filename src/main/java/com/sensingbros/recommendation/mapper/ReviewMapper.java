//package com.sensingbros.recommendation.mapper;
//
//import com.sensingbros.recommendation.model.ReviewDTO;
//import com.sensingbros.recommendation.domain.Review;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ReviewMapper {
//
//    private final ModelMapper modelMapper;
//
//    public ReviewMapper(ModelMapper modelMapper) {
//        this.modelMapper = modelMapper;
//    }
//
//    // Entity → DTO
//    public ReviewDTO toDto(Review review) {
//        return modelMapper.map(review, ReviewDTO.class);
//    }
//
//    // DTO → Entity
//    public Review toEntity(ReviewDTO reviewDto) {
//        return modelMapper.map(reviewDto, Review.class);
//    }
//}
