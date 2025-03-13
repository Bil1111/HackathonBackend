package com.example.config.AIAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/duckduckgo")
public class DuckDuckGoController {

    @Autowired
    private  DuckDuckGoService duckDuckGoService;


    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam String query) {
        String result = duckDuckGoService.getInstantAnswer(query);
        return ResponseEntity.ok(result);
    }
}

