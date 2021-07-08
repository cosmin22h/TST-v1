package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.user.ProfileMetricsDto;
import com.PSproject.TvShowsTracker.dto.user.UserEpisodeDto;
import com.PSproject.TvShowsTracker.dto.user.UserEpisodeIdsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserEpisodeService {
    List<UserEpisodeDto> findAllViewedEpisodesByTvShow(Long idTvShow, Long idUser) throws ApiExceptionResponse;
    UserEpisodeDto addViewedEpisode(UserEpisodeIdsDto episodeViewed) throws ApiExceptionResponse;
    UserEpisodeDto removeViewedEpisode(UserEpisodeIdsDto episodeViewed) throws ApiExceptionResponse;
    UserEpisodeDto rateEpisode(UserEpisodeIdsDto ratedEpisode) throws ApiExceptionResponse;
    ProfileMetricsDto getProfileMetrics(Long idUser) throws ApiExceptionResponse;
}
