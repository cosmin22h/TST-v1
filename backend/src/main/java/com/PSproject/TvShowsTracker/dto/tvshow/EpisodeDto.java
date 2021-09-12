package com.PSproject.TvShowsTracker.dto.tvshow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EpisodeDto {

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

    @Size(max = 2000, message = "Overview must have maximum {max} characters")
    private String overview;

    @Min(value = 0, message = "Rating must be at least {value}")
    @Max(value = 5, message = "Rating must be maximum {value}")
    private Float rating;

    private String stillPath;
    private TvShowInfoDto tvShow;
}
