package com.example.tvmaze.service.impl;

import com.example.tvmaze.dto.response.CommentResponse;
import com.example.tvmaze.dto.response.ShowResponse;
import com.example.tvmaze.dto.response.ShowSearchResponse;
import com.example.tvmaze.dto.tvmaze.TvMazeSearchResult;
import com.example.tvmaze.dto.tvmaze.TvMazeShow;
import com.example.tvmaze.exception.ExternalApiException;
import com.example.tvmaze.mapper.ShowMapper;
import com.example.tvmaze.model.CommentDocument;
import com.example.tvmaze.model.ShowDocument;
import com.example.tvmaze.repository.CommentRepository;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final RestClient tvMazeRestClient;
    private final ShowMapper showMapper;
    private final ShowRepository showRepository;
    private final CommentRepository commentRepository;

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

            List<TvMazeShow> shows = results.stream()
                    .map(TvMazeSearchResult::getShow)
                    .toList();

            // Single query to fetch all comments for the resulting shows (avoid N+1)
            List<Long> showIds = shows.stream()
                    .map(TvMazeShow::getId)
                    .toList();

            Map<Long, List<CommentResponse>> commentsByShowId = commentRepository
                    .findByShowIdIn(showIds)
                    .stream()
                    .collect(Collectors.groupingBy(
                            CommentDocument::getShowId,
                            Collectors.mapping(showMapper::toCommentResponse, Collectors.toList())
                    ));

            return shows.stream()
                    .map(show -> showMapper.toSearchResponse(
                            show,
                            commentsByShowId.getOrDefault(show.getId(), List.of())
                    ))
                    .toList();

        } catch (Exception ex) {
            log.error("Error consuming TVMaze search API", ex);
            throw new ExternalApiException("Error consuming TVMaze search API", ex);
        }
    }

    @Override
    public ShowResponse getShowById(Long id) {
        log.info("Fetching show with id={}", id);

        Optional<ShowDocument> cached = showRepository.findById(id);
        if (cached.isPresent()) {
            log.info("Cache HIT for show id={}", id);
            return showMapper.toShowResponse(cached.get());
        }
        log.info("Cache MISS for show id={}, calling TVMaze API", id);

        try {
            TvMazeShow show = tvMazeRestClient.get()
                    .uri("/shows/{id}", id)
                    .retrieve()
                    .body(TvMazeShow.class);

            ShowResponse response = showMapper.toShowResponse(show);

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