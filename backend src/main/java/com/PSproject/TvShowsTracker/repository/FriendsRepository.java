package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.user.Friends;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendsRepository extends CrudRepository<Friends, Long> {
    List<Friends> findAllByMyUser(MyUser user);
    List<Friends> findAllByFriend(MyUser friend);
}
