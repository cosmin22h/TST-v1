package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.constants.ListType;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsDetailsDto;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsDto;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsIdsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ListTvShowsService {
    List<ListType> findTvShowInList(ListTvShowsIdsDto tvShowsToFind) throws ApiExceptionResponse;
    List<ListTvShowsDetailsDto> findTvShowsInListForUser(ListTvShowsIdsDto tvShowsToFind, boolean firstFour) throws ApiExceptionResponse;
    ListTvShowsDto addTvShowToList(ListTvShowsIdsDto newTvShowToList) throws ApiExceptionResponse;
    ListTvShowsDto removeTvShowFromList(ListTvShowsIdsDto tvShowToRemove) throws ApiExceptionResponse;
    ListTvShowsDto addTvShowToRecentlyWatched(ListTvShowsIdsDto newTvShowToList) throws ApiExceptionResponse;
}
