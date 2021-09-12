package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.user.AuthSession;
import org.springframework.data.repository.CrudRepository;

public interface AuthSessionRepository extends CrudRepository<AuthSession, Long> {
}
