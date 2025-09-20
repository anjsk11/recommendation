package com.sensingbros.recommendation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensingbros.recommendation.repository.ReviewRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FlaskService {


    private final RestTemplate restTemplate = new RestTemplate();
    private final ReviewRepository reviewRepository;
    private final ObjectMapper objectMapper;

    public Map<String, Double> getRecommendationFromFlask(UUID userId) throws JsonProcessingException {
        // 1. DB에서 사용자가 리뷰를 남긴 식당 ID 목록 조회
        List<String> itemIds = reviewRepository.findRestaurantIdsByUserId(userId);

        // 2. Flask 서버에 보낼 요청 데이터 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("item_ids", itemIds);

        String flaskUrl = "http://3.36.236.71:5000/recommend";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = objectMapper.writeValueAsString(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        // 3. Flask 서버로 POST 요청 전송 - String으로 받기
        ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, entity, String.class);

        // 4. JSON 응답을 직접 파싱
        if (response.getStatusCode() == HttpStatus.OK) {
            return parseFlaskResponse(response.getBody());
        } else {
            throw new RuntimeException("Flask 서버 통신 실패");
        }
    }

    // JSON 응답을 직접 파싱하는 함수
    private Map<String, Double> parseFlaskResponse(String jsonResponse) throws JsonProcessingException {
        // ObjectMapper를 사용해 JSON 문자열을 Map<String, Double>로 직접 변환
        return objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Double>>() {});
    }
}