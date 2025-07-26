package com.sensingbros.recommendation.repository;

import com.sensingbros.recommendation.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 필요에 따라 커스텀 메서드 추가 가능
}
