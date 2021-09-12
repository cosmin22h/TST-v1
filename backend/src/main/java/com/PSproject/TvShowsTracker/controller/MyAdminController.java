package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.MyAdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin
@RequestMapping("/TST/admin")
public class MyAdminController {

    private final MyAdminService myAdminService;

    public MyAdminController(MyAdminService myAdminService) {
        this.myAdminService = myAdminService;
    }

    @ApiOperation(value = "Add a new admin")
    @PostMapping("/add")
    public ResponseEntity addAdmin(@ApiParam(value = "Requires a body with the admin fields") @RequestBody @Valid UserDto newAdmin) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException {
        myAdminService.addAdmin(newAdmin);
        return ResponseEntity.ok().build();
    }
}
