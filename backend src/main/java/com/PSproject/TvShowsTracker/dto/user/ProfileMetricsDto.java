package com.PSproject.TvShowsTracker.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class ProfileMetricsDto {
    private Integer months;
    private Integer days;
    private Integer hours;
    private Integer episodesWatched;
}
