package com.PSproject.TvShowsTracker.mapper.user;

import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.model.user.MyAdmin;

public class MyAdminMapper {
    public static MyAdmin mapDtoToModel(UserDto userDto) {
        MyAdmin admin = new MyAdmin();
        if (userDto.getId() != null) admin.setId(userDto.getId());
        admin.setUsername(userDto.getUsername());
        admin.setEmail(userDto.getEmail());
        admin.setDateJoined(userDto.getDateJoined());
        admin.setRole(userDto.getRole());
        admin.setIsActive(userDto.getIsActive());
        admin.setPassword(userDto.getPassword());

        return admin;
    }
}
