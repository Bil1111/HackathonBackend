package com.example.config.trivia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/trivia")
public class TriviaController {

    @Autowired
    private TriviaService triviaService;

    @GetMapping
    public Mono<TriviaResponse> getTrivia(
            @RequestParam(defaultValue = "5") int amount,
            @RequestParam(defaultValue = "medium") String difficulty,
            @RequestParam(defaultValue = "multiple") String type) {
        return triviaService.getTriviaQuestions(amount, difficulty, type);
    }
}

