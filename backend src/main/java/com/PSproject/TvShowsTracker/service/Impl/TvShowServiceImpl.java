package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowInfoDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.tvshow.TvShowMapper;
import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import com.PSproject.TvShowsTracker.repository.TvShowRepository;
import com.PSproject.TvShowsTracker.service.TvShowService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class TvShowServiceImpl implements TvShowService {

    private final TvShowRepository tvShowRepository;

    public TvShowServiceImpl(TvShowRepository tvShowRepository) {
        this.tvShowRepository = tvShowRepository;
    }

    @Override
    @Transactional
    public List<TvShowDto> fetchAll() throws ApiExceptionResponse {
        List<TvShowDto> tvShows = new ArrayList<>();
        tvShowRepository.findAll().forEach((tvShow -> tvShows.add(0, TvShowMapper.mapModelToDto(tvShow))));
        if (tvShows.isEmpty()) throw ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("No tv shows")
                .errors(Collections.singletonList("error.tv_shows.not_found"))
                .build();
        return tvShows;
    }

    @Override
    @Transactional
    public TvShowDto findTvShowByTMDBid(Long id) throws ApiExceptionResponse {
        TvShow getTvShow = tvShowRepository.findByTmdbId(id);
        if (getTvShow == null) throw ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("TvShow not in database")
                .errors(Collections.singletonList("error.tv_show.not_found"))
                .build();
        return TvShowMapper.mapModelToDto(getTvShow);
    }

    @Override
    public TvShowDto findTvShowById(Long id) throws ApiExceptionResponse {
        try {
            return TvShowMapper.mapModelToDto(tvShowRepository.findById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("TvShow not found")
                    .errors(Collections.singletonList("error.tv_show.not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public TvShowDto addTvShow(TvShowDto newTvShowDto) {
        TvShow newTvShow = TvShowMapper.mapDtoToModel(newTvShowDto);
        newTvShow.setRating(0.0F);
        return TvShowMapper.mapModelToDto(tvShowRepository.save(newTvShow));
    }

    @Override
    @Transactional
    public TvShowDto editTvShow(Long id, TvShowDto updateTvShowDto) throws ApiExceptionResponse {
        try {
            TvShow tvShowToEdit = tvShowRepository.findById(id).orElseThrow();
            List<Episode> episodes = tvShowToEdit.getEpisodes();
            tvShowToEdit = TvShowMapper.mapDtoToModel(updateTvShowDto);
            tvShowToEdit.setEpisodes(episodes);

            return TvShowMapper.mapModelToDto(tvShowRepository.save(tvShowToEdit));
        } catch (NoSuchElementException e){
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("TvShow not found")
                    .errors(Collections.singletonList("error.tv_show.not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public TvShowDto deleteTvShow(Long id) throws ApiExceptionResponse {
        try {
            TvShow tvShowToDelete = tvShowRepository.findById(id).orElseThrow();
            tvShowRepository.delete(tvShowToDelete);
            return TvShowMapper.mapModelToDto(tvShowToDelete);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("TvShow not found")
                    .errors(Collections.singletonList("error.tv_show.not_found"))
                    .build();
        }
    }

    @Override
    public List<TvShowInfoDto> fetchAllPosters() throws ApiExceptionResponse {
        List<TvShowInfoDto> posters = new ArrayList<>();
        tvShowRepository.findAll().forEach(tvShow -> posters.add(TvShowInfoDto.builder()
                .id(tvShow.getId())
                .name(tvShow.getName())
                .genres(tvShow.getGenres())
                .posterPath(tvShow.getPosterPath())
                .noOfSeasons(tvShow.getNoOfSeasons())
                .noOfEpisodes(tvShow.getNoOfEpisodes())
                .overview(tvShow.getOverview())
                .build()));
        if (posters.isEmpty()) throw ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("No TV shows")
                .errors(Collections.singletonList("error.tv_show.not_found"))
                .build();
        return posters;
    }

    @Override
    public List<TvShowInfoDto> fetchTvShowByMatch(String term) throws ApiExceptionResponse {
        List<TvShowInfoDto> tvShowMatched = new ArrayList<>();
        List<TvShow> tvShows = new ArrayList<>();
        tvShowRepository.findAll().forEach(tvShows::add);

        Pattern pattern = Pattern.compile(term, Pattern.CASE_INSENSITIVE);
        for (TvShow tvShow: tvShows) {
            if (pattern.matcher(tvShow.getName()).find()) tvShowMatched.add(TvShowInfoDto.builder()
                    .id(tvShow.getId())
                    .name(tvShow.getName())
                    .noOfSeasons(tvShow.getNoOfSeasons())
                    .status(tvShow.getStatus())
                    .posterPath(tvShow.getPosterPath())
                    .build());
        }

        return tvShowMatched;
    }
}
