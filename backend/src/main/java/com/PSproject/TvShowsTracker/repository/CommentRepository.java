package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.Comment;
import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAllByEpisode(Episode e);
    void deleteById(Long id);
}
