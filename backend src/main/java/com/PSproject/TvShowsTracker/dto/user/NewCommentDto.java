package com.PSproject.TvShowsTracker.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewCommentDto {

    @Size(min = 3, max = 1000, message = "Content must be between {min} and {max} characters")
    private String content;

    @NotNull
    private Long idEpisode;

    @NotNull
    private Long idUser;
}
