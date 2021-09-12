package com.PSproject.TvShowsTracker.mapper.user;

import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.model.user.BasicUser;

public class UserMapper {
    public static UserDto mapModelToDto(BasicUser user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDateJoined(user.getDateJoined());
        userDto.setRole(user.getRole());
        userDto.setIsActive(user.getIsActive());
        userDto.setPassword(user.getPassword());
        userDto.setAuthSession(user.getAuthSession());

        return userDto;
    }
}
