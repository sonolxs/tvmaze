package com.example.tvmaze.mapper;

import com.example.tvmaze.dto.response.ShowResponse;
import com.example.tvmaze.dto.response.ShowSearchResponse;
import com.example.tvmaze.dto.tvmaze.*;
import org.springframework.stereotype.Component;

import com.example.tvmaze.model.ShowDocument;
import com.example.tvmaze.dto.response.ShowResponse;

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
    // ---------- SHOW RESPONSE <-> DOCUMENT ----------

    public ShowDocument toDocument(ShowResponse response) {
        if (response == null) return null;
        return ShowDocument.builder()
                .id(response.getId())
                .url(response.getUrl())
                .name(response.getName())
                .type(response.getType())
                .language(response.getLanguage())
                .genres(response.getGenres())
                .status(response.getStatus())
                .runtime(response.getRuntime())
                .averageRuntime(response.getAverageRuntime())
                .premiered(response.getPremiered())
                .ended(response.getEnded())
                .officialSite(response.getOfficialSite())
                .schedule(mapScheduleToDoc(response.getSchedule()))
                .rating(mapRatingToDoc(response.getRating()))
                .weight(response.getWeight())
                .network(mapNetworkToDoc(response.getNetwork()))
                .webChannel(mapNetworkToDoc(response.getWebChannel()))
                .dvdCountry(mapCountryToDoc(response.getDvdCountry()))
                .externals(mapExternalsToDoc(response.getExternals()))
                .image(mapImageToDoc(response.getImage()))
                .summary(response.getSummary())
                .updated(response.getUpdated())
                .links(mapLinksToDoc(response.getLinks()))
                .build();
    }

    public ShowResponse toShowResponse(ShowDocument doc) {
        if (doc == null) return null;
        return ShowResponse.builder()
                .id(doc.getId())
                .url(doc.getUrl())
                .name(doc.getName())
                .type(doc.getType())
                .language(doc.getLanguage())
                .genres(doc.getGenres())
                .status(doc.getStatus())
                .runtime(doc.getRuntime())
                .averageRuntime(doc.getAverageRuntime())
                .premiered(doc.getPremiered())
                .ended(doc.getEnded())
                .officialSite(doc.getOfficialSite())
                .schedule(mapScheduleFromDoc(doc.getSchedule()))
                .rating(mapRatingFromDoc(doc.getRating()))
                .weight(doc.getWeight())
                .network(mapNetworkFromDoc(doc.getNetwork()))
                .webChannel(mapNetworkFromDoc(doc.getWebChannel()))
                .dvdCountry(mapCountryFromDoc(doc.getDvdCountry()))
                .externals(mapExternalsFromDoc(doc.getExternals()))
                .image(mapImageFromDoc(doc.getImage()))
                .summary(doc.getSummary())
                .updated(doc.getUpdated())
                .links(mapLinksFromDoc(doc.getLinks()))
                .build();
    }

    private ShowDocument.Schedule mapScheduleToDoc(ShowResponse.ScheduleResponse s) {
        if (s == null) return null;
        return ShowDocument.Schedule.builder().time(s.getTime()).days(s.getDays()).build();
    }

    private ShowResponse.ScheduleResponse mapScheduleFromDoc(ShowDocument.Schedule s) {
        if (s == null) return null;
        return ShowResponse.ScheduleResponse.builder().time(s.getTime()).days(s.getDays()).build();
    }

    private ShowDocument.Rating mapRatingToDoc(ShowResponse.RatingResponse r) {
        if (r == null) return null;
        return ShowDocument.Rating.builder().average(r.getAverage()).build();
    }

    private ShowResponse.RatingResponse mapRatingFromDoc(ShowDocument.Rating r) {
        if (r == null) return null;
        return ShowResponse.RatingResponse.builder().average(r.getAverage()).build();
    }

    private ShowDocument.Network mapNetworkToDoc(ShowResponse.NetworkResponse n) {
        if (n == null) return null;
        return ShowDocument.Network.builder()
                .id(n.getId())
                .name(n.getName())
                .country(mapCountryToDoc(n.getCountry()))
                .officialSite(n.getOfficialSite())
                .build();
    }

    private ShowResponse.NetworkResponse mapNetworkFromDoc(ShowDocument.Network n) {
        if (n == null) return null;
        return ShowResponse.NetworkResponse.builder()
                .id(n.getId())
                .name(n.getName())
                .country(mapCountryFromDoc(n.getCountry()))
                .officialSite(n.getOfficialSite())
                .build();
    }

    private ShowDocument.Country mapCountryToDoc(ShowResponse.CountryResponse c) {
        if (c == null) return null;
        return ShowDocument.Country.builder()
                .name(c.getName()).code(c.getCode()).timezone(c.getTimezone()).build();
    }

    private ShowResponse.CountryResponse mapCountryFromDoc(ShowDocument.Country c) {
        if (c == null) return null;
        return ShowResponse.CountryResponse.builder()
                .name(c.getName()).code(c.getCode()).timezone(c.getTimezone()).build();
    }

    private ShowDocument.Externals mapExternalsToDoc(ShowResponse.ExternalsResponse e) {
        if (e == null) return null;
        return ShowDocument.Externals.builder()
                .tvrage(e.getTvrage()).thetvdb(e.getThetvdb()).imdb(e.getImdb()).build();
    }

    private ShowResponse.ExternalsResponse mapExternalsFromDoc(ShowDocument.Externals e) {
        if (e == null) return null;
        return ShowResponse.ExternalsResponse.builder()
                .tvrage(e.getTvrage()).thetvdb(e.getThetvdb()).imdb(e.getImdb()).build();
    }

    private ShowDocument.Image mapImageToDoc(ShowResponse.ImageResponse i) {
        if (i == null) return null;
        return ShowDocument.Image.builder().medium(i.getMedium()).original(i.getOriginal()).build();
    }

    private ShowResponse.ImageResponse mapImageFromDoc(ShowDocument.Image i) {
        if (i == null) return null;
        return ShowResponse.ImageResponse.builder().medium(i.getMedium()).original(i.getOriginal()).build();
    }

    private ShowDocument.Links mapLinksToDoc(ShowResponse.LinksResponse l) {
        if (l == null) return null;
        return ShowDocument.Links.builder()
                .self(mapLinkToDoc(l.getSelf()))
                .previousepisode(mapLinkToDoc(l.getPreviousepisode()))
                .nextepisode(mapLinkToDoc(l.getNextepisode()))
                .build();
    }

    private ShowResponse.LinksResponse mapLinksFromDoc(ShowDocument.Links l) {
        if (l == null) return null;
        return ShowResponse.LinksResponse.builder()
                .self(mapLinkFromDoc(l.getSelf()))
                .previousepisode(mapLinkFromDoc(l.getPreviousepisode()))
                .nextepisode(mapLinkFromDoc(l.getNextepisode()))
                .build();
    }

    private ShowDocument.Link mapLinkToDoc(ShowResponse.LinkResponse l) {
        if (l == null) return null;
        return ShowDocument.Link.builder().href(l.getHref()).name(l.getName()).build();
    }

    private ShowResponse.LinkResponse mapLinkFromDoc(ShowDocument.Link l) {
        if (l == null) return null;
        return ShowResponse.LinkResponse.builder().href(l.getHref()).name(l.getName()).build();
    }
}