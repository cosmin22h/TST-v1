package com.PSproject.TvShowsTracker.model.user;

import com.PSproject.TvShowsTracker.constants.ListType;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import com.PSproject.TvShowsTracker.validators.ListTypeSubset;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ListTvShows {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ListTypeSubset(anyOf = {ListType.FAVORITES, ListType.RECENTLY_WATCHED, ListType.TO_WATCH, ListType.STOPPED, ListType.UP_TO_DATE})
    private ListType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private MyUser myUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tvshow_id")
    @JsonIgnore
    private TvShow tvShowAdded;

}
