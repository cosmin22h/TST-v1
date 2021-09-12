package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.dto.user.ProfileMetricsDto;
import com.PSproject.TvShowsTracker.dto.user.UserEpisodeDto;
import com.PSproject.TvShowsTracker.dto.user.UserEpisodeIdsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.UserEpisodeMapper;
import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.model.user.UserEpisode;
import com.PSproject.TvShowsTracker.repository.EpisodeRepository;
import com.PSproject.TvShowsTracker.repository.MyUserRepository;
import com.PSproject.TvShowsTracker.repository.TvShowRepository;
import com.PSproject.TvShowsTracker.repository.UserEpisodeRepository;
import com.PSproject.TvShowsTracker.service.UserEpisodeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserEpisodeServiceImpl implements UserEpisodeService {

    private final UserEpisodeRepository userEpisodeRepository;
    private final MyUserRepository myUserRepository;
    private final EpisodeRepository episodeRepository;

    public UserEpisodeServiceImpl(UserEpisodeRepository userEpisodeRepository, MyUserRepository myUserRepository, EpisodeRepository episodeRepository, TvShowRepository tvShowRepository) {
        this.userEpisodeRepository = userEpisodeRepository;
        this.myUserRepository = myUserRepository;
        this.episodeRepository = episodeRepository;
    }

    @Override
    @Transactional
    public List<UserEpisodeDto> findAllViewedEpisodesByTvShow(Long idTvShow, Long idUser) {
        List<UserEpisode> allEpisodeViewed = new ArrayList<>();
        userEpisodeRepository.findAll().forEach(allEpisodeViewed::add);
        List<UserEpisodeDto> episodesViewedByTvShow = allEpisodeViewed.stream()
                .filter(ue -> ue.getMyUser().getId().equals(idUser) && ue.getEpisode().getTvShow().getId().equals(idTvShow))
                .map(ep -> UserEpisodeMapper.mapModelToDto(ep))
                .collect(Collectors.toList());


         return episodesViewedByTvShow;
    }

    @Override
    @Transactional
    public UserEpisodeDto addViewedEpisode(UserEpisodeIdsDto episodeViewed) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(episodeViewed.getIdUser()).orElseThrow();
            Episode episode = episodeRepository.findById(episodeViewed.getIdEpisode()).orElseThrow();
            UserEpisode userEpisode = UserEpisode
                    .builder()
                    .episode(episode)
                    .rating(episodeViewed.getRating())
                    .myUser(user)
                    .build();
            return UserEpisodeMapper.mapModelToDto(userEpisodeRepository.save(userEpisode));
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't viewed the episode")
                    .errors(Collections.singletonList("error.add_viewed_episode.no_user_or_no_episode"))
                    .build();
        }
    }

    @Override
    @Transactional
    public UserEpisodeDto removeViewedEpisode(UserEpisodeIdsDto episodeViewed) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(episodeViewed.getIdUser()).orElseThrow();

            UserEpisode episodeViewedToDelete = null;
            for (UserEpisode e: userEpisodeRepository.findAllByMyUser(user)) {
                if (e.getEpisode().getId().equals(episodeViewed.getIdEpisode())) {
                    episodeViewedToDelete = e;
                    break;
                }
            }

            userEpisodeRepository.delete(episodeViewedToDelete);
            // remove rating
            if (!episodeViewedToDelete.getRating().equals(0.0f)) {
                Episode episode = episodeViewedToDelete.getEpisode();
                List<UserEpisode> episodes = userEpisodeRepository.findAllByEpisode(episode);
                Integer noOfEpisodesRated = episodes.stream()
                        .filter(e -> e.getRating() != 0)
                        .collect(Collectors.toList()).size();
                Float newRating = (episode.getRating() * (noOfEpisodesRated + 1) - episodeViewedToDelete.getRating()) / noOfEpisodesRated;
                if (noOfEpisodesRated.equals(0)) {
                    episode.setRating(0.0f);
                } else {
                    episode.setRating(newRating);
                }

                episodeRepository.save(episode);
                // update tv show rating
                Float ratingTvShow = 0.0f;
                Integer epRated = 0;
                TvShow tvShow = episode.getTvShow();
                for (Episode e : tvShow.getEpisodes()) {
                    if (e.getRating() != 0) {
                        ratingTvShow+= e.getRating();
                        epRated++;
                    }
                }
                if (epRated == 0) {
                    tvShow.setRating(0.0f);
                } else {
                    tvShow.setRating(ratingTvShow / epRated);
                }
            }
            return UserEpisodeMapper.mapModelToDto(episodeViewedToDelete);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't remove the episode")
                    .errors(Collections.singletonList("error.remove_viewed_episode.no_user_or_no_episode"))
                    .build();
        }
    }

    @Override
    @Transactional
    public UserEpisodeDto rateEpisode(UserEpisodeIdsDto ratedEpisode) throws ApiExceptionResponse {
        try {
            Episode episode = episodeRepository.findById(ratedEpisode.getIdEpisode()).orElseThrow();
            List<UserEpisode> episodes = userEpisodeRepository.findAllByEpisode(episode);

            UserEpisode episodeToBeRated = null;
            for (UserEpisode e: episodes) {
                if(e.getMyUser().getId().equals(ratedEpisode.getIdUser())) {
                    episodeToBeRated = e;
                    break;
                }
            }
            Integer noOfEpisodesRated = episodes.stream()
                    .filter(e -> e.getRating() != 0)
                    .collect(Collectors.toList()).size() + 1;

            Float oldRating = episodeToBeRated.getRating();
            if (oldRating != 0.0f) {
                noOfEpisodesRated--;
                episode.setRating(episode.getRating() * noOfEpisodesRated - oldRating);
            }
            // rating user
            episodeToBeRated.setRating(ratedEpisode.getRating());
            UserEpisode userEpisode = userEpisodeRepository.save(episodeToBeRated);

            // update episode rating
            Float oldRatingEpisode = episode.getRating();
            episode.setRating((oldRatingEpisode + ratedEpisode.getRating()) / noOfEpisodesRated);
            episodeRepository.save(episode);

            // update tv show rating
            Float ratingTvShow = 0.0f;
            Integer epRated = 0;
            TvShow tvShow = episode.getTvShow();
            for (Episode e : tvShow.getEpisodes()) {
                if (e.getRating() != 0) {
                    ratingTvShow+= e.getRating();
                    epRated++;
                }
            }

            tvShow.setRating(ratingTvShow / epRated);

            return UserEpisodeMapper.mapModelToDto(userEpisode);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't rate the episode")
                    .errors(Collections.singletonList("error.add_rating_episode.no_episode"))
                    .build();
        }
    }

    @Override
    @Transactional
    public ProfileMetricsDto getProfileMetrics(Long idUser) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(idUser).orElseThrow();

            List<UserEpisode> episodes = userEpisodeRepository.findAllByMyUser(user);

            Integer tvTime = 0;
            for (UserEpisode e: episodes) {
                tvTime+= e.getEpisode().getTvShow().getEpisodeRunTime();
            }
            Integer months = 0;
            Integer days = 0;
            Integer hours;

            hours = tvTime / 60;
            while (hours > 23) {
                hours-=24;
                days++;
            }
            while (days > 30) {
                days-=31;
                months++;
            }
            return ProfileMetricsDto.builder()
                    .months(months)
                    .days(days)
                    .hours(hours)
                    .episodesWatched(episodes.size()).build();
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't remove the episode")
                    .errors(Collections.singletonList("error.remove_viewed_episode.no_user_or_no_episode"))
                    .build();
        }
    }
}

