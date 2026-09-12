package com.example.tvmaze.service;

import com.example.tvmaze.dto.request.CommentRequest;
import com.example.tvmaze.dto.response.ApiResponse;

public interface CommentService {
    ApiResponse addComment(Long showId, CommentRequest request);
}