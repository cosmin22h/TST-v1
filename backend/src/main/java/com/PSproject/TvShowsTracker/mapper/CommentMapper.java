package com.PSproject.TvShowsTracker.mapper;

import com.PSproject.TvShowsTracker.dto.user.CommentDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserProfileDto;
import com.PSproject.TvShowsTracker.mapper.tvshow.EpisodeMapper;
import com.PSproject.TvShowsTracker.model.Comment;

public class CommentMapper {
    public static CommentDto mapModelToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .episode(EpisodeMapper.mapModelToDto(comment.getEpisode(), false))
                .content(comment.getContent())
                .user(MyUserProfileDto.builder().username(comment.getMyUser().getUsername()).id(comment.getMyUser().getId()).build())
                .isSpoiler(comment.getIsSpoiler())
                .postDate(comment.getPostDate())
                .likes(comment.getLikes())
                .build();
    }
}
