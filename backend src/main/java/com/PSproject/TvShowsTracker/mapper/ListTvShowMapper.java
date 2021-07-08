package com.PSproject.TvShowsTracker.mapper;

import com.PSproject.TvShowsTracker.dto.user.ListTvShowsDto;
import com.PSproject.TvShowsTracker.mapper.tvshow.TvShowMapper;
import com.PSproject.TvShowsTracker.mapper.user.MyUserMapper;
import com.PSproject.TvShowsTracker.model.user.ListTvShows;

public class ListTvShowMapper {

    public static ListTvShowsDto modelToDto(ListTvShows listTvShows) {
        return ListTvShowsDto.builder()
                .tvShowDto(TvShowMapper.mapModelToDto(listTvShows.getTvShowAdded()))
                .type(listTvShows.getType())
                .user(MyUserMapper.mapModelToDto(listTvShows.getMyUser()))
                .build();
    }
}
