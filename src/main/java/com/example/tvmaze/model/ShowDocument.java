package com.example.tvmaze.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shows")
public class ShowDocument {

    @Id
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
    private Schedule schedule;
    private Rating rating;
    private Integer weight;
    private Network network;
    private Network webChannel;
    private Country dvdCountry;
    private Externals externals;
    private Image image;
    private String summary;
    private Long updated;
    private Links links;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Schedule {
        private String time;
        private List<String> days;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rating {
        private Double average;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Network {
        private Long id;
        private String name;
        private Country country;
        private String officialSite;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Country {
        private String name;
        private String code;
        private String timezone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Externals {
        private Long tvrage;
        private Long thetvdb;
        private String imdb;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String medium;
        private String original;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Links {
        private Link self;
        private Link previousepisode;
        private Link nextepisode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        private String href;
        private String name;
    }
}