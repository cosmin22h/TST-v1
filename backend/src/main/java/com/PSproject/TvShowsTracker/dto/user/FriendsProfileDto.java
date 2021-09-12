package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FriendsProfileDto {
    private MyUserProfileDto friend;
    private Boolean isAccepted;
}
