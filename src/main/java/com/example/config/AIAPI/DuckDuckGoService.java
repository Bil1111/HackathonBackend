package com.example.config.AIAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DuckDuckGoService {

    @Autowired
    private RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DuckDuckGoService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getInstantAnswer(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String apiUrl = "https://api.duckduckgo.com/?q=" + encodedQuery + "&format=json&no_redirect=1&no_html=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String answer = jsonNode.path("AbstractText").asText();
                return answer.isEmpty() ? "Вибачте, я не знайшов відповіді на це питання." : answer;
            }

            return "Помилка при отриманні відповіді від DuckDuckGo.";
        } catch (Exception e) {
            return "Помилка при запиті до DuckDuckGo: " + e.getMessage();
        }
    }
}
