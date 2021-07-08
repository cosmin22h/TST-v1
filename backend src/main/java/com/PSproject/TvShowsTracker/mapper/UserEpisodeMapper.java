package com.PSproject.TvShowsTracker.mapper;

import com.PSproject.TvShowsTracker.dto.user.UserEpisodeDto;
import com.PSproject.TvShowsTracker.model.user.UserEpisode;

public class UserEpisodeMapper {

    public static UserEpisodeDto mapModelToDto(UserEpisode userEpisode) {
        return UserEpisodeDto.builder()
                .episodeId(userEpisode.getEpisode().getId())
                .rating(userEpisode.getRating())
                .build();
    }
}
