package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    List<Episode> findAllByTvShow(TvShow tvShow);
}
