package com.PSproject.TvShowsTracker.mapper.tvshow;

import com.PSproject.TvShowsTracker.dto.tvshow.*;
import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;

import java.util.*;
import java.util.stream.Collectors;

public class TvShowMapper {
    public static TvShowDto mapModelToDto(TvShow tvShow) {
        TvShowDto tvShowDto = new TvShowDto();
        tvShowDto.setId(tvShow.getId());
        tvShowDto.setTmdbId(tvShow.getTmdbId());
        tvShowDto.setName(tvShow.getName());
        tvShowDto.setGenres(tvShow.getGenres());
        tvShowDto.setPosterPath(tvShow.getPosterPath());
        tvShowDto.setNoOfSeasons(tvShow.getNoOfSeasons());
        tvShowDto.setNoOfEpisodes(tvShow.getNoOfEpisodes());
        tvShowDto.setStatus(tvShow.getStatus());
        tvShowDto.setOverview(tvShow.getOverview());
        tvShowDto.setNetworks(tvShow.getNetworks());
        tvShowDto.setEpisodeRunTime(tvShow.getEpisodeRunTime());
        tvShowDto.setFirstAirDate(tvShow.getFirstAirDate());
        tvShowDto.setLastAirDate(tvShow.getLastAirDate());
        tvShowDto.setNextAirDate(tvShow.getNextAirDate());
        tvShowDto.setRating(tvShow.getRating());
        Set<EpisodeDto> episodeDtos = new HashSet<>();
        for (Episode e: tvShow.getEpisodes()) {
            episodeDtos.add(EpisodeMapper.mapModelToDto(e, true));
        }
        List<EpisodeDto> sortedEpisodes = episodeDtos.stream().sorted(Comparator.comparing(EpisodeDto::getNoSeason).thenComparing(EpisodeDto::getEpisodeNumber)).collect(Collectors.toList());
        tvShowDto.setEpisodes(sortedEpisodes);

        return tvShowDto;
    }

    public static TvShow mapDtoToModel(TvShowDto tvShowDto) {
        TvShow tvShow = new TvShow();
        if(tvShowDto.getId() != null) tvShow.setId(tvShowDto.getId());
        tvShow.setTmdbId(tvShowDto.getTmdbId());
        tvShow.setName(tvShowDto.getName());
        tvShow.setGenres(tvShowDto.getGenres());
        tvShow.setPosterPath(tvShowDto.getPosterPath());
        tvShow.setNoOfSeasons(tvShowDto.getNoOfSeasons());
        tvShow.setNoOfEpisodes(tvShowDto.getNoOfEpisodes());
        tvShow.setStatus(tvShowDto.getStatus());
        tvShow.setOverview(tvShowDto.getOverview());
        tvShow.setNetworks(tvShowDto.getNetworks());
        tvShow.setEpisodeRunTime(tvShowDto.getEpisodeRunTime());
        tvShow.setFirstAirDate(tvShowDto.getFirstAirDate());
        tvShow.setLastAirDate(tvShowDto.getLastAirDate());
        tvShow.setNextAirDate(tvShowDto.getNextAirDate());
        tvShow.setRating(tvShowDto.getRating());

        return tvShow;
    }

    public static TvShowDetailsDto mapDtoToDetailsDto(TvShowDto tvShowDto) {
        return TvShowDetailsDto.builder()
                .id(tvShowDto.getId())
                .name(tvShowDto.getName())
                .tmdbId(tvShowDto.getTmdbId())
                .noOfSeasons(tvShowDto.getNoOfSeasons())
                .noOfEpisodes(tvShowDto.getNoOfEpisodes())
                .status(tvShowDto.getStatus())
                .nextAirDate(tvShowDto.getNextAirDate())
                .build();
    }

    public static TvShowInfoDto mapModelToInfoDto(TvShow tvShow) {
        return TvShowInfoDto.builder()
                .id(tvShow.getId())
                .name(tvShow.getName())
                .genres(tvShow.getGenres())
                .posterPath(tvShow.getPosterPath())
                .noOfSeasons(tvShow.getNoOfSeasons())
                .noOfEpisodes(tvShow.getNoOfEpisodes())
                .status(tvShow.getStatus())
                .overview(tvShow.getOverview())
                .tmdbId(tvShow.getTmdbId())
                .build();
    }
}
