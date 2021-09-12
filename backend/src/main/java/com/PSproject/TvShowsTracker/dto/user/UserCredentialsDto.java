package com.PSproject.TvShowsTracker.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCredentialsDto {
    @NotNull(message = "Username is mandatory")
    @Size(min = 5, message = "Username must have at least {min} characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can't contains special characters")
    private String username;

    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password must have at least {min} characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\W).*$",
            message = "Password must contains  at least 1 uppercase, 1 lowercase, 1 digit, 1 special character")
    private String password;
}
