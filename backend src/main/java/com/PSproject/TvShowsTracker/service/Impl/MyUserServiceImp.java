package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.constants.Paths;
import com.PSproject.TvShowsTracker.constants.Role;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDetailsDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.user.MyUserMapper;
import com.PSproject.TvShowsTracker.model.user.AuthSession;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.repository.AuthSessionRepository;
import com.PSproject.TvShowsTracker.repository.BasicUserRepository;
import com.PSproject.TvShowsTracker.repository.MyUserRepository;
import com.PSproject.TvShowsTracker.service.MyUserService;
import com.PSproject.TvShowsTracker.utils.email.EmailMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class MyUserServiceImp implements MyUserService {

    private final static String SUBJECT = "Account confirmation";

    private final MyUserRepository myUserRepository;
    private final BasicUserRepository basicUserRepository;
    private final AuthSessionRepository authSessionRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MyUserServiceImp(MyUserRepository myUserRepository, BasicUserRepository basicUserRepository, AuthSessionRepository authSessionRepository) {
        this.myUserRepository = myUserRepository;
        this.basicUserRepository = basicUserRepository;
        this.authSessionRepository = authSessionRepository;
    }

    @Override
    @Transactional
    public MyUserDto findById(Long id) throws ApiExceptionResponse {
        try {
            return MyUserMapper.mapModelToDto(myUserRepository.findById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.user.not_found");
            errors.add("error.id_user.id_not_found");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("User not found")
                    .errors(errors)
                    .build();
        }
    }

    @Override
    @Transactional
    public MyUserDto findByUsername(Long id, String username) throws ApiExceptionResponse {
        MyUser user = myUserRepository.findByUsername(username);
        if (user == null) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.user.not_found");
            errors.add("error.id_user.id_not_found");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("User not found")
                    .errors(errors)
                    .build();
        }
        return MyUserMapper.mapModelToDto(user);
    }

    @Override
    @Transactional
    public MyUserDto addUser(MyUserDto newUserDto) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException {
        if (basicUserRepository.findByUsername(newUserDto.getUsername()) != null) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("This username already exists")
                    .errors(Collections.singletonList("error.user.cant_add_user"))
                    .build();
        }
        if (basicUserRepository.findByUsername(newUserDto.getEmail()) != null) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("This email already exists")
                    .errors(Collections.singletonList("error.user.cant_add_user"))
                    .build();
        }
        if (newUserDto.getIsActive() == null) newUserDto.setIsActive(Boolean.TRUE);
        newUserDto.setRole(Role.USER);
        newUserDto.setDateJoined(new Date());
        newUserDto.setDisplayName(newUserDto.getUsername());
        MyUser newMyUser = MyUserMapper.mapDtoToModel(newUserDto);

        AuthSession authSession = authSessionRepository.save(AuthSession.builder().isActive(Boolean.FALSE).build());
        newMyUser.setAuthSession(authSession);
        newMyUser.setPassword(passwordEncoder.encode(newMyUser.getPassword()));
        MyUser userAdded = myUserRepository.save(newMyUser);

        String url = Paths.CONTEXT_PATH;
        String messageHeader = "<h2>Welcome to the party, " + userAdded.getUsername() + "!</h2>";
        String messageBody = "<p>Your new <a href=\"" + url +"\">TST</a> account has been created.<br/>"
                +"Thank you!</p>";
        String singInLink = "\n\n<a href=\"" + url + "\"><button>Log in now</button></a>";
        EmailMaker.sendEmail(emailSender, userAdded.getEmail(), messageHeader + messageBody + singInLink, SUBJECT);
        return MyUserMapper.mapModelToDto(userAdded);
    }

    @Override
    @Transactional
    public MyUserDto editUser(Long id, MyUserDetailsDto userDetailsDto) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(id).orElseThrow();
            if(!user.getUsername().equals(userDetailsDto.getUsername())) {
                if (basicUserRepository.findByUsername(userDetailsDto.getUsername()) != null) {
                    throw ApiExceptionResponse.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message("This username already exists")
                            .errors(Collections.singletonList("This username already exists"))
                            .build();
                }
                user.setUsername(userDetailsDto.getUsername());
            }
            if(!user.getEmail().equals(userDetailsDto.getEmail())) {
                if (myUserRepository.findByEmail(userDetailsDto.getEmail()) != null) {
                    throw ApiExceptionResponse.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message("This email already exists")
                            .errors(Collections.singletonList("This email already exists"))
                            .build();
                }
                user.setEmail(userDetailsDto.getEmail());
            }
            user.setDisplayName(userDetailsDto.getDisplayName());
            user.setAbout(userDetailsDto.getAbout());
            user.setBirthday(userDetailsDto.getBirthday());
            user.setGender(userDetailsDto.getGender());
            user.setCountry(userDetailsDto.getCountry());
            user.setFacebook(userDetailsDto.getFacebook());
            user.setInstagram(userDetailsDto.getInstagram());
            user.setTwitter(userDetailsDto.getTwitter());
            user.setReddit(userDetailsDto.getReddit());

            return MyUserMapper.mapModelToDto(myUserRepository.save(user));
        } catch (NoSuchElementException e) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.user.not_found");
            errors.add("error.id_user.id_not_found");
            errors.add("error.edit_user.cant_edit_user");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("User not found to edit")
                    .errors(errors)
                    .build();
        }

    }

    @Override
    @Transactional
    public MyUserDto updateAvatar(Long id, MultipartFile avatar) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(id).orElseThrow();
            if (avatar != null) user.setAvatar(avatar.getBytes());
            else user.setAvatar(null);
            return MyUserMapper.mapModelToDto(myUserRepository.save(user));
        } catch (NoSuchElementException e) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.user.not_found");
            errors.add("error.id_user.id_not_found");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Can' t update avatar")
                    .errors(errors)
                    .build();
        } catch (IOException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Can' t update avatar")
                    .errors(Collections.singletonList("error.update_user.cant_set_avatar"))
                    .build();
        }
    }

}
