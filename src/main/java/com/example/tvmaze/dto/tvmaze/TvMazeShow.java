package com.example.tvmaze.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvMazeShow {
    private Long id;
    private String url;
    private String name;
    private String type;
    private String language;
    private List<String> genres;
    private String status;
    private Integer runtime;
    private Integer averageRuntime;
    private String premiered;
    private String ended;
    private String officialSite;
    private TvMazeSchedule schedule;
    private TvMazeRating rating;
    private Integer weight;
    private TvMazeNetwork network;
    private TvMazeNetwork webChannel;
    private TvMazeCountry dvdCountry;
    private TvMazeExternals externals;
    private TvMazeImage image;
    private String summary;
    private Long updated;

    @JsonProperty("_links")
    private TvMazeLinks links;
}