package com.sensingbros.recommendation.repository;

import com.sensingbros.recommendation.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

}