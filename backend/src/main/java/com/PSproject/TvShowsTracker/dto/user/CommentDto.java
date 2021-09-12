package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.dto.tvshow.EpisodeDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserProfileDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentDto {
    private Long id;
    private MyUserProfileDto user;
    private Long likes;
    private String content;
    private Boolean isSpoiler;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date postDate;
    private EpisodeDto episode;
}
