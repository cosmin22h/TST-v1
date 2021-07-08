package com.PSproject.TvShowsTracker.dto.tvshow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EpisodeDetailsDto {
    private Long id;

    @NotNull(message = "Number of season is mandatory")
    @Min(value = 1, message = "Number of season must be at least {value}")
    private Integer noSeason;

    @NotNull(message = "Number of episode is mandatory")
    @Min(value = 0, message = "Number of season must be at least {value}")
    private Integer episodeNumber;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Air date is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date airDate;
}
