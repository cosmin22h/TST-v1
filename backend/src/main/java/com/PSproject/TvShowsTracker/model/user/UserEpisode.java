package com.PSproject.TvShowsTracker.model.user;

import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserEpisode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Float rating;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private MyUser myUser;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "episode_id")
    @JsonIgnore
    private Episode episode;
}
