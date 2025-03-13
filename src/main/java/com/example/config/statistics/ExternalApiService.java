package com.example.config.statistics;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import java.util.Map;
import java.util.HashMap;

@Service
public class ExternalApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    // 1. Habitica API - створення завдань


    // 2. Open Trivia Database - отримання вікторин
    public String getTriviaQuestions(int amount, String category, String difficulty) {
        String url = String.format("https://opentdb.com/api.php?amount=%d&category=%s&difficulty=%s&type=multiple", amount, category, difficulty);
        return restTemplate.getForObject(url, String.class);
    }

    // 3. Badgr API - отримання списку бейджів
    public String getBadges(String accessToken) {
        String url = "https://api.badgr.io/v2/badgeclasses";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
