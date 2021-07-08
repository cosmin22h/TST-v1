package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.constants.ListType;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsDetailsDto;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsIdsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.ListTvShowsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/TST/lists")
public class ListTvShowsController {

    private final ListTvShowsService listTvShowsService;

    public ListTvShowsController(ListTvShowsService listTvShowsService) {
        this.listTvShowsService = listTvShowsService;
    }

    @ApiOperation(value = "Return a list of tv shows by a type")
    @GetMapping("/all-tv-show/{type}/{idUser}")
    public ResponseEntity findAllTvShowForAList(@ApiParam(value = "Requires a list type") @PathVariable ListType type, @ApiParam(value = "Requires a user id") @PathVariable Long idUser) throws ApiExceptionResponse {
        List<ListTvShowsDetailsDto> list = listTvShowsService.findTvShowsInListForUser(ListTvShowsIdsDto.builder()
                .type(type)
                .idUser(idUser)
                .build(), false);
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Return a list of the first 4 tv shows for each type")
    @GetMapping("/only-four-tv-show/{idUser}")
    public ResponseEntity findOnlyFourTvShowForAList(@ApiParam(value = "Requires a user id") @PathVariable Long idUser) throws ApiExceptionResponse {
        List<ListTvShowsDetailsDto> list = listTvShowsService.findTvShowsInListForUser(ListTvShowsIdsDto.builder()
                .idUser(idUser)
                .build(), true);
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Return the tv show lists")
    @GetMapping("/find-in-list/{idUser}/{type}/{idTvShow}")
    public ResponseEntity findInList(@ApiParam(value = "Requires a user id") @PathVariable Long idUser, @ApiParam(value = "Requires a list type") @PathVariable ListType type, @ApiParam(value = "Requires a tv show id") @PathVariable Long idTvShow) throws ApiExceptionResponse {
        ListTvShowsIdsDto tvShowsToFind = ListTvShowsIdsDto.builder()
                .idTvShow(idTvShow)
                .idUser(idUser)
                .type(type)
                .build();

        return ResponseEntity.ok().body(listTvShowsService.findTvShowInList(tvShowsToFind));
    }

    @ApiOperation(value = "Add a tv show to a list")
    @PostMapping("/add-to-list")
    public ResponseEntity addToList(@ApiParam(value = "Requires a body with the list type and the user and tv show id") @RequestBody @Valid ListTvShowsIdsDto newTvShowToList) throws ApiExceptionResponse {
        if (newTvShowToList.getType().equals(ListType.RECENTLY_WATCHED)) {
            listTvShowsService.addTvShowToRecentlyWatched(newTvShowToList);
        } else {
            listTvShowsService.addTvShowToList(newTvShowToList);
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Remove a tv show from a list")
    @DeleteMapping("/remove-from-list/{type}/{idTvShow}/{idUser}")
    public ResponseEntity removeFromList(@ApiParam(value = "Requires a list type") @PathVariable ListType type, @ApiParam(value = "Requires a tv show id") @PathVariable Long idTvShow, @ApiParam(value = "Requires a user id") @PathVariable Long idUser) throws ApiExceptionResponse {
        listTvShowsService.removeTvShowFromList(ListTvShowsIdsDto.builder()
                .type(type)
                .idTvShow(idTvShow)
                .idUser(idUser)
                .build());
        return ResponseEntity.ok().build();
    }
}
