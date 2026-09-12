package com.example.tvmaze.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvMazeExternals {
    private Long tvrage;
    private Long thetvdb;
    private String imdb;
}