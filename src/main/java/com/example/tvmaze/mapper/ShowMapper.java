package com.example.tvmaze.mapper;

import com.example.tvmaze.dto.response.ShowSearchResponse;
import com.example.tvmaze.dto.tvmaze.TvMazeShow;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    public ShowSearchResponse toSearchResponse(TvMazeShow show) {
        if (show == null) {
            return null;
        }
        return ShowSearchResponse.builder()
                .id(show.getId())
                .name(show.getName())
                .channel(resolveChannel(show))
                .summary(show.getSummary())
                .genres(show.getGenres())
                .build();
    }

    private String resolveChannel(TvMazeShow show) {
        if (show.getNetwork() != null && show.getNetwork().getName() != null) {
            return show.getNetwork().getName();
        }
        if (show.getWebChannel() != null && show.getWebChannel().getName() != null) {
            return show.getWebChannel().getName();
        }
        return null;
    }
}