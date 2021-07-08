package com.PSproject.TvShowsTracker.mapper.user;

import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDetailsDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDto;
import com.PSproject.TvShowsTracker.model.user.MyUser;

public class MyUserMapper {
    public static MyUserDto mapModelToDto(MyUser myUser) {
        MyUserDto myUserDto = new MyUserDto();
        myUserDto.setId(myUser.getId());
        myUserDto.setAvatar(myUser.getAvatar());
        myUserDto.setUsername(myUser.getUsername());
        myUserDto.setEmail(myUser.getEmail());
        myUserDto.setRole(myUser.getRole());
        myUserDto.setDateJoined(myUser.getDateJoined());
        myUserDto.setPassword(myUser.getPassword());
        myUserDto.setDisplayName(myUser.getDisplayName());
        myUserDto.setAbout(myUser.getAbout());
        myUserDto.setBirthday(myUser.getBirthday());
        myUserDto.setGender(myUser.getGender());
        myUserDto.setCountry(myUser.getCountry());
        myUserDto.setFacebook(myUser.getFacebook());
        myUserDto.setInstagram(myUser.getInstagram());
        myUserDto.setTwitter(myUser.getTwitter());
        myUserDto.setReddit(myUser.getReddit());

        return myUserDto;
    }

    public static MyUser mapDtoToModel(MyUserDto myUserDto) {
        MyUser myUser = new MyUser();
        if (myUserDto.getId() != null) myUser.setId(myUserDto.getId());
        myUser.setAvatar(myUserDto.getAvatar());
        myUser.setUsername(myUserDto.getUsername());
        myUser.setEmail(myUserDto.getEmail());
        myUser.setRole(myUserDto.getRole());
        myUser.setIsActive(myUserDto.getIsActive());
        myUser.setDateJoined(myUserDto.getDateJoined());
        myUser.setPassword(myUserDto.getPassword());
        myUser.setDisplayName(myUserDto.getDisplayName());
        myUser.setAbout(myUserDto.getAbout());
        myUser.setBirthday(myUserDto.getBirthday());
        myUser.setGender(myUserDto.getGender());
        myUser.setCountry(myUserDto.getCountry());
        myUser.setFacebook(myUserDto.getFacebook());
        myUser.setInstagram(myUserDto.getInstagram());
        myUser.setTwitter(myUserDto.getTwitter());
        myUser.setReddit(myUserDto.getReddit());

        return myUser;
    }

    public static MyUserDetailsDto mapDtoToDetailsDto(MyUserDto myUserDto) {
        return MyUserDetailsDto.builder()
                .id(myUserDto.getId())
                .username(myUserDto.getUsername())
                .email(myUserDto.getEmail())
                .displayName(myUserDto.getDisplayName())
                .about(myUserDto.getAbout())
                .birthday(myUserDto.getBirthday())
                .gender(myUserDto.getGender())
                .country(myUserDto.getCountry())
                .facebook(myUserDto.getFacebook())
                .instagram(myUserDto.getInstagram())
                .twitter(myUserDto.getTwitter())
                .reddit(myUserDto.getReddit()).build();
    }

}
