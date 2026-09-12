package com.example.tvmaze.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvMazeSchedule {
    private String time;
    private List<String> days;
}