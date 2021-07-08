package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.constants.Paths;
import com.PSproject.TvShowsTracker.dto.user.UserCredentialsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.model.user.BasicUser;
import com.PSproject.TvShowsTracker.model.user.PasswordResetToken;
import com.PSproject.TvShowsTracker.repository.BasicUserRepository;
import com.PSproject.TvShowsTracker.repository.MyUserRepository;
import com.PSproject.TvShowsTracker.repository.PasswordResetTokenRepository;
import com.PSproject.TvShowsTracker.service.PasswordResetService;
import com.PSproject.TvShowsTracker.utils.email.EmailMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final static int EXPIRATION_MINUTES = 10;
    private final static String SUBJECT = "Reset password";

    private final BasicUserRepository basicUserRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordResetServiceImpl(MyUserRepository myUserRepository, BasicUserRepository basicUserRepository, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.basicUserRepository = basicUserRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    @Transactional
    public void createPasswordResetToken(String userEmail) throws MessagingException, UnsupportedEncodingException {
        BasicUser user = basicUserRepository.findByEmail(userEmail);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.MINUTE, EXPIRATION_MINUTES);
            PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(cal.getTime())
                    .build();
            passwordResetTokenRepository.save(passwordResetToken);
            String url = Paths.CONTEXT_PATH + "/user/change-password/" + token;
            String messageHeader = "<h2>Hi, " + user.getUsername() + "</h2>";
            String messageBody = "<p>A request has been received to change the password for your TST account."
                    + " Just click the button below and you'll be on your way."
                    + " If you did not make this request, please ignore this email.</p>";
            String resetLink = "\n\n<a href=\"" + url + "\"><button>Reset password</button></a>";
            EmailMaker.sendEmail(emailSender, user.getEmail(), messageHeader + messageBody + resetLink, SUBJECT);
        } else {
            String url = Paths.CONTEXT_PATH + "/register";
            String messageHeader = "<h2>Hi there</h2>";
            String messageBody = "<p>A request has been received to change the password for your TST account."
                    + " But you do not have an account. If you did not make this request, please ignore this email.</p>";
            String singInLink = "\n\n<a href=\"" + url + "\"><button>Sign up now</button></a>";
            EmailMaker.sendEmail(emailSender, userEmail, messageHeader + messageBody + singInLink, SUBJECT);
        }
    }

    @Override
    @Transactional
    public String validatePasswordResetToken(String token) throws ApiExceptionResponse, ParseException {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Invalid token")
                    .errors(Collections.singletonList("error.token.invalid_token"))
                    .build();
        }
        if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(passwordResetToken.getExpiryDate().toString()).before(new Date())) {
            throw  ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Expired token")
                    .errors(Collections.singletonList("error.token.expired_token"))
                    .build();
        }
        String username = passwordResetToken.getUser().getUsername();
        passwordResetTokenRepository.delete(passwordResetToken);
        return username;
    }

    @Override
    @Transactional
    public void setNewPassword(UserCredentialsDto userCredentialsDto) {
        BasicUser user = basicUserRepository.findByUsername(userCredentialsDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCredentialsDto.getPassword()));
        basicUserRepository.save(user);
    }
}
