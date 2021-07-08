package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDetailsDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
public interface MyUserService {
    MyUserDto findById(Long id) throws ApiExceptionResponse;
    MyUserDto findByUsername(Long id, String username) throws ApiExceptionResponse;
    MyUserDto addUser(MyUserDto newUserDto) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException;
    MyUserDto editUser(Long id, MyUserDetailsDto userToEdit) throws ApiExceptionResponse;
    MyUserDto updateAvatar(Long id, MultipartFile avatar) throws ApiExceptionResponse;
}
