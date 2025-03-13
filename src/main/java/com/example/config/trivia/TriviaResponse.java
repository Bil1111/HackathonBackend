package com.example.config.trivia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TriviaResponse {

    @JsonProperty("response_code")
    private int responseCode;

    private List<TriviaQuestion> results;

}

