package com.sensingbros.recommendation.service;

import com.sensingbros.recommendation.domain.Place;
import com.sensingbros.recommendation.mapper.PlaceMapper;
import com.sensingbros.recommendation.model.PlaceDTO;
import com.sensingbros.recommendation.model.UsersDTO;
import com.sensingbros.recommendation.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlaceService {
    private final PlaceMapper placeMapper;
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceMapper placeMapper, PlaceRepository placeRepository) {
        this.placeMapper = placeMapper;
        this.placeRepository = placeRepository;
    }

    public Optional<PlaceDTO> getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .map(placeMapper::toDto);
    }

}