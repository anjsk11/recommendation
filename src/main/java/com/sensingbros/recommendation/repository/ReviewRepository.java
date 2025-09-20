package com.sensingbros.recommendation.repository;

import com.sensingbros.recommendation.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // 필요에 따라 커스텀 메서드 추가 가능
    List<Review> findByPlaceId(Integer placeId);
    Integer countByPlaceId(Integer placeId);

    @Query("SELECT AVG(r.score) FROM Review r WHERE r.place.id = :placeId")
    BigDecimal avgRatingByPlaceId(@Param("placeId") Integer placeId);

    // 사용자가 리뷰를 작성한 식당들 리스트 가져오기
    @Query("SELECT DISTINCT r.place.id FROM Review r WHERE r.user.id = :userId")
    List<String> findRestaurantIdsByUserId(@Param("userId") UUID userId);
}
