package com.PSproject.TvShowsTracker.model.tvshow;

import com.PSproject.TvShowsTracker.model.user.ListTvShows;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TvShow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "TMDB ID is mandatory")
    private Long tmdbId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @ElementCollection
    List<@NotNull @NotEmpty(message = "Must be at least one genre") String> genres = new ArrayList<String>();

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

    @Lob
    @Column(length=2000)
    @Size(max = 2000, message = "Overview must have maximum {max} characters")
    private String overview;

    @ElementCollection
    List<@NotNull @NotEmpty(message = "Must be at least one network") String> networks = new ArrayList<String>();

    @NotNull(message = "Episode run time is mandatory")
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

    @OneToMany(targetEntity = Episode.class, mappedBy = "tvShow", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Episode> episodes = new ArrayList<>();

    @OneToMany(targetEntity = ListTvShows.class, mappedBy = "tvShowAdded", cascade = CascadeType.ALL)
    private List<ListTvShows> lists = new ArrayList<>();
}
