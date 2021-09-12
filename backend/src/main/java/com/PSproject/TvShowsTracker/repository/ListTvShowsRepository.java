package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.user.ListTvShows;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListTvShowsRepository extends CrudRepository<ListTvShows, Long> {
    List<ListTvShows> findAllByMyUser(MyUser user);
}
