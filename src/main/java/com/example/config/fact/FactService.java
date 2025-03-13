package com.example.config.fact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FactService {
    @Autowired
    private RestTemplate restTemplate;
    private static final String API_URL = "http://numbersapi.com/random/year";

    public String getRandomFact() {
        return restTemplate.getForObject(API_URL, String.class);
    }
}

