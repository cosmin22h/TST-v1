package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.user.UserEpisodeIdsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.UserEpisodeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/TST/episode-viewed")
public class UserEpisodeController {

    private final UserEpisodeService userEpisodeService;

    public UserEpisodeController(UserEpisodeService userEpisodeService) {
        this.userEpisodeService = userEpisodeService;
    }

    @ApiOperation(value = "Return a list of the viewed episodes by a user for a tv show")
    @GetMapping("/tv-show/{idTvShow}/{idUser}")
    public ResponseEntity fetchAllViewedEpisodes(@ApiParam(value = "Requires a tv show id") @PathVariable Long idTvShow, @ApiParam(value = "Requires a user id") @PathVariable Long idUser) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(userEpisodeService.findAllViewedEpisodesByTvShow(idTvShow, idUser));
    }

    @ApiOperation(value = "Mark an episode")
    @PostMapping("/add")
    public ResponseEntity addViewedEpisode(@ApiParam(value = "Requires a body with the user and the episode id") @RequestBody @Valid UserEpisodeIdsDto episodeViewed) throws ApiExceptionResponse {
        userEpisodeService.addViewedEpisode(episodeViewed);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Rate an episode")
    @PutMapping("/rate")
    public ResponseEntity rateEpisode(@ApiParam(value = "Requires a body with the user and the episode id and the rating") @RequestBody @Valid UserEpisodeIdsDto episodeRated) throws ApiExceptionResponse {
        userEpisodeService.rateEpisode(episodeRated);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Unmark an episode")
    @DeleteMapping("/delete/{idUser}/{idEpisode}")
    public ResponseEntity deleteViewedEpisode(@ApiParam(value = "Requires a user id") @PathVariable Long idUser, @ApiParam(value = "Requires an episode id") @PathVariable Long idEpisode) throws ApiExceptionResponse {
        UserEpisodeIdsDto userEpisodeIdsDto = UserEpisodeIdsDto.builder().idEpisode(idEpisode).idUser(idUser).build();
        userEpisodeService.removeViewedEpisode(userEpisodeIdsDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return profile metrics")
    @GetMapping("/get-metrics/{id}")
    public ResponseEntity getProfileMetrics(@ApiParam(value = "Requires a user id") @PathVariable Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(userEpisodeService.getProfileMetrics(id));
    }
}
