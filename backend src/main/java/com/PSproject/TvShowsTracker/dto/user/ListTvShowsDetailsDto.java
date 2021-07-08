package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.constants.ListType;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ListTvShowsDetailsDto {
    private ListType type;
    private TvShowInfoDto tvShowInfoDto;
}
