package com.sensingbros.recommendation.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.sensingbros.recommendation.service.FlaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FlaskController {

    private final FlaskService  flaskService;

    public FlaskController(FlaskService flaskService) {
        this.flaskService = flaskService;
    }

    @GetMapping("/recommend")
    public Mono<JsonNode> callFlask() {
        return flaskService.callFlaskRecommend();
    }
}