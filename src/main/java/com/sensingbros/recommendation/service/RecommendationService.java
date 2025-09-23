package com.sensingbros.recommendation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensingbros.recommendation.domain.Place;
import com.sensingbros.recommendation.domain.Users;
import com.sensingbros.recommendation.repository.PlaceHeatmapProjection;
import com.sensingbros.recommendation.repository.PlaceRepository;
import com.sensingbros.recommendation.repository.ReviewRepository;
import com.sensingbros.recommendation.repository.UsersRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ReviewRepository reviewRepository;
    private final ObjectMapper objectMapper;
    private final PlaceRepository placeRepository;
    private final UsersRepository usersRepository;
    private static final double HEATMAP_WEIGHT = 5;
    private static final Integer RETURN_SIZE = 20;

    public Map<Integer, Double> getRecommendationFromFlask(UUID userId) throws JsonProcessingException {
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
    private Map<Integer, Double> parseFlaskResponse(String jsonResponse) throws JsonProcessingException {
        // ObjectMapper를 사용해 JSON 문자열을 Map<String, Double>로 직접 변환
        return objectMapper.readValue(jsonResponse, new TypeReference<Map<Integer, Double>>() {});
    }

    // 히트맵의 정보를 가져오는 함수
    public Map<Integer, Double> getMapIdToHeatmapValue(UUID userId) {
        // 1. DB에서 place의 모든 정보를 조회
        List<PlaceHeatmapProjection> places = placeRepository.findAllPlaceHeatmap();

        // 2. id와 heatmap 값 매핑 저장용 Map 생성
        Map<Integer, Double> idToHeatmapValue = new HashMap<>();

        // 3. 현재 시간대에 맞는 히트맵 가져오기
        Integer[][] heatmap = getHeatmapByTime(userId);
        // 3.1. 히트맵 최대값 찾기
        int heatmapmaxVal = 0;
        for (int i = 0; i < heatmap.length; i++) {
            for (int j = 0; j < heatmap[0].length; j++) {
                if (heatmap[i][j] > heatmapmaxVal) {
                    heatmapmaxVal = heatmap[i][j];
                }
            }
        }
        if (heatmapmaxVal == 0) heatmapmaxVal = 1;  // 0으로 나누는 오류 방지

        // 4. 각 place 객체에서 heatmapX, heatmapY 값 사용해 heatmap 값 추출 후 저장
        for (PlaceHeatmapProjection place : places) {
            Integer id = place.getId();
            int x = place.getHeatmapx();
            int y = place.getHeatmapy();

            // heatmap 배열 범위 검사
            if (x >= 0 && x < heatmap.length && y >= 0 && y < heatmap[0].length) {
                Double value = ((double) heatmap[x][y] / heatmapmaxVal) * HEATMAP_WEIGHT;
                idToHeatmapValue.put(id, value);
            } else {
                // 범위 밖 좌표인 경우 기본값(예: 0) 처리
                idToHeatmapValue.put(id, 0.0);
            }
        }

        return idToHeatmapValue;
    }

    // 시간대에 따라 알맞은 히트맵을 가져오는 함수
    public Integer[][] getHeatmapByTime(UUID userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalTime now = LocalTime.now();

        if (now.isBefore(LocalTime.of(8, 0))) {
            return user.getMorningHeatmap();  // 00:00 ~ 07:59
        } else if (now.isBefore(LocalTime.of(16, 0))) {
            return user.getNoonHeatmap();     // 08:00 ~ 15:59
        } else {
            return user.getNightHeatmap();    // 16:00 ~ 23:59
        }
    }

    // Map을 2개 가져와 Merge하는 함수
    public Map<Integer, Double> mergeScores(Map<Integer, Double> heatmapScores, Map<Integer, Double> flaskScores) {
        Map<Integer, Double> merged = new HashMap<>(heatmapScores);

        for (Map.Entry<Integer, Double> entry : flaskScores.entrySet()) {
            merged.merge(entry.getKey(), entry.getValue(), Double::sum);
        }

        return merged;
    }

    // Map을 가져오고 Merge하는 함수로 보내는 함수
    public Map<Integer, Double> getCombinedRecommendation(UUID userId) throws JsonProcessingException {
        // 1. 히트맵 점수 가져오기
        Map<Integer, Double> heatmapScores = getMapIdToHeatmapValue(userId);

        // 2. Flask 추천 점수 가져오기
        Map<Integer, Double> flaskScores = getRecommendationFromFlask(userId);

        return mergeScores(heatmapScores, flaskScores);
    }

    // 합친 Map을 정렬하고 상위 20개의 id로 바꾸는 함수
    public List<Place> getTop20IdsByCombinedScore(UUID userId) throws JsonProcessingException {
        Map<Integer, Double> combinedScores = getCombinedRecommendation(userId);

        List<Integer> topIds = combinedScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(RETURN_SIZE)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return placeRepository.findAllById(topIds);
    }
}