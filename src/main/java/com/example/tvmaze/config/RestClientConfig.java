package com.example.tvmaze.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${tvmaze.api.base-url}")
    private String baseUrl;

    @Bean
    public RestClient tvMazeRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}