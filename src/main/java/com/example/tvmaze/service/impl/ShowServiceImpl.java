package com.example.tvmaze.service.impl;

import com.example.tvmaze.dto.response.ShowSearchResponse;
import com.example.tvmaze.dto.tvmaze.TvMazeSearchResult;
import com.example.tvmaze.exception.ExternalApiException;
import com.example.tvmaze.mapper.ShowMapper;
import com.example.tvmaze.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final RestClient tvMazeRestClient;
    private final ShowMapper showMapper;

    @Override
    public List<ShowSearchResponse> searchShows(String query) {
        log.info("Searching shows with query='{}'", query);
        try {
            List<TvMazeSearchResult> results = tvMazeRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/shows")
                            .queryParam("q", query)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (results == null || results.isEmpty()) {
                return Collections.emptyList();
            }

            return results.stream()
                    .map(TvMazeSearchResult::getShow)
                    .map(showMapper::toSearchResponse)
                    .toList();

        } catch (Exception ex) {
            log.error("Error consuming TVMaze search API", ex);
            throw new ExternalApiException("Error consuming TVMaze search API", ex);
        }
    }
}