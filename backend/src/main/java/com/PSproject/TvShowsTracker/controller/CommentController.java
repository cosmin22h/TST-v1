package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.constants.Role;
import com.PSproject.TvShowsTracker.dto.user.CommentDto;
import com.PSproject.TvShowsTracker.dto.user.NewCommentDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.CommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin
@RequestMapping("/TST/comment")
public class CommentController {

    private final CommentService commentService;
    private final SimpMessagingTemplate messagingTemplate;

    public CommentController(CommentService commentService, SimpMessagingTemplate messagingTemplate) {
        this.commentService = commentService;
        this.messagingTemplate = messagingTemplate;
    }

    @ApiOperation(value = "Post a comment")
    @PostMapping("/post")
    public ResponseEntity postComment(@ApiParam(value = "Requires a comment body") @RequestBody @Valid NewCommentDto newComment) throws ApiExceptionResponse {
        CommentDto comment = commentService.addComment(newComment);
        String message = "New comment from " + comment.getUser().getDisplayName() + ". Check: " + comment.getEpisode().getTvShow().getName() + " (TMDB id: " + comment.getEpisode().getTvShow().getTmdbId() + ")" + ", season - " + comment.getEpisode().getNoSeason() + ", no - " + comment.getEpisode().getEpisodeNumber();
        messagingTemplate.convertAndSend("/topic/socket/user/comments", message);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return all comments for an episode")
    @GetMapping("/all-comments/{id}/{roleUser}")
    public ResponseEntity fetchAllComments(@ApiParam(value = "Requires an episode id") @PathVariable Long id, @ApiParam(value = "Requires a role")@PathVariable Role roleUser) throws ApiExceptionResponse {
        boolean sortByLikes = false;
        if (roleUser.equals(Role.USER)) {
            sortByLikes = true;
        }
        return ResponseEntity.ok().body(commentService.fetchAllByEpisode(id, sortByLikes));
    }

    @ApiOperation(value = "Remove a comment")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity removeComment(@ApiParam(value = "Requires a comment id") @PathVariable Long id) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok().body(commentService.sentEmailRemoveComment(commentService.removeComment(id)));
    }

    @ApiOperation(value = "Mark a comment as spoiler/not spoiler")
    @PutMapping("/edit/{id}")
    public ResponseEntity editComment(@ApiParam(value = "Requires a comment id") @PathVariable Long id, @ApiParam(value = "Requires an updated comment body")@RequestBody CommentDto commentDto) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException {
        commentService.editComment(id, commentDto.getIsSpoiler());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Like a comment")
    @PostMapping("/{idComment}/like-comment/{idUser}")
    public ResponseEntity likeComment(@ApiParam(value = "Requires a comment id") @PathVariable Long idUser, @ApiParam(value = "Requires an user id")@PathVariable Long idComment) throws ApiExceptionResponse {
        commentService.likeComment(idUser, idComment);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Unlike a comment")
    @DeleteMapping("/{idComment}/remove-like/{idUser}")
    public ResponseEntity removeLikedComment(@ApiParam(value = "Requires a comment id")@PathVariable Long idUser,  @ApiParam(value = "Requires an user id")@PathVariable Long idComment) throws ApiExceptionResponse {
        commentService.removeLike(idUser, idComment);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return all liked comments for a user")
    @GetMapping("/all-liked-comments/{idUser}")
    public ResponseEntity fetchAllLikedComment(@ApiParam(value = "Requires an user id") @PathVariable Long idUser) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(commentService.fetchAllLikedComments(idUser));
    }

    @ApiOperation(value = "Return a comment")
    @GetMapping("/{id}")
    public ResponseEntity getComment(@ApiParam(value = "Requires a comment id") @PathVariable Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(commentService.getComment(id));
    }

}
