package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.user.CommentDto;
import com.PSproject.TvShowsTracker.dto.user.NewCommentDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.model.Comment;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public interface CommentService {
    CommentDto addComment(NewCommentDto newComment) throws ApiExceptionResponse;
    Comment removeComment(Long id) throws ApiExceptionResponse;
    Long sentEmailRemoveComment(Comment commentDeleted) throws MessagingException, UnsupportedEncodingException;
    List<CommentDto> fetchAllByEpisode(Long idEpisode, boolean sortByLikes) throws ApiExceptionResponse;
    CommentDto editComment(Long idComment, Boolean isSpoiler) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException;
    void likeComment(Long idUser, Long idComment) throws ApiExceptionResponse;
    void removeLike(Long idUser, Long idComment) throws ApiExceptionResponse;
    List<CommentDto> fetchAllLikedComments(Long idUser) throws ApiExceptionResponse;
    CommentDto getComment(Long id) throws ApiExceptionResponse;
}
