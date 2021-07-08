package com.PSproject.TvShowsTracker.dto.tvshow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TvShowDetailsDto {
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "TMDB ID is mandatory")
    private Long tmdbId;

    @NotNull
    @Min(value = 1, message = "Number of seasons must be at least {value}")
    private Integer noOfSeasons;

    @NotNull
    @Min(value = 1, message = "Number of episodes must be at least {value}")
    private Integer noOfEpisodes;

    @Pattern(regexp = "^(Returning Series|Ended)$", message = "Invalid status")
    private String status;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date nextAirDate;
}
