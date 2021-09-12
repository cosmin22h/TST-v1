package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.constants.ListType;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ListTvShowsDto {
    private ListType type;
    private TvShowDto tvShowDto;
    private MyUserDto user;
}
