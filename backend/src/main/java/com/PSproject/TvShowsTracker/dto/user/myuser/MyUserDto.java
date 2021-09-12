package com.PSproject.TvShowsTracker.dto.user.myuser;

import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyUserDto extends UserDto {
    private Long id;
    private byte[] avatar;

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
