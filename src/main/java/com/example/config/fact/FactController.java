package com.example.config.fact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fact")
public class FactController {

    @Autowired
    private  FactService factService;


    @GetMapping
    public ResponseEntity<Map<String, String>> getFact() {
        Map<String, String> response = new HashMap<>();
        response.put("fact", factService.getRandomFact());
        return ResponseEntity.ok(response);
    }

}
