package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.dto.user.UserCredentialsDto;
import com.PSproject.TvShowsTracker.dto.user.UserDto;
import com.PSproject.TvShowsTracker.dto.user.UserLogInDetailsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.user.UserMapper;
import com.PSproject.TvShowsTracker.model.user.AuthSession;
import com.PSproject.TvShowsTracker.model.user.BasicUser;
import com.PSproject.TvShowsTracker.repository.AuthSessionRepository;
import com.PSproject.TvShowsTracker.repository.BasicUserRepository;
import com.PSproject.TvShowsTracker.service.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BasicUserServiceImpl implements BasicUserService {

    private final BasicUserRepository basicUserRepository;
    private final AuthSessionRepository authSessionRepository;

    public BasicUserServiceImpl(BasicUserRepository basicUserRepository, AuthSessionRepository authSessionRepository) {
        this.basicUserRepository = basicUserRepository;
        this.authSessionRepository = authSessionRepository;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<UserDto> fetchAll() throws ApiExceptionResponse {
        List<UserDto> users = new ArrayList<>();
        basicUserRepository.findAll().forEach((user)-> users.add(UserMapper.mapModelToDto(user)));
        if (users.size() == 0) throw ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("No users")
                .errors(Collections.singletonList("error.users.no_users"))
                .build();
        return users;
    }

    @Override
    @Transactional
    public UserDto findUserById(Long id) throws ApiExceptionResponse {
        BasicUser user;
        try {
            user = basicUserRepository.findById(id).orElseThrow();
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
        return UserMapper.mapModelToDto(user);
    }

    @Override
    @Transactional
    public UserDto findUserByUsername(String username) {
        BasicUser user = basicUserRepository.findByUsername(username);
        if (user == null) return null;
        return UserMapper.mapModelToDto(user);
    }

    @Override
    @Transactional
    public UserDto findUserByEmail(String email) {
        BasicUser user = basicUserRepository.findByEmail(email);
        if (user == null) return null;
        return UserMapper.mapModelToDto(user);
    }

    @Override
    @Transactional
    public UserDto editBasicUser(Long id, UserDto updateUserDto) throws ApiExceptionResponse {
        BasicUser userToEdit;
        try {
            userToEdit = basicUserRepository.findById(id).orElseThrow();
            if (!userToEdit.getUsername().equals(updateUserDto.getUsername())) {
                if (basicUserRepository.findByUsername(updateUserDto.getUsername()) != null) {
                    throw ApiExceptionResponse.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message("This username already exists")
                            .errors(Collections.singletonList("error.user.cant_add_user"))
                            .build();
                }
                userToEdit.setUsername(updateUserDto.getUsername());
            }
            if (!userToEdit.getEmail().equals(updateUserDto.getEmail())) {
                if (basicUserRepository.findByEmail(updateUserDto.getEmail()) != null) {
                    throw ApiExceptionResponse.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message("This email already exists")
                            .errors(Collections.singletonList("error.user.cant_add_user"))
                            .build();
                }
                userToEdit.setEmail(updateUserDto.getEmail());
            }
            userToEdit.setIsActive(updateUserDto.getIsActive());
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
        return UserMapper.mapModelToDto(basicUserRepository.save(userToEdit));
    }

    @Override
    @Transactional
    public UserDto deleteBasicUser(Long id) throws ApiExceptionResponse {
        BasicUser userToDelete;
        try {
            userToDelete = basicUserRepository.findById(id).orElseThrow();
            basicUserRepository.delete(userToDelete);
            authSessionRepository.delete(userToDelete.getAuthSession());
        } catch (NoSuchElementException e) {
            ArrayList<String> errors=new ArrayList<>();
            errors.add("error.user.not_found");
            errors.add("error.id_user.id_not_found");
            errors.add("error.delete_user.cant_delete_user");
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("User not found to delete")
                    .errors(errors)
                    .build();
        }
        return UserMapper.mapModelToDto(userToDelete);
    }

    @Override
    @Transactional
    public UserLogInDetailsDto login(UserCredentialsDto credentialsDto) throws ApiExceptionResponse {
        BasicUser getUser = basicUserRepository.findByUsername(credentialsDto.getUsername());
        if (getUser == null) throw ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("User not found")
                .errors(Collections.singletonList("error.user.not_found"))
                .build();
        if (!getUser.getIsActive()) throw ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("User blocked")
                .errors(Collections.singletonList("error.user.blocked"))
                .build();
        boolean passwordIsValid = passwordEncoder.matches(credentialsDto.getPassword(), getUser.getPassword());
        if (!passwordIsValid) throw ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Wrong password")
                .errors(Collections.singletonList("error.password.wrong"))
                .build();

        AuthSession authSession = getUser.getAuthSession();
        if (authSession.getIsActive().equals(Boolean.TRUE)) throw ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Already login")
                .errors(Collections.singletonList("error.password.wrong"))
                .build();
        authSession.setIsActive(Boolean.TRUE);
        authSession.setDateLastLogin(new Date());

        authSessionRepository.save(authSession);

        return UserLogInDetailsDto.builder()
                .id(getUser.getId())
                .role(getUser.getRole())
                .build();
    }

    @Override
    public void logout(Long id) throws ApiExceptionResponse {
        try {
            BasicUser user = basicUserRepository.findById(id).orElseThrow();
            AuthSession authSession = user.getAuthSession();
            authSession.setIsActive(Boolean.FALSE);
            authSession.setDateLastLogout(new Date());
            basicUserRepository.save(user);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("User not found")
                    .errors(Collections.singletonList("error.user.not_found"))
                    .build();
        }
    }

}
