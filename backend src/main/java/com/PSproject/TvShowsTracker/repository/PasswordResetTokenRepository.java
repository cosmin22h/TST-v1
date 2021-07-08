package com.PSproject.TvShowsTracker.repository;

import com.PSproject.TvShowsTracker.model.user.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
