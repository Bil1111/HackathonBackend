package com.example.config.habitica;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
public class HabiticaService {

    private static final Logger logger = LoggerFactory.getLogger(HabiticaService.class);
    @Autowired
    private RestTemplate restTemplate;

    public String createHabiticaTask(String userId, String apiToken, Map<String, Object> taskData) {
        String url = "https://habitica.com/api/v3/tasks/user";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-user", userId);
        headers.set("x-api-key", apiToken); // правильний заголовок
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(taskData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            logger.info("Habitica task created successfully. Response: {}", response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Error creating Habitica task. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error("Unexpected error when creating Habitica task", e);
            return "Unexpected error: " + e.getMessage();
        }
    }
}
