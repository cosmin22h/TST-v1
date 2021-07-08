package com.PSproject.TvShowsTracker.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserEpisodeIdsDto {

    @NotNull
    private Long idUser;

    @NotNull
    private Long idEpisode;

    private Float rating;
}
