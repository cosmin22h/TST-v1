package com.PSproject.TvShowsTracker.dto;

import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PasswordResetTokenDto {
    private Long id;
    private String token;
    private MyUserDto user;
    private Date expiryDate;
}
