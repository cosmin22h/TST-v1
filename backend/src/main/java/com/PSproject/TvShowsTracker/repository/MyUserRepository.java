package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.user.MyUser;
import org.springframework.data.repository.CrudRepository;

public interface MyUserRepository extends CrudRepository<MyUser, Long> {
    MyUser findByUsername(String username);
    MyUser findByEmail(String email);
}
