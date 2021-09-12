package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
public interface MyAdminService {
    UserDto addAdmin(UserDto newAdminDto) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException;
}
