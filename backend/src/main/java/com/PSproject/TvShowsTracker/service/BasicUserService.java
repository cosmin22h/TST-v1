package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.user.UserCredentialsDto;
import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.dto.user.UserLogInDetailsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BasicUserService {
    List<UserDto> fetchAll() throws ApiExceptionResponse;
    UserDto findUserById(Long id) throws ApiExceptionResponse;
    UserDto findUserByUsername(String username);
    UserDto findUserByEmail(String email);
    UserDto editBasicUser(Long id, UserDto updateUserDto) throws ApiExceptionResponse;
    UserDto deleteBasicUser(Long id) throws ApiExceptionResponse;
    UserLogInDetailsDto login(UserCredentialsDto credentialsDto) throws ApiExceptionResponse;
    void logout(Long id) throws ApiExceptionResponse;

}
