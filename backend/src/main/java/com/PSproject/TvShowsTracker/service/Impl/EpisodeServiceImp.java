package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.dto.tvshow.EpisodeDto;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.tvshow.EpisodeMapper;
import com.PSproject.TvShowsTracker.mapper.tvshow.TvShowMapper;
import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import com.PSproject.TvShowsTracker.repository.EpisodeRepository;
import com.PSproject.TvShowsTracker.repository.TvShowRepository;
import com.PSproject.TvShowsTracker.service.EpisodeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EpisodeServiceImp implements EpisodeService {

    private final TvShowRepository tvShowRepository;
    private final EpisodeRepository episodeRepository;

    public EpisodeServiceImp(TvShowRepository tvShowRepository, EpisodeRepository episodeRepository) {
        this.tvShowRepository = tvShowRepository;
        this.episodeRepository = episodeRepository;
    }

    @Override
    @Transactional
    public List<EpisodeDto> findAllByTvShow(TvShowDto tvShowDto) throws ApiExceptionResponse {
        List<EpisodeDto> episodes = new ArrayList<>();
        TvShow tvShow = TvShowMapper.mapDtoToModel(tvShowDto);
        episodeRepository.findAllByTvShow(tvShow).forEach(episode -> episodes.add(EpisodeMapper.mapModelToDto(episode, true)));
        List<EpisodeDto> sortedEpisodes = episodes.stream().sorted(Comparator.comparing(EpisodeDto::getNoSeason).thenComparing(EpisodeDto::getEpisodeNumber)).collect(Collectors.toList());
        if (sortedEpisodes.size() == 0) throw ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("No episodes for " + tvShowDto.getName())
                .errors(Collections.singletonList("error.episodes.not_found"))
                .build();

        return sortedEpisodes;
    }

    @Override
    @Transactional
    public EpisodeDto findById(Long id) throws ApiExceptionResponse {
        try {
            return EpisodeMapper.mapModelToDto(episodeRepository.findById(id).orElseThrow(), false);
        } catch (NoSuchElementException e) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.episode.not_found");
            errors.add("error.id_episode.id_not_found");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Episode not found")
                    .errors(errors)
                    .build();
        }
    }

    @Override
    @Transactional
    public EpisodeDto addEpisode(TvShowDto tvShowDto, EpisodeDto newEpisodeDto) throws ApiExceptionResponse {
        TvShow tvShow = TvShowMapper.mapDtoToModel(tvShowDto);
        Episode newEpisode = EpisodeMapper.mapDtoToModel(newEpisodeDto);
        newEpisode.setRating(0.0F);
        newEpisode.setTvShow(tvShow);
        return EpisodeMapper.mapModelToDto(episodeRepository.save(newEpisode), false);
    }

    @Override
    @Transactional
    public EpisodeDto editEpisode(Long id, EpisodeDto updateEpisode) throws ApiExceptionResponse {
        try {
            Episode episodeToEdit = episodeRepository.findById(id).orElseThrow();
            TvShow tvShow = episodeToEdit.getTvShow();
            episodeToEdit = EpisodeMapper.mapDtoToModel(updateEpisode);
            episodeToEdit.setTvShow(tvShow);
            return EpisodeMapper.mapModelToDto(episodeRepository.save(episodeToEdit), false);
        } catch (NoSuchElementException e) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.episode.not_found");
            errors.add("error.id_episode.id_not_found");
            errors.add("error.edit_episode.cant_edit_episode");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Episode not found")
                    .errors(errors)
                    .build();
        }

    }

    @Override
    @Transactional
    public EpisodeDto deleteEpisode(Long id) throws ApiExceptionResponse {
        try {
            Episode episodeToDelete = episodeRepository.findById(id).orElseThrow();
            episodeToDelete.getTvShow().getEpisodes().remove(episodeToDelete);
            episodeRepository.delete(episodeToDelete);
            return EpisodeMapper.mapModelToDto(episodeToDelete, false);
        } catch (NoSuchElementException e) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.episode.not_found");
            errors.add("error.id_episode.id_not_found");
            errors.add("error.delete_episode.cant_delete_episode");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Episode not found")
                    .errors(errors)
                    .build();
        }
    }

}
