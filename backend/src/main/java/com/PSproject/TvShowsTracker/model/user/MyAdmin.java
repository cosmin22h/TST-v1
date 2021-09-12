package com.PSproject.TvShowsTracker.model.user;

import com.PSproject.TvShowsTracker.constants.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class MyAdmin extends BasicUser {

    public MyAdmin(Long id, String username, String email, Boolean isActive, Date dateJoined, String password, AuthSession authSession) {
        super(id, username, email, isActive, Role.ADMIN, dateJoined, password, authSession);
    }
}
