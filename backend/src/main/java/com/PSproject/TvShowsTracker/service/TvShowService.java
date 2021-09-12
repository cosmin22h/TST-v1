package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowInfoDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TvShowService {
    List<TvShowDto> fetchAll() throws ApiExceptionResponse;
    TvShowDto findTvShowByTMDBid(Long id) throws ApiExceptionResponse;
    TvShowDto findTvShowById(Long id) throws ApiExceptionResponse;
    TvShowDto addTvShow(TvShowDto newTvShowDto) throws ApiExceptionResponse;
    TvShowDto editTvShow(Long id, TvShowDto updateTvShowDto) throws ApiExceptionResponse;
    TvShowDto deleteTvShow(Long id) throws ApiExceptionResponse;
    List<TvShowInfoDto> fetchAllPosters() throws ApiExceptionResponse;
    List<TvShowInfoDto> fetchTvShowByMatch(String term) throws ApiExceptionResponse;
}
