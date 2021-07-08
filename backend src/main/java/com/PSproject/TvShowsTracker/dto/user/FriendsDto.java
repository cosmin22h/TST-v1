package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.model.user.MyUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FriendsDto {

    private Long id;
    Boolean isAccepted;
    private MyUser friend;
    private MyUser myUser;
}
