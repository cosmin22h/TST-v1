package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDetailsDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserProfileDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.user.MyUserMapper;
import com.PSproject.TvShowsTracker.service.MyUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin
@RequestMapping("/TST/user")
public class MyUserController {

    private final MyUserService myUserService;
    private final SimpMessagingTemplate messagingTemplate;

    public MyUserController(MyUserService myUserService, SimpMessagingTemplate messagingTemplate) {
        this.myUserService = myUserService;
        this.messagingTemplate = messagingTemplate;
    }

    @ApiOperation(value = "Add a new user")
    @PostMapping("/add")
    public ResponseEntity addUser(@ApiParam(value = "Requires a body with the user fields for sign up") @RequestBody @Valid MyUserDto newUser) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException {
        UserDto user = myUserService.addUser(newUser);
        messagingTemplate.convertAndSend("/topic/socket/user/register", "New user registration: " + user.getUsername());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return an user by id")
    @GetMapping("/get/{id}")
    public ResponseEntity getUser(@ApiParam(value = "Requires a user id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(MyUserMapper.mapDtoToDetailsDto(myUserService.findById(id)));
    }

    @ApiOperation(value = "Update an user")
    @PutMapping("/edit/{id}")
    public ResponseEntity editUser(@ApiParam(value = "Requires a user id") @PathVariable(value = "id") Long id, @ApiParam(value = "Requires the updated fields") @RequestBody @Valid MyUserDetailsDto updateUser) throws ApiExceptionResponse {
        myUserService.editUser(id, updateUser);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return the profile info of an user for display")
    @GetMapping("/profile/{id}")
    public ResponseEntity getProfile(@ApiParam(value = "Requires a user id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        MyUserDto profile = myUserService.findById(id);
        return ResponseEntity.ok().body(MyUserProfileDto.builder()
                .displayName(profile.getDisplayName())
                .build());
    }

    @ApiOperation(value = "Return an user by username")
    @GetMapping("/{id}/get-user/{username}")
    public ResponseEntity getUserByUsername(@ApiParam(value = "Requires a user id") @PathVariable Long id, @ApiParam(value = "Requires a username") @PathVariable String username) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(MyUserMapper.mapDtoToDetailsDto(myUserService.findByUsername(id, username)));
    }

    @ApiOperation(value = "Upload avatar pic")
    @PostMapping("/upload-avatar/{id}")
    public ResponseEntity uploadAvatar(@ApiParam(value = "Requires a user id") @PathVariable Long id, @ApiParam(value = "Requires a file")@RequestParam MultipartFile avatar) throws ApiExceptionResponse {
        myUserService.updateAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return avatar pic")
    @GetMapping(value = "/avatar/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity getAvatar(@ApiParam(value = "Requires a user id") @PathVariable Long id) throws ApiExceptionResponse {
        MyUserDto user = myUserService.findById(id);
        return ResponseEntity.ok().body(user.getAvatar());
    }

    @ApiOperation(value = "Delete avatar pic")
    @DeleteMapping("/delete-avatar/{id}")
    public ResponseEntity deleteAvatar(@ApiParam(value = "Requires a user id") @PathVariable Long id) throws ApiExceptionResponse {
        myUserService.updateAvatar(id, null);
        return ResponseEntity.ok().build();
    }

}
