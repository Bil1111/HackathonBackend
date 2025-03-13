package com.example.config.habitica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/habitica")
public class HabiticaController {

    @Autowired
    private HabiticaService habiticaService;

    @PostMapping("/tasks")
    public ResponseEntity<String> createTask(
            @RequestHeader("x-api-user") String userId,
            @RequestHeader("x-api-key") String apiToken,
            @RequestBody Map<String, Object> taskData
    ) {
        String response = habiticaService.createHabiticaTask(userId, apiToken, taskData);
        return ResponseEntity.ok(response);
    }
}
