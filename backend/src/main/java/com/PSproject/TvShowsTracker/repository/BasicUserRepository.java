package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.user.BasicUser;
import org.springframework.data.repository.CrudRepository;

public interface BasicUserRepository extends CrudRepository<BasicUser, Long> {
    BasicUser findByUsername(String username);
    BasicUser findByEmail(String email);

}
