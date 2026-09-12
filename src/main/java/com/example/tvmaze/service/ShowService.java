package com.example.tvmaze.service;

import com.example.tvmaze.dto.response.ShowResponse;
import com.example.tvmaze.dto.response.ShowSearchResponse;

import java.util.List;

public interface ShowService {
    List<ShowSearchResponse> searchShows(String query);
    ShowResponse getShowById(Long id);
}