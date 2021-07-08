package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.constants.Paths;
import com.PSproject.TvShowsTracker.dto.user.CommentDto;
import com.PSproject.TvShowsTracker.dto.user.NewCommentDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserProfileDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.tvshow.EpisodeMapper;
import com.PSproject.TvShowsTracker.model.Comment;
import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.repository.CommentRepository;
import com.PSproject.TvShowsTracker.repository.EpisodeRepository;
import com.PSproject.TvShowsTracker.repository.MyUserRepository;
import com.PSproject.TvShowsTracker.service.CommentService;
import com.PSproject.TvShowsTracker.utils.email.EmailMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final static String SUBJECT_SPOILER = "Spoiler report";
    private final static String SUBJECT_DELETE = "Comment deleted";

    private final CommentRepository commentRepository;
    private final MyUserRepository myUserRepository;
    private final EpisodeRepository episodeRepository;

    @Autowired
    private JavaMailSender emailSender;

    public CommentServiceImpl(CommentRepository commentRepository, MyUserRepository myUserRepository, EpisodeRepository episodeRepository) {
        this.commentRepository = commentRepository;
        this.myUserRepository = myUserRepository;
        this.episodeRepository = episodeRepository;
    }

    @Override
    @Transactional
    public CommentDto addComment(NewCommentDto newComment) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(newComment.getIdUser()).orElseThrow();
            Episode episode = episodeRepository.findById(newComment.getIdEpisode()).orElseThrow();

            Comment comment = Comment.builder()
                    .content(newComment.getContent())
                    .episode(episode)
                    .likes(0L)
                    .isSpoiler(Boolean.FALSE)
                    .myUser(user)
                    .postDate(new Date())
                    .build();

            Comment commentAdded = commentRepository.save(comment);
            return CommentDto.builder()
                    .user(MyUserProfileDto.builder().displayName(comment.getMyUser().getDisplayName()).build())
                    .content(commentAdded.getContent())
                    .episode(EpisodeMapper.mapModelToDto(commentAdded.getEpisode(), false))
                    .build();
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't post comment")
                    .errors(Collections.singletonList("error.comment.user_or_episode_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public Comment removeComment(Long id) throws ApiExceptionResponse {
        try {
            Comment commentToDelete = commentRepository.findById(id).orElseThrow();
            for (MyUser user: commentToDelete.getUsers()) {
                List<Comment> updateLikedComments = user.getLikedComments();
                updateLikedComments.remove(commentToDelete);
                myUserRepository.save(user);
            }
            commentRepository.delete(commentToDelete);
            return Comment.builder()
                    .id(commentToDelete.getId())
                    .myUser(commentToDelete.getMyUser())
                    .episode(commentToDelete.getEpisode())
                    .content(commentToDelete.getContent())
                    .postDate(commentToDelete.getPostDate())
                    .build();
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't remove comment")
                    .errors(Collections.singletonList("error.comment.comment_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public Long sentEmailRemoveComment(Comment commentDeleted) throws MessagingException, UnsupportedEncodingException {
        MyUser user = commentDeleted.getMyUser();
        Episode episode = commentDeleted.getEpisode();
        String messageHeader, messageBody, commentBody;
        messageHeader = "<h2>Hi, " + user.getUsername() + "!</h2>";
        messageBody = "Your comment at " + episode.getTvShow().getName() + ", season" + episode.getNoSeason()
                + ", no " + episode.getNoSeason() + " has been deleted.<br/>"
                + "If you deleted  it, it's ok, ignore this email. But if didn't, then you are in trouble.<br/>"
                + "Your comment probably was inappropriate or spam or had copyright problem and was removed by an admin."
                + " Please, be more careful. This comments can lead to a ban.<br/>"
                + "Thanks, TST team.";
        commentBody = "<p><b>Post date comment: </b>" + commentDeleted.getPostDate()
                + "<br/><b>Comment: </b>" + commentDeleted.getContent() + "</p>";
        EmailMaker.sendEmail(emailSender, user.getEmail(), messageHeader + messageBody + commentBody, SUBJECT_DELETE);

        return commentDeleted.getId();
    }

    @Override
    @Transactional
    public List<CommentDto> fetchAllByEpisode(Long idEpisode, boolean sortByLikes) throws ApiExceptionResponse {
        try {
            Episode episode = episodeRepository.findById(idEpisode).orElseThrow();
            List<CommentDto> comments = new ArrayList<>();
            for (Comment comment: commentRepository.findAllByEpisode(episode)) {
                MyUser user = comment.getMyUser();
                MyUserProfileDto myUserProfileDto = MyUserProfileDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .displayName(user.getDisplayName())
                        .build();
                CommentDto commentDto = CommentDto.builder()
                        .likes(comment.getLikes())
                        .id(comment.getId())
                        .content(comment.getContent())
                        .isSpoiler(comment.getIsSpoiler())
                        .user(myUserProfileDto)
                        .postDate(comment.getPostDate())
                        .build();
                comments.add(commentDto);
            }

            List<CommentDto> sortedComments;
            if (sortByLikes) {
                sortedComments = comments.stream().sorted(Comparator.comparing(CommentDto::getLikes).thenComparing(CommentDto::getPostDate).reversed()).collect(Collectors.toList());
            } else {
                sortedComments = comments.stream().sorted(Comparator.comparing(CommentDto::getPostDate).reversed()).collect(Collectors.toList());
            }

            return sortedComments;
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't fetch comments")
                    .errors(Collections.singletonList("error.comment.episode_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public CommentDto editComment(Long idComment, Boolean isSpoiler) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException {
        try {
            Comment comment = commentRepository.findById(idComment).orElseThrow();
            comment.setIsSpoiler(isSpoiler);
            commentRepository.save(comment);

            String url = Paths.CONTEXT_PATH + "/episode/" + comment.getEpisode().getId() + "/comments";
            String messageHeader, messageBody, commentBody, commentsLink;
            MyUser user = comment.getMyUser();
            Episode episode = comment.getEpisode();
            if (isSpoiler) {
                messageHeader = "<h2>Hands up, " + user.getUsername() + "!</h2>";
                messageBody = "<p>Your comment has been marked as spoiler."
                        +" Why do you spoil people? Spoilers are bad, Mkay?"
                        + "<br/> Check the comments from " + episode.getTvShow().getName() + ", season " + episode.getNoSeason()
                        + ", no " + episode.getEpisodeNumber() + " to see what have you done."
                        + "<br/>If you think that we are mistaken, feel free to contact us</p>";
            }
            else {
                messageHeader = "<h2>Ups, our bad</h2>";
                messageBody = "<p>Sorry, " + user.getUsername() + ". "
                        + "Your comment at " + episode.getTvShow().getName() + ", season " + episode.getNoSeason()
                        + ", no " + episode.getEpisodeNumber() + " is no longer mark as spoiler."
                        + "<br/>We'll be more careful. That's it a pinky promise!</p>";
            }
            commentBody = "<p><b>Post date comment: </b>" + comment.getPostDate()
                    + "<br/><b>Comment: </b>" + comment.getContent() + "</p>";
            commentsLink = "\n\n<a href=\"" + url + "\"><button>View comments</button></a>";
            EmailMaker.sendEmail(emailSender, user.getEmail(), messageHeader + messageBody + commentBody + commentsLink, SUBJECT_SPOILER);
            return CommentDto.builder().build();
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't edit comment")
                    .errors(Collections.singletonList("error.comment.comment_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public void likeComment(Long idUser, Long idComment) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(idUser).orElseThrow();
            Comment comment = commentRepository.findById(idComment).orElseThrow();
            comment.setLikes(comment.getLikes() + 1);
            commentRepository.save(comment);
            List<Comment> likedList = user.getLikedComments();
            likedList.add(comment);
            user.setLikedComments(likedList);
            myUserRepository.save(user);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't like comment")
                    .errors(Collections.singletonList("error.comment.comment_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public void removeLike(Long idUser, Long idComment) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(idUser).orElseThrow();
            Comment comment = null;
            for (Comment c: user.getLikedComments()) {
                if (c.getId().equals(idComment)) {
                    comment = c;
                    break;
                }
            }
            List<Comment> likedList = user.getLikedComments();
            likedList.remove(comment);
            user.setLikedComments(likedList);
            myUserRepository.save(user);
            comment.setLikes(comment.getLikes() - 1);
            commentRepository.save(comment);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't remove liked comment")
                    .errors(Collections.singletonList("error.comment.comment_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public List<CommentDto> fetchAllLikedComments(Long idUser) throws ApiExceptionResponse {
        MyUser user = myUserRepository.findById(idUser).orElseThrow();
        List<CommentDto> comments = new ArrayList<>();
        for (Comment c: user.getLikedComments()) {
            comments.add(CommentDto.builder()
                    .id(c.getId())
                    .build());
        }
        return comments;
    }

    @Override
    @Transactional
    public CommentDto getComment(Long id) throws ApiExceptionResponse {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow();
            return CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .episode(EpisodeMapper.mapModelToDto(comment.getEpisode(), false))
                    .build();
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't find comment")
                    .errors(Collections.singletonList("error.comment.comment_not_found"))
                    .build();
        }
    }
}
