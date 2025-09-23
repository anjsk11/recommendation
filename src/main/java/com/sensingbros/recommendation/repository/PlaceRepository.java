package com.sensingbros.recommendation.repository;

import com.sensingbros.recommendation.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    @Query("SELECT p.id AS id, p.heatmapx AS heatmapx, p.heatmapy AS heatmapy FROM Place p")
    List<PlaceHeatmapProjection> findAllPlaceHeatmap();
}