package com.PSproject.TvShowsTracker.model.tvshow;

import com.PSproject.TvShowsTracker.model.Comment;
import com.PSproject.TvShowsTracker.model.user.UserEpisode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Number of season is mandatory")
    @Min(value = 1, message = "Number of season must be at least {value}")
    private Integer noSeason;

    @NotNull(message = "Number of episode is mandatory")
    @Min(value = 0, message = "Number of season must be at least {value}")
    private Integer episodeNumber;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date airDate;

    @Lob
    @Column(length=2000)
    @Size(max = 2000, message = "Overview must have maximum {max} characters")
    private String overview;

    @Min(value = 0, message = "Rating must be at least {value}")
    @Max(value = 5, message = "Rating must be maximum {value}")
    private Float rating;

    private String stillPath;

    @NotNull(message = "TV show is mandatory")
    @ManyToOne
    @JoinColumn(name = "tvshow_id")
    @JsonIgnore
    private TvShow tvShow;

    @OneToMany(targetEntity = UserEpisode.class, mappedBy = "episode", cascade = CascadeType.ALL)
    private List<UserEpisode> users = new ArrayList<>();

    @OneToMany(mappedBy = "episode", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

}
