package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import org.springframework.data.repository.CrudRepository;

public interface TvShowRepository extends CrudRepository<TvShow, Long> {
    TvShow findByTmdbId(Long id);
}
