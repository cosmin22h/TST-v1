package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDetailsDto;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowInfoDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.tvshow.TvShowMapper;
import com.PSproject.TvShowsTracker.service.TvShowService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/TST/tv-show")
public class TvShowController {

    private final TvShowService tvShowService;

    public TvShowController(TvShowService tvShowService) {
        this.tvShowService = tvShowService;
    }

    @ApiOperation(value = "Return a list of tv shows (display for admins)")
    @GetMapping("/view")
    public ResponseEntity fetchAllDetails() throws ApiExceptionResponse {
        List<TvShowDetailsDto> tvShows = new ArrayList<>();
        tvShowService.fetchAll().forEach(tvShowDto -> tvShows.add(TvShowMapper.mapDtoToDetailsDto(tvShowDto)));
        return ResponseEntity.ok().body(tvShows);
    }

    @ApiOperation(value = "Return a list of tv shows (display for users)")
    @GetMapping("/info")
    public ResponseEntity fetchAllInfos() throws ApiExceptionResponse {
        List<TvShowInfoDto> tvShows = new ArrayList<>();
        tvShowService.fetchAll().forEach(tvShowDto -> tvShows.add(TvShowInfoDto.builder()
                .id(tvShowDto.getId())
                .name(tvShowDto.getName())
                .noOfSeasons(tvShowDto.getNoOfSeasons())
                .status(tvShowDto.getStatus())
                .posterPath(tvShowDto.getPosterPath())
                .build()));
        return ResponseEntity.ok().body(tvShows);
    }

    @ApiOperation(value = "Return a list of tv show")
    @GetMapping("")
    public ResponseEntity fetchAll() throws ApiExceptionResponse {
        return ResponseEntity.ok().body(tvShowService.fetchAll());
    }

    @ApiOperation(value = "Return a tv show by TMDB id")
    @GetMapping("/add/check/{id}")
    public ResponseEntity findTvShowByTMDBid(@ApiParam(value = "Requires a tv show TMDB id") @PathVariable(value = "id") Long tmdbId) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(tvShowService.findTvShowByTMDBid(tmdbId));
    }

    @ApiOperation(value = "Return a tv show by id")
    @GetMapping("/{id}")
    public ResponseEntity findTvShowById(@ApiParam(value = "Requires a tv show id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(tvShowService.findTvShowById(id));
    }

    @ApiOperation(value = "Add a new tv show")
    @PostMapping("/add")
    public ResponseEntity addTvShow(@ApiParam(value = "Requires a tv show body") @RequestBody @Valid TvShowDto newTvShow) throws ApiExceptionResponse {
        tvShowService.addTvShow(newTvShow);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update a tv show")
    @PutMapping("/{id}/edit")
    public ResponseEntity editTvShow(@ApiParam(value = "Requires a tv show id") @PathVariable(value = "id") Long id, @ApiParam(value = "Requires the updated fields") @RequestBody @Valid TvShowDto updateTvShowDto) throws ApiExceptionResponse {
        tvShowService.editTvShow(id, updateTvShowDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete a tv show")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteTvShow(@ApiParam(value = "Requires a tv show id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        tvShowService.deleteTvShow(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return a list of 8 random tv shows (info fields)")
    @GetMapping("/posters")
    public ResponseEntity fetchRandomPosters() throws ApiExceptionResponse {
        List<TvShowInfoDto> allPosters = tvShowService.fetchAllPosters();
        Random random = new Random();
        Set<TvShowInfoDto> randomPosters = new HashSet<>();
        while(randomPosters.size() < 8) {
            Integer index = random.nextInt(allPosters.size());
            randomPosters.add(allPosters.get(index));
        }

        return ResponseEntity.ok().body(randomPosters);
    }

    @ApiOperation(value = "Return a list of matched tv shows")
    @GetMapping("/search/{term}")
    public ResponseEntity fetchAllMatched(@ApiParam(value = "Requires a search term") @PathVariable(value = "term") String term) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(tvShowService.fetchTvShowByMatch(term));
    }
}
