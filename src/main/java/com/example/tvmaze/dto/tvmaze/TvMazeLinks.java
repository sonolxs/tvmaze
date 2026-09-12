package com.example.tvmaze.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvMazeLinks {
    private TvMazeLink self;
    private TvMazeLink previousepisode;
    private TvMazeLink nextepisode;
}