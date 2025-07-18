package com.sensingbros.recommendation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IntArray2DConverter implements AttributeConverter<Integer[][], String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Integer[][] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert heatmap to JSON", e);
        }
    }

    @Override
    public Integer[][] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Integer[][].class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert JSON to heatmap", e);
        }
    }
}
