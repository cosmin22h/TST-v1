package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.constants.ListType;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsDetailsDto;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsDto;
import com.PSproject.TvShowsTracker.dto.user.ListTvShowsIdsDto;
import com.PSproject.TvShowsTracker.exceptions.ApiExceptionResponse;
import com.PSproject.TvShowsTracker.mapper.ListTvShowMapper;
import com.PSproject.TvShowsTracker.mapper.tvshow.TvShowMapper;
import com.PSproject.TvShowsTracker.model.user.ListTvShows;
import com.PSproject.TvShowsTracker.model.tvshow.TvShow;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.repository.BasicUserRepository;
import com.PSproject.TvShowsTracker.repository.ListTvShowsRepository;
import com.PSproject.TvShowsTracker.repository.MyUserRepository;
import com.PSproject.TvShowsTracker.repository.TvShowRepository;
import com.PSproject.TvShowsTracker.service.ListTvShowsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ListTvShowsServiceImpl implements ListTvShowsService {

    private final ListTvShowsRepository listTvShowsRepository;
    private final MyUserRepository myUserRepository;
    private final TvShowRepository tvShowRepository;

    public ListTvShowsServiceImpl(ListTvShowsRepository listTvShowsRepository, MyUserRepository myUserRepository, TvShowRepository tvShowRepository, BasicUserRepository basicUserRepository) {
        this.listTvShowsRepository = listTvShowsRepository;
        this.myUserRepository = myUserRepository;
        this.tvShowRepository = tvShowRepository;
    }

    @Override
    @Transactional
    public List<ListType> findTvShowInList(ListTvShowsIdsDto tvShowsToFind) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(tvShowsToFind.getIdUser()).orElseThrow();
            List<ListTvShows> lists = listTvShowsRepository.findAllByMyUser(user);

            List<ListType> listsName = new ArrayList<>();

            for (ListTvShows element: lists) {
                if (element.getTvShowAdded().getId().equals(tvShowsToFind.getIdTvShow()))
                    listsName.add(element.getType());
            }

            return listsName;

        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't find the list")
                    .errors(Collections.singletonList("error.find_in_list.no_user"))
                    .build();
        }
    }

    private List<ListTvShowsDetailsDto> firstFourTvShows(List<ListTvShowsDetailsDto> fullList, ListType type) {
        return fullList.
                stream()
                .filter(e -> e.getType().equals(type))
                .limit(4)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ListTvShowsDetailsDto> findTvShowsInListForUser(ListTvShowsIdsDto tvShowsToFind, boolean firstFour) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(tvShowsToFind.getIdUser()).orElseThrow();
            List<ListTvShows> listUser = listTvShowsRepository.findAllByMyUser(user);
            List<ListTvShows> listUserType;
            if (tvShowsToFind.getType() != null) {
                listUserType = listUser
                        .stream()
                        .filter(e -> e.getType().equals(tvShowsToFind.getType()))
                        .collect(Collectors.toList());
            } else {
                listUserType = listUser;
            }

            List<ListTvShowsDetailsDto> listUserDto = new LinkedList<>();
            for (ListTvShows element: listUserType) {
                listUserDto.add(0, ListTvShowsDetailsDto
                        .builder()
                        .type(element.getType())
                        .tvShowInfoDto(TvShowMapper.mapModelToInfoDto(element.getTvShowAdded()))
                        .build());
            }

            List<ListTvShowsDetailsDto> finalList;
            if (firstFour) {
                List<ListTvShowsDetailsDto> recently = firstFourTvShows(listUserDto, ListType.RECENTLY_WATCHED);
                List<ListTvShowsDetailsDto> favorites = firstFourTvShows(listUserDto, ListType.FAVORITES);
                List<ListTvShowsDetailsDto> toWatch = firstFourTvShows(listUserDto, ListType.TO_WATCH);
                List<ListTvShowsDetailsDto> upToDate = firstFourTvShows(listUserDto, ListType.UP_TO_DATE);
                List<ListTvShowsDetailsDto> stopped = firstFourTvShows(listUserDto, ListType.STOPPED);
                finalList = Stream.of(recently, favorites, toWatch, upToDate, stopped)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            } else {
                finalList = listUserDto;
            }

            return finalList;
        } catch (Exception e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't find the list")
                    .errors(Collections.singletonList("error.find_in_list.no_user"))
                    .build();
        }
    }

    @Override
    @Transactional
    public ListTvShowsDto addTvShowToList(ListTvShowsIdsDto newTvShowToList) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(newTvShowToList.getIdUser()).orElseThrow();
            TvShow tvShow = tvShowRepository.findById(newTvShowToList.getIdTvShow()).orElseThrow();
            ListTvShows listTvShows = ListTvShows.builder()
                    .type(newTvShowToList.getType())
                    .myUser(user)
                    .tvShowAdded(tvShow)
                    .build();
            ListTvShows listTvShowsSaved = listTvShowsRepository.save(listTvShows);
            return ListTvShowMapper.modelToDto(listTvShowsSaved);
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't find the list")
                    .errors(Collections.singletonList("error.find_in_list.no_user"))
                    .build();
        }
    }

    @Override
    @Transactional
    public ListTvShowsDto removeTvShowFromList(ListTvShowsIdsDto tvShowToRemove) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(tvShowToRemove.getIdUser()).orElseThrow();

            List<ListTvShows> listUser = listTvShowsRepository.findAllByMyUser(user)
                    .stream()
                    .filter(e -> e.getTvShowAdded().getId().equals(tvShowToRemove.getIdTvShow()))
                    .collect(Collectors.toList());

            for (ListTvShows element: listUser) {
                if (element.getType().equals(tvShowToRemove.getType())) {
                    listTvShowsRepository.delete(element);
                    break;
                }
            }
            return null;
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't remove from list")
                    .errors(Collections.singletonList("error.find_in_list.no_user"))
                    .build();
        }
    }

    @Override
    @Transactional
    public ListTvShowsDto addTvShowToRecentlyWatched(ListTvShowsIdsDto newTvShowToList) throws ApiExceptionResponse {
        try {
            MyUser user = myUserRepository.findById(newTvShowToList.getIdUser()).orElseThrow();
            List<ListTvShows> listRecently = listTvShowsRepository.findAllByMyUser(user).stream()
                    .filter(e -> e.getType().equals(ListType.RECENTLY_WATCHED))
                    .collect(Collectors.toList());

            ListTvShows listTvShows = null;
            for (ListTvShows e: listRecently) {
                if (e.getTvShowAdded().getId().equals(newTvShowToList.getIdTvShow())) {
                    listTvShows = e;
                    break;
                }
            }
            if (listTvShows != null) {
                listTvShowsRepository.delete(listTvShows);
            } else if (listRecently.size() == 4) {
                listTvShowsRepository.delete(listRecently.get(0));
            }
            TvShow tvShow = tvShowRepository.findById(newTvShowToList.getIdTvShow()).orElseThrow();
            listTvShows = ListTvShows.builder()
                    .type(newTvShowToList.getType())
                    .myUser(user)
                    .tvShowAdded(tvShow)
                    .build();
            listTvShowsRepository.save(listTvShows);
            return new ListTvShowsDto();
        } catch (NoSuchElementException e) {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Couldn't add to list")
                    .errors(Collections.singletonList("error.find_in_list.no_user"))
                    .build();
        }
    }
}
