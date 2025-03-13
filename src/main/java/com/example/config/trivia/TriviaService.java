package com.example.config.trivia;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TriviaService {

    private final WebClient webClient;

    public TriviaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://opentdb.com").build();
    }

    public Mono<TriviaResponse> getTriviaQuestions(int amount, String difficulty, String type) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api.php")
                        .queryParam("amount", amount)
                        .queryParam("difficulty", difficulty)
                        .queryParam("type", type)
                        .build())
                .retrieve()
                .bodyToMono(TriviaResponse.class);
    }
}

