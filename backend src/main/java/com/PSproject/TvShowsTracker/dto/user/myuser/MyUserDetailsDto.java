package com.PSproject.TvShowsTracker.dto.user.myuser;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MyUserDetailsDto {
    private Long id;

    @NotNull(message = "Username is mandatory")
    @Size(min = 5, message = "Username must have at least {min} characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can't contains special characters")
    private String username;

    @NotNull(message = "Email is mandatory")
    @Email
    private String email;

    @NotBlank(message = "Display name is mandatory")
    @Size(min = 3, message = "Display name must have at least {min} characters")
    private String displayName;

    @Size(max = 1000, message = "About must have maximum {max} characters")
    private String about;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    private String gender;
    private String country;
    private String facebook;
    private String instagram;
    private String twitter;
    private String reddit;
}
