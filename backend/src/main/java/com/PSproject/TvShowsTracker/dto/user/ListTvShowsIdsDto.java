package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.constants.ListType;
import com.PSproject.TvShowsTracker.validators.ListTypeSubset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class ListTvShowsIdsDto {

    @ListTypeSubset(anyOf = {ListType.FAVORITES, ListType.RECENTLY_WATCHED, ListType.TO_WATCH, ListType.STOPPED, ListType.UP_TO_DATE})
    private ListType type;
    private Long idUser;
    private Long idTvShow;
}
