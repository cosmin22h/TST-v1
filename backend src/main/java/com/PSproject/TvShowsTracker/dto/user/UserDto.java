package com.PSproject.TvShowsTracker.dto.user;

import com.PSproject.TvShowsTracker.constants.Role;
import com.PSproject.TvShowsTracker.model.user.AuthSession;
import com.PSproject.TvShowsTracker.validators.UserRoleSubset;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private Long id;

    @NotNull(message = "Username is mandatory")
    @Size(min = 5, message = "Username must have at least {min} characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can't contains special characters")
    private String username;

    @NotNull(message = "Email is mandatory")
    @Email
    private String email;

    private Boolean isActive;

    @UserRoleSubset(anyOf = {Role.USER, Role.ADMIN})
    private Role role;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dateJoined;

    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password must have at least {min} characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\W).*$",
            message = "Password must contains  at least 1 uppercase, 1 lowercase, 1 digit, 1 special character")
    private String password;

    private AuthSession authSession;
}
