package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.Comment;
import com.PSproject.TvShowsTracker.model.Report;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long> {
    List<Report> findAllByComment(Comment comment);
}
