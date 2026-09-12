package com.example.tvmaze.controller;

import com.example.tvmaze.dto.response.ShowResponse;
import com.example.tvmaze.dto.response.ShowSearchResponse;
import com.example.tvmaze.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shows", description = "Show related operations")
@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @Operation(summary = "Search shows by query")
    @ApiResponse(responseCode = "200", description = "List of shows matching the query")
    @GetMapping("/search")
    public ResponseEntity<List<ShowSearchResponse>> searchShows(
            @Parameter(description = "Search query", required = true)
            @RequestParam("q") String query) {

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query parameter 'q' must not be empty");
        }

        return ResponseEntity.ok(showService.searchShows(query));
    }

    @Operation(summary = "Get show by ID")
    @ApiResponse(responseCode = "200", description = "Show found")
    @ApiResponse(responseCode = "404", description = "Show not found")
    @GetMapping("/{id}")
    public ResponseEntity<ShowResponse> getShowById(
            @Parameter(description = "TVMaze show ID", required = true)
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(showService.getShowById(id));
    }
}