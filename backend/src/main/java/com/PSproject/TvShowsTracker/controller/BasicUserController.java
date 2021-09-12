package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.dto.user.UserCredentialsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.BasicUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/TST/basic-user")
public class BasicUserController {

    private final BasicUserService basicUserService;

    public BasicUserController(BasicUserService basicUserService) {
        this.basicUserService = basicUserService;
    }

    @ApiOperation(value = "Returns a list of all users and all admins")
    @GetMapping("/view")
    public ResponseEntity fetchAll() throws ApiExceptionResponse {
        return ResponseEntity.ok().body(basicUserService.fetchAll());
    }

    @ApiOperation(value = "Return an user/admin")
    @GetMapping("/{id}")
    public ResponseEntity findUser(@ApiParam(value = "Requires a user id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(basicUserService.findUserById(id));
    }

    @ApiOperation(value = "Update an user/admin (common fields)")
    @PutMapping("/{id}/edit")
    public ResponseEntity editUser(@ApiParam(value = "Requires a user id") @PathVariable(value = "id") Long id, @ApiParam(value = "Requires the updated fields") @RequestBody @Valid UserDto updateUser) throws ApiExceptionResponse {
        basicUserService.editBasicUser(id, updateUser);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete an user/admin")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteUser(@ApiParam(value = "Requires a user id") @PathVariable(value = "id") Long id) throws ApiExceptionResponse {
        basicUserService.deleteBasicUser(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Login method")
    @PostMapping("/login")
    public ResponseEntity login(@ApiParam(value = "Requires the credentials (username and password)")@RequestBody @Valid UserCredentialsDto user) throws ApiExceptionResponse {
        return ResponseEntity.ok().body(basicUserService.login(user));
    }

    @ApiOperation(value = "Logout method")
    @PostMapping("/logout/{id}")
    public ResponseEntity logout(@ApiParam(value = "Requires a user id")@PathVariable Long id) throws ApiExceptionResponse {
        basicUserService.logout(id);
        return ResponseEntity.ok().build();
    }
}
