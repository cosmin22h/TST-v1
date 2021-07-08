package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserLogInDetailsDto {
    private Long id;
    private Role role;
}
