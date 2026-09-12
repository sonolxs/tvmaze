package com.example.tvmaze.service.impl;

import com.example.tvmaze.dto.request.CommentRequest;
import com.example.tvmaze.dto.response.ApiResponse;
import com.example.tvmaze.model.CommentDocument;
import com.example.tvmaze.repository.CommentRepository;
import com.example.tvmaze.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public ApiResponse addComment(Long showId, CommentRequest request) {
        log.info("Adding comment for showId={} with rating={}", showId, request.getRating());

        CommentDocument document = CommentDocument.builder()
                .showId(showId)
                .comment(request.getComment())
                .rating(request.getRating())
                .createdAt(Instant.now())
                .build();

        CommentDocument saved = commentRepository.save(document);
        log.info("Comment saved with id={} for showId={}", saved.getId(), showId);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Comment added successfully for show " + showId)
                .build();
    }
}