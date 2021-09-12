package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.dto.user.FriendsDto;
import com.PSproject.TvShowsTracker.dto.user.FriendsIdsDto;
import com.PSproject.TvShowsTracker.dto.user.FriendsProfileDto;
import com.PSproject.TvShowsTracker.dto.user.myuser.MyUserProfileDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.model.user.Friends;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.repository.FriendsRepository;
import com.PSproject.TvShowsTracker.repository.MyUserRepository;
import com.PSproject.TvShowsTracker.service.FriendsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FriendsServiceImp implements FriendsService {

    private final FriendsRepository friendsRepository;
    private final MyUserRepository myUserRepository;

    public FriendsServiceImp(FriendsRepository friendsRepository, MyUserRepository myUserRepository) {
        this.friendsRepository = friendsRepository;
        this.myUserRepository = myUserRepository;
    }

    @Override
    @Transactional
    public FriendsIdsDto sendRequest(FriendsIdsDto request) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(request.getIdUser()).orElseThrow();
            MyUser friend = myUserRepository.findById(request.getIdFriend()).orElseThrow();

            Friends friends = Friends.builder()
                    .myUser(user)
                    .friend(friend)
                    .isAccepted(false)
                    .build();
            friendsRepository.save(friends);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't sent request")
                    .errors(Collections.singletonList("error.user.user_not_found"))
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    public FriendsDto acceptRequest(FriendsIdsDto request) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(request.getIdUser()).orElseThrow();
            MyUser userRequest = myUserRepository.findById(request.getIdFriend()).orElseThrow();

            List<Friends> friendRequests = user.getFollowers();

            Friends friend = null;
            for (Friends f: friendRequests) {
                if (f.getMyUser().getId().equals(request.getIdFriend())) {
                    friend = f;
                    break;
                }
            }

            if (friend == null) throw new NoSuchElementException();

            friend.setIsAccepted(Boolean.TRUE);
            Friends newFriend = Friends.builder()
                    .isAccepted(Boolean.TRUE)
                    .myUser(user)
                    .friend(userRequest)
                    .build();

            friendsRepository.save(friend);
            friendsRepository.save(newFriend);

            return FriendsDto.builder().friend(newFriend.getFriend()).myUser(newFriend.getMyUser()).build();
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't accept request")
                    .errors(Collections.singletonList("error.user.user_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public FriendsIdsDto rejectRequest(Long idUser, Long idFriend) throws ApiExceptionResponse {
        try {
            MyUser userToFind = myUserRepository.findById(idUser).orElseThrow();

            List<Friends> friendsUser = friendsRepository.findAllByFriend(userToFind);
            Friends userFriend = null;
            for (Friends f: friendsUser) {
                if (f.getMyUser().getId().equals(idFriend)) {
                    userFriend = f;
                    break;
                }
            }

            friendsRepository.delete(userFriend);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't reject request")
                    .errors(Collections.singletonList("error.user.user_not_found"))
                    .build();
        }

        return null;
    }

    @Override
    @Transactional
    public FriendsIdsDto unfriend(Long idUser, Long idFriend) throws ApiExceptionResponse {
        try {
            MyUser userToFind = myUserRepository.findById(idUser).orElseThrow();

            List<Friends> friendsUser = friendsRepository.findAllByMyUser(userToFind);
            List<Friends> userFriends = friendsRepository.findAllByFriend(userToFind);

            Friends userFriend = null;
            for (Friends f: friendsUser) {
                if (f.getFriend().getId().equals(idFriend)) {
                    userFriend = f;
                    break;
                }
            }

            Friends friendUser = null;
            for (Friends f: userFriends) {
                if (f.getMyUser().getId().equals(idFriend)) {
                    friendUser = f;
                    break;
                }
            }

            friendsRepository.delete(userFriend);
            friendsRepository.delete(friendUser);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't remove the user")
                    .errors(Collections.singletonList("error.user.user_not_found"))
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    public FriendsIdsDto findFriend(Long idUser, Long idFriend) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(idUser).orElseThrow();

            FriendsIdsDto friend = null;
            for (Friends f: user.getFollowing()) {
                if (f.getFriend().getId().equals(idFriend)) {
                    friend = FriendsIdsDto.builder()
                            .id(f.getId())
                            .isAccepted(f.getIsAccepted())
                            .build();
                    break;
                }
            }

            return friend;

        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't find user")
                    .errors(Collections.singletonList("error.user.user_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public List<FriendsProfileDto> findAllFriends(Long idUser) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(idUser).orElseThrow();

            List<FriendsProfileDto> friends = new ArrayList<>();
            for (Friends f: user.getFollowers()) {
                FriendsProfileDto friend = FriendsProfileDto.builder()
                        .isAccepted(f.getIsAccepted())
                        .friend(MyUserProfileDto.builder().id(f.getMyUser().getId()).username(f.getMyUser().getUsername()).displayName(f.getMyUser().getDisplayName()).build())
                        .build();
                if (f.getIsAccepted().equals(Boolean.TRUE)) friends.add(friend);
            }

            return friends;

        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't find friends")
                    .errors(Collections.singletonList("error.user.user_not_found"))
                    .build();
        }
    }

    @Override
    @Transactional
    public List<FriendsProfileDto> findAllRequest(Long idUser) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(idUser).orElseThrow();

            List<FriendsProfileDto> friendRequests = new ArrayList<>();
            for (Friends f: user.getFollowers()) {
                FriendsProfileDto friend = FriendsProfileDto.builder()
                        .isAccepted(f.getIsAccepted())
                        .friend(MyUserProfileDto.builder().id(f.getMyUser().getId()).username(f.getMyUser().getUsername()).displayName(f.getMyUser().getDisplayName()).build())
                        .build();
                if (f.getIsAccepted().equals(Boolean.FALSE)) friendRequests.add(friend);
            }

            return friendRequests;

        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Can't find request")
                    .errors(Collections.singletonList("error.user.user_not_found"))
                    .build();
        }
    }
}
