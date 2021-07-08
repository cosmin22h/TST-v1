package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.model.user.UserEpisode;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserEpisodeRepository extends CrudRepository<UserEpisode, Long> {
    List<UserEpisode> findAllByMyUser(MyUser user);
    List<UserEpisode> findAllByEpisode(Episode episode);
}
