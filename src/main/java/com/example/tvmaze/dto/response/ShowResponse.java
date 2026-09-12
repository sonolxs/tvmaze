package com.example.tvmaze.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShowResponse {

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
    private ScheduleResponse schedule;
    private RatingResponse rating;
    private Integer weight;
    private NetworkResponse network;
    private NetworkResponse webChannel;
    private CountryResponse dvdCountry;
    private ExternalsResponse externals;
    private ImageResponse image;
    private String summary;
    private Long updated;
    private LinksResponse links;
    private List<CommentResponse> comments;

    @Data
    @Builder
    public static class ScheduleResponse {
        private String time;
        private List<String> days;
    }

    @Data
    @Builder
    public static class RatingResponse {
        private Double average;
    }

    @Data
    @Builder
    public static class NetworkResponse {
        private Long id;
        private String name;
        private CountryResponse country;
        private String officialSite;
    }

    @Data
    @Builder
    public static class CountryResponse {
        private String name;
        private String code;
        private String timezone;
    }

    @Data
    @Builder
    public static class ExternalsResponse {
        private Long tvrage;
        private Long thetvdb;
        private String imdb;
    }

    @Data
    @Builder
    public static class ImageResponse {
        private String medium;
        private String original;
    }

    @Data
    @Builder
    public static class LinksResponse {
        private LinkResponse self;
        private LinkResponse previousepisode;
        private LinkResponse nextepisode;
    }

    @Data
    @Builder
    public static class LinkResponse {
        private String href;
        private String name;
    }

}