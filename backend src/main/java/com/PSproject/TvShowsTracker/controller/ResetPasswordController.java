package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.user.UserCredentialsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.service.PasswordResetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@RestController
@CrossOrigin
@RequestMapping("/TST")
public class ResetPasswordController {

    private final PasswordResetService passwordResetService;

    public ResetPasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @ApiOperation(value = "Create a reset token")
    @PostMapping("/reset-password")
    public ResponseEntity resetPasswordRequest(@ApiParam(value = "Requires an email") @RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Validate a reset token")
    @GetMapping("/user/change-password")
    public ResponseEntity changePassword(@ApiParam(value = "Requires a token")  @RequestParam("token") String token) throws ApiExceptionResponse, ParseException {
        return ResponseEntity.ok().body(passwordResetService.validatePasswordResetToken(token));
    }

    @ApiOperation(value = "Set the new password")
    @PutMapping("/user/set-new-password")
    public ResponseEntity setNewPassword(@ApiParam(value = "Requires the credentials") @RequestBody @Valid UserCredentialsDto userCredentials) {
        passwordResetService.setNewPassword(userCredentials);
        return ResponseEntity.ok().build();
    }

}
