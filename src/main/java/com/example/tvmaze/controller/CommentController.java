package com.example.tvmaze.controller;

import com.example.tvmaze.dto.request.CommentRequest;
import com.example.tvmaze.dto.response.ApiResponse;
import com.example.tvmaze.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comments", description = "Show comments operations")
@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Add a comment and rating to a show")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Comment created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/{showId}/comments")
    public ResponseEntity<ApiResponse> addComment(
            @Parameter(description = "TVMaze show ID", required = true)
            @PathVariable("showId") Long showId,
            @Valid @RequestBody CommentRequest request) {

        ApiResponse response = commentService.addComment(showId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}