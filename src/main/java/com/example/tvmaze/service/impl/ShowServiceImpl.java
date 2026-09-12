package com.example.tvmaze.service.impl;

import com.example.tvmaze.dto.response.ShowResponse;
import com.example.tvmaze.dto.response.ShowSearchResponse;
import com.example.tvmaze.dto.tvmaze.TvMazeSearchResult;
import com.example.tvmaze.dto.tvmaze.TvMazeShow;
import com.example.tvmaze.exception.ExternalApiException;
import com.example.tvmaze.mapper.ShowMapper;
import com.example.tvmaze.model.ShowDocument;
import com.example.tvmaze.repository.ShowRepository;
import com.example.tvmaze.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final RestClient tvMazeRestClient;
    private final ShowMapper showMapper;
    private final ShowRepository showRepository;

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

    @Override
    public ShowResponse getShowById(Long id) {
        log.info("Fetching show with id={}", id);

        // 1. Check cache in MongoDB
        Optional<ShowDocument> cached = showRepository.findById(id);
        if (cached.isPresent()) {
            log.info("Cache HIT for show id={}", id);
            return showMapper.toShowResponse(cached.get());
        }
        log.info("Cache MISS for show id={}, calling TVMaze API", id);

        // 2. Cache miss -> call TVMaze
        try {
            TvMazeShow show = tvMazeRestClient.get()
                    .uri("/shows/{id}", id)
                    .retrieve()
                    .body(TvMazeShow.class);

            ShowResponse response = showMapper.toShowResponse(show);

            // 3. Save to MongoDB
            ShowDocument document = showMapper.toDocument(response);
            showRepository.save(document);
            log.info("Show id={} saved to MongoDB cache", id);

            return response;

        } catch (HttpClientErrorException.NotFound ex) {
            log.warn("Show with id={} not found in TVMaze", id);
            throw ex;
        } catch (Exception ex) {
            log.error("Error consuming TVMaze show API for id={}", id, ex);
            throw new ExternalApiException("Error consuming TVMaze show API", ex);
        }
    }
}