package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.constants.Paths;
import com.PSproject.TvShowsTracker.constants.Role;
import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.user.MyAdminMapper;
import com.PSproject.TvShowsTracker.mapper.user.UserMapper;
import com.PSproject.TvShowsTracker.model.user.AuthSession;
import com.PSproject.TvShowsTracker.model.user.MyAdmin;
import com.PSproject.TvShowsTracker.repository.AuthSessionRepository;
import com.PSproject.TvShowsTracker.repository.MyAdminRepository;
import com.PSproject.TvShowsTracker.service.BasicUserService;
import com.PSproject.TvShowsTracker.service.MyAdminService;
import com.PSproject.TvShowsTracker.utils.email.EmailMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;

@Service
public class MyAdminServiceImpl implements MyAdminService {

    private final static String SUBJECT = "Admin account confirmation";

    private final MyAdminRepository myAdminRepository;
    private final BasicUserService basicUserService;
    private final AuthSessionRepository authSessionRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MyAdminServiceImpl(MyAdminRepository myAdminRepository, BasicUserService basicUserService, AuthSessionRepository authSessionRepository) {
        this.myAdminRepository = myAdminRepository;
        this.basicUserService = basicUserService;
        this.authSessionRepository = authSessionRepository;
    }

    @Override
    @Transactional
    public UserDto addAdmin(UserDto newAdminDto) throws ApiExceptionResponse, MessagingException, UnsupportedEncodingException {
        if (basicUserService.findUserByUsername(newAdminDto.getUsername()) != null) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("This username already exists")
                    .errors(Collections.singletonList("error.user.cant_add_user"))
                    .build();
        }
        if (basicUserService.findUserByEmail(newAdminDto.getEmail()) != null) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("This email already exists")
                    .errors(Collections.singletonList("error.user.cant_add_user"))
                    .build();
        }
        newAdminDto.setDateJoined(new Date());
        newAdminDto.setRole(Role.ADMIN);
        MyAdmin admin = MyAdminMapper.mapDtoToModel(newAdminDto);
        AuthSession authSession = authSessionRepository.save(AuthSession.builder().isActive(Boolean.FALSE).build());
        admin.setAuthSession(authSession);
        newAdminDto.setPassword(passwordEncoder.encode(newAdminDto.getPassword()));
        MyAdmin adminAdded = myAdminRepository.save(admin);

        String url = Paths.CONTEXT_PATH;
        String messageHeader = "<h2>Welcome aboard, " + admin.getUsername() + "!</h2>";
        String messageBody = "<p>We are sure that you will prove to be a great addition to the TST team.</p>";
        String singInLink = "\n\n<a href=\"" + url + "\"><button>Log in now</button></a>";
        EmailMaker.sendEmail(emailSender, admin.getEmail(), messageHeader + messageBody + singInLink, SUBJECT);
        return UserMapper.mapModelToDto(adminAdded);
    }

}
