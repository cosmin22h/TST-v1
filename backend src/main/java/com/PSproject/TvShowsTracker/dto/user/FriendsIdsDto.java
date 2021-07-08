package com.PSproject.TvShowsTracker.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FriendsIdsDto {
    private Long id;

    private Boolean isAccepted;

    @NotNull
    private Long idFriend;

    @NotNull
    private Long idUser;
}
