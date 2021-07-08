package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import com.PSproject.TvShowsTracker.repository.TvShowRepository;
import com.PSproject.TvShowsTracker.service.TvShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TvShowServiceImplTest {

/*    private final Long ID = 123456789L;
    private final Long TMDB_ID = 1234567L;
    private final Long NOT_TMDB_ID = 1234560L;
    private final String NAME = "TV Test";

    private TvShowService tvShowService;

    @Mock
    private TvShowRepository tvShowRepository;

    private TvShow tvShow;

    @BeforeEach
    void setUp() {
        initMocks(this);
        tvShow = new TvShow();
        tvShow.setId(ID);
        tvShow.setTmdbId(TMDB_ID);
        tvShow.setName(NAME);
        when(tvShowRepository.findByTmdbId(TMDB_ID)).thenReturn(tvShow);
    }

    @Test
    void givenExistingTmdbId_whenFindByTmdbId_thenFindOne() {
        tvShowService = new TvShowServiceImpl(tvShowRepository);

        TvShow tvShowTemp = tvShowRepository.findByTmdbId(TMDB_ID);

        assertNotNull(tvShowTemp);
        assertEquals(TMDB_ID, tvShowTemp.getTmdbId());
    }

    @Test
    void givenNonExistingTmdbId_whenFindByTmdbId_thenThrowException() {
        assertThrows(NullPointerException.class, () -> {
            tvShowService.findTvShowById(NOT_TMDB_ID);
        });
    }

    @Test
    void createTvShow() {
        tvShowService = new TvShowServiceImpl(tvShowRepository);


        TvShowDto newTvShow = new TvShowDto();
        newTvShow.setName(NAME);
        newTvShow.setTmdbId(TMDB_ID);

        TvShowDto tvAdded = null;
        try {
            tvAdded = tvShowService.addTvShow(newTvShow);
        } catch (ApiExceptionResponse apiExceptionResponse) {
            apiExceptionResponse.printStackTrace();
        }

        assertNotNull(tvAdded);
        assertEquals(TMDB_ID, tvAdded.getTmdbId());
        assertEquals(NAME, tvAdded.getName());
    }

    @Test
    void deleteTvShow() {
        tvShowService = new TvShowServiceImpl(tvShowRepository);

        TvShowDto tvDeleted= null;
        try {
            tvDeleted = tvShowService.deleteTvShow(ID);
        } catch (ApiExceptionResponse apiExceptionResponse) {
            apiExceptionResponse.printStackTrace();
        }

        assertNotNull(tvDeleted);
        assertEquals(TMDB_ID, tvDeleted.getTmdbId());
        assertEquals(NAME, tvDeleted.getName());
    }*/
}