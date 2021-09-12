package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.user.FriendsDto;
import com.PSproject.TvShowsTracker.dto.user.FriendsIdsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.FriendsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/TST/friends")
public class FriendsController {

    private final FriendsService friendsService;
    private final SimpMessagingTemplate messagingTemplate;

    public FriendsController(FriendsService friendsService, SimpMessagingTemplate messagingTemplate) {
        this.friendsService = friendsService;
        this.messagingTemplate = messagingTemplate;
    }

    @ApiOperation(value = "Send a friend request")
    @PostMapping("/request/{idUser}/{idFriend}")
    public ResponseEntity sendRequest(@ApiParam(value = "Requires an user id") @PathVariable Long idUser, @ApiParam(value = "Requires a friend id") @PathVariable Long idFriend) throws ApiExceptionResponse {
        FriendsIdsDto request = FriendsIdsDto.builder().idUser(idUser).idFriend(idFriend).build();
        friendsService.sendRequest(request);
        messagingTemplate.convertAndSend("/topic/socket/user/send-request/" + idFriend, "New friend request");
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Accept a friend request")
    @PutMapping("/accept")
    public ResponseEntity acceptRequest(@ApiParam(value = "Requires a valid body with the users id") @RequestBody @Valid FriendsIdsDto request) throws ApiExceptionResponse {
        FriendsDto friendsDto = friendsService.acceptRequest(request);
        messagingTemplate.convertAndSend("/topic/socket/user/accept-request/" + request.getIdFriend(), friendsDto.getMyUser().getDisplayName() + " is your new friend");
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Reject a friend request")
    @DeleteMapping("/reject/{idUser}/{idFriend}")
    public ResponseEntity rejectRequest(@ApiParam(value = "Requires an user id") @PathVariable Long idUser, @ApiParam(value = "Requires a friend id") @PathVariable Long idFriend) throws ApiExceptionResponse {
        friendsService.rejectRequest(idUser, idFriend);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete a friend")
    @DeleteMapping("/unfriend/{idUser}/{idFriend}")
    public ResponseEntity unfriend(@ApiParam(value = "Requires an user id") @PathVariable Long idUser, @ApiParam(value = "Requires a friend id") @PathVariable Long idFriend) throws ApiExceptionResponse {
        friendsService.unfriend(idUser, idFriend);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return a friend")
    @GetMapping("/friend/{idUser}/{idFriend}")
    public ResponseEntity findFriend(@ApiParam(value = "Requires an user id")@PathVariable Long idUser, @ApiParam(value = "Requires a friend id")@PathVariable Long idFriend) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(friendsService.findFriend(idUser, idFriend));
    }

    @ApiOperation(value = "Return all friends")
    @GetMapping("/all-friends/{id}")
    public ResponseEntity findAllFriends(@ApiParam(value = "Requires an user id")@PathVariable Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(friendsService.findAllFriends(id));
    }

    @ApiOperation(value = "Return all friend requests")
    @GetMapping("/all-requests/{id}")
    public ResponseEntity findAllRequests(@ApiParam(value = "Requires an user id")@PathVariable Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(friendsService.findAllRequest(id));
    }

}
