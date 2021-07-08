package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.tvshow.EpisodeDto;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EpisodeService {
    List<EpisodeDto> findAllByTvShow(TvShowDto tvShowDto) throws ApiExceptionResponse;
    EpisodeDto findById(Long id) throws ApiExceptionResponse;
    EpisodeDto addEpisode(TvShowDto tvShowDto, EpisodeDto newEpisodeDto) throws ApiExceptionResponse;
    EpisodeDto editEpisode(Long id, EpisodeDto updateEpisode) throws ApiExceptionResponse;
    EpisodeDto deleteEpisode(Long id) throws ApiExceptionResponse;
}
