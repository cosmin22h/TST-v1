package com.PSproject.TvShowsTracker.dto.user.myuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MyUserProfileDto {
    private Long id;

    @NotNull(message = "Username is mandatory")
    @Size(min = 5, message = "Username must have at least {min} characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can't contains special characters")
    private String username;

    @NotBlank(message = "Display name is mandatory")
    @Size(min = 3, message = "Display name must have at least {min} characters")
    private String displayName;
}
