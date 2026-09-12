package com.example.tvmaze.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvMazeShow {
    private Long id;
    private String name;
    private String summary;
    private List<String> genres;
    private TvMazeNetwork network;
    private TvMazeNetwork webChannel;
}