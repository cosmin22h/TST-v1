package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.user.FriendsDto;
import com.PSproject.TvShowsTracker.dto.user.FriendsIdsDto;
import com.PSproject.TvShowsTracker.dto.user.FriendsProfileDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FriendsService {
    FriendsIdsDto sendRequest(FriendsIdsDto request) throws ApiExceptionResponse;
    FriendsDto acceptRequest(FriendsIdsDto request) throws ApiExceptionResponse;
    FriendsIdsDto rejectRequest(Long idUser, Long idFriend) throws ApiExceptionResponse;
    FriendsIdsDto unfriend(Long idUser, Long idFriend) throws ApiExceptionResponse;
    FriendsIdsDto findFriend(Long idUser, Long idFriend) throws ApiExceptionResponse;
    List<FriendsProfileDto> findAllFriends(Long idUser) throws ApiExceptionResponse;
    List<FriendsProfileDto> findAllRequest(Long idUser) throws ApiExceptionResponse;
}
