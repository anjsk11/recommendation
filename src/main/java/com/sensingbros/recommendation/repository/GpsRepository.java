package com.sensingbros.recommendation.repository;

import com.sensingbros.recommendation.domain.Gps;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpsRepository extends JpaRepository<Gps, Integer> {
}
