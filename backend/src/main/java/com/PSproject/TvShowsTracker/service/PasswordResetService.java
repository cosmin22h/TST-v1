package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.user.UserCredentialsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@Component
public interface PasswordResetService {
    void createPasswordResetToken(String userEmail) throws MessagingException, UnsupportedEncodingException;
    String validatePasswordResetToken(String token) throws ApiExceptionResponse, ParseException;
    void setNewPassword(UserCredentialsDto userCredentialsDto);
}
