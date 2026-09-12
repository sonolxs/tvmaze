package com.example.tvmaze.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvMazeCountry {
    private String name;
    private String code;
    private String timezone;
}