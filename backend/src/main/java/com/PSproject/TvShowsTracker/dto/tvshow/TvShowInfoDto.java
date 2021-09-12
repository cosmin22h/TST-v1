package com.PSproject.TvShowsTracker.dto.tvshow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TvShowInfoDto {
    private Long id;

    private Long tmdbId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    List<@NotNull @NotEmpty(message = "Must be at least one genre") String> genres = new ArrayList<String>();

    @Pattern(regexp = "^(Returning Series|Ended)$", message = "Invalid status")
    private String status;

    @NotBlank(message = "Poster path is mandatory")
    private String posterPath;

    @NotNull
    @Min(value = 1, message = "Number of seasons must be at least {value}")
    private Integer noOfSeasons;

    @NotNull
    @Min(value = 1, message = "Number of episodes must be at least {value}")
    private Integer noOfEpisodes;

    @Size(max = 2000, message = "Overview must have maximum {max} characters")
    private String overview;
}
