package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.tvshow.EpisodeDto;
import com.PSproject.TvShowsTracker.dto.tvshow.TvShowDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.EpisodeService;
import com.PSproject.TvShowsTracker.service.TvShowService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/TST/episode")
public class EpisodeController {

    private final TvShowService tvShowService;
    private final EpisodeService episodeService;

    public EpisodeController(TvShowService tvShowService, EpisodeService episodeService) {
        this.tvShowService = tvShowService;
        this.episodeService = episodeService;
    }

    @ApiOperation(value = "Add a new list of episodes to a tv show")
    @PostMapping("/add/tv-show/{id}")
    public ResponseEntity addEpisodes(@ApiParam(value = "Requires a tv show id") @PathVariable(value = "id") Long id, @ApiParam(value = "Requires a list of episodes") @RequestBody List<@Valid EpisodeDto> episodes) throws ApiExceptionResponse {
        TvShowDto tvShow = tvShowService.findTvShowByTMDBid(id);
        for (EpisodeDto e: episodes) {
            episodeService.addEpisode(tvShow, e);
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Add a new episode to tv show")
    @PostMapping("/add-one/tv-show/{id}")
    public ResponseEntity addEpisode(@ApiParam(value = "Requires a tv show id") @PathVariable(value = "id") Long id, @ApiParam(value = "Requires an episode body") @RequestBody @Valid EpisodeDto episode) throws ApiExceptionResponse {
        TvShowDto tvShow = tvShowService.findTvShowById(id);
        episodeService.addEpisode(tvShow, episode);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return a list of episodes")
    @GetMapping("/episodes/tv-show/{id}")
    public ResponseEntity getEpisodesByTvShow(@ApiParam(value = "Requires a tv show id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        TvShowDto tvShowDto = tvShowService.findTvShowById(id);
        return ResponseEntity.ok().body(episodeService.findAllByTvShow(tvShowDto));
    }

    @ApiOperation(value = "Return an episode by id")
    @GetMapping("/{id}")
    public ResponseEntity getEpisode(@ApiParam(value = "Requires an episode id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(episodeService.findById(id));
    }

    @ApiOperation(value = "Update an episode")
    @PutMapping("/edit/{id}")
    public ResponseEntity editEpisode(@ApiParam(value = "Requires an episode id") @PathVariable(value = "id") Long id, @ApiParam(value = "Requires the updated fields") @RequestBody @Valid EpisodeDto updateTvShow) throws ApiExceptionResponse {
        episodeService.editEpisode(id, updateTvShow);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete an episode")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEpisode(@ApiParam(value = "Requires an episode id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        episodeService.deleteEpisode(id);
        return ResponseEntity.ok().build();
    }
}
