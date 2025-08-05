package com.sensingbros.recommendation.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FlaskService {

    private final WebClient webClient;

    public FlaskService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<JsonNode> callFlaskRecommend() {
        return webClient.post()
                .uri("http://3.36.236.71:8000/recommend")
                .bodyValue("{}")  // 빈 JSON 바디
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}