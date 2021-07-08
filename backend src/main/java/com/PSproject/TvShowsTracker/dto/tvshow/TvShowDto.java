package com.PSproject.TvShowsTracker.dto.tvshow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TvShowDto {
    private Long id;

    @NotNull(message = "TMDB ID is mandatory")
    private Long tmdbId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull
    @NotEmpty(message = "Must be at least one genre")
    List<String> genres = new ArrayList<String>();

    @NotBlank(message = "Poster path is mandatory")
    private String posterPath;

    @NotNull
    @Min(value = 1, message = "Number of seasons must be at least {value}")
    private Integer noOfSeasons;

    @NotNull
    @Min(value = 1, message = "Number of episodes must be at least {value}")
    private Integer noOfEpisodes;

    @Pattern(regexp = "^(Returning Series|Ended)$", message = "Invalid status")
    private String status;

    @Size(max = 2000, message = "Overview must have maximum {max} characters")
    private String overview;

    @NotNull
    @NotEmpty(message = "Must be at least one network")
    List<String> networks = new ArrayList<String>();

    @Min(value = 5, message = "Episode run time must be at least {value}")
    private Integer episodeRunTime;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date firstAirDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date lastAirDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date nextAirDate;

    @Min(value = 0, message = "Rating must be at least {value}")
    @Max(value = 5, message = "Rating must be maximum {value}")
    private Float rating;

    private List<EpisodeDto> episodes;
}
