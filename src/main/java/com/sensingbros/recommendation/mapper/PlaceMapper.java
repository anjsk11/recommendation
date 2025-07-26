package com.sensingbros.recommendation.mapper;

import com.sensingbros.recommendation.domain.Place;
import com.sensingbros.recommendation.model.PlaceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper {

    private final ModelMapper modelMapper;

    // ModelMapper를 주입
    public PlaceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // User 엔티티 → UserDto 매핑
    public PlaceDTO toDto(Place place) {
        return modelMapper.map(place, PlaceDTO.class);
    }

    // UserDto → User 엔티티 매핑
    public Place toEntity(PlaceDTO placeDto) {
        return modelMapper.map(placeDto, Place.class);
    }
}
