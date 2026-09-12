package com.example.tvmaze.mapper;

import com.example.tvmaze.dto.response.ShowResponse;
import com.example.tvmaze.dto.response.ShowSearchResponse;
import com.example.tvmaze.dto.tvmaze.*;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    // ---------- SEARCH ----------

    public ShowSearchResponse toSearchResponse(TvMazeShow show) {
        if (show == null) {
            return null;
        }
        return ShowSearchResponse.builder()
                .id(show.getId())
                .name(show.getName())
                .channel(resolveChannelName(show))
                .summary(show.getSummary())
                .genres(show.getGenres())
                .build();
    }

    // ---------- SHOW COMPLETO ----------

    public ShowResponse toShowResponse(TvMazeShow show) {
        if (show == null) {
            return null;
        }
        return ShowResponse.builder()
                .id(show.getId())
                .url(show.getUrl())
                .name(show.getName())
                .type(show.getType())
                .language(show.getLanguage())
                .genres(show.getGenres())
                .status(show.getStatus())
                .runtime(show.getRuntime())
                .averageRuntime(show.getAverageRuntime())
                .premiered(show.getPremiered())
                .ended(show.getEnded())
                .officialSite(show.getOfficialSite())
                .schedule(mapSchedule(show.getSchedule()))
                .rating(mapRating(show.getRating()))
                .weight(show.getWeight())
                .network(mapNetwork(show.getNetwork()))
                .webChannel(mapNetwork(show.getWebChannel()))
                .dvdCountry(mapCountry(show.getDvdCountry()))
                .externals(mapExternals(show.getExternals()))
                .image(mapImage(show.getImage()))
                .summary(show.getSummary())
                .updated(show.getUpdated())
                .links(mapLinks(show.getLinks()))
                .build();
    }

    // ---------- HELPERS ----------

    private String resolveChannelName(TvMazeShow show) {
        if (show.getNetwork() != null && show.getNetwork().getName() != null) {
            return show.getNetwork().getName();
        }
        if (show.getWebChannel() != null && show.getWebChannel().getName() != null) {
            return show.getWebChannel().getName();
        }
        return null;
    }

    private ShowResponse.ScheduleResponse mapSchedule(TvMazeSchedule schedule) {
        if (schedule == null) return null;
        return ShowResponse.ScheduleResponse.builder()
                .time(schedule.getTime())
                .days(schedule.getDays())
                .build();
    }

    private ShowResponse.RatingResponse mapRating(TvMazeRating rating) {
        if (rating == null) return null;
        return ShowResponse.RatingResponse.builder()
                .average(rating.getAverage())
                .build();
    }

    private ShowResponse.NetworkResponse mapNetwork(TvMazeNetwork network) {
        if (network == null) return null;
        return ShowResponse.NetworkResponse.builder()
                .id(network.getId())
                .name(network.getName())
                .country(mapCountry(network.getCountry()))
                .officialSite(network.getOfficialSite())
                .build();
    }

    private ShowResponse.CountryResponse mapCountry(TvMazeCountry country) {
        if (country == null) return null;
        return ShowResponse.CountryResponse.builder()
                .name(country.getName())
                .code(country.getCode())
                .timezone(country.getTimezone())
                .build();
    }

    private ShowResponse.ExternalsResponse mapExternals(TvMazeExternals externals) {
        if (externals == null) return null;
        return ShowResponse.ExternalsResponse.builder()
                .tvrage(externals.getTvrage())
                .thetvdb(externals.getThetvdb())
                .imdb(externals.getImdb())
                .build();
    }

    private ShowResponse.ImageResponse mapImage(TvMazeImage image) {
        if (image == null) return null;
        return ShowResponse.ImageResponse.builder()
                .medium(image.getMedium())
                .original(image.getOriginal())
                .build();
    }

    private ShowResponse.LinksResponse mapLinks(TvMazeLinks links) {
        if (links == null) return null;
        return ShowResponse.LinksResponse.builder()
                .self(mapLink(links.getSelf()))
                .previousepisode(mapLink(links.getPreviousepisode()))
                .nextepisode(mapLink(links.getNextepisode()))
                .build();
    }

    private ShowResponse.LinkResponse mapLink(TvMazeLink link) {
        if (link == null) return null;
        return ShowResponse.LinkResponse.builder()
                .href(link.getHref())
                .name(link.getName())
                .build();
    }
}