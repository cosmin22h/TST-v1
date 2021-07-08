package com.PSproject.TvShowsTracker.mapper.tvshow;

import com.PSproject.TvShowsTracker.dto.tvshow.EpisodeDto;
import com.PSproject.TvShowsTracker.model.tvshow.Episode;

public class EpisodeMapper {
    public static EpisodeDto mapModelToDto(Episode episode, boolean noTvShow) {
        EpisodeDto episodeDto = new EpisodeDto();
        episodeDto.setId(episode.getId());
        episodeDto.setNoSeason(episode.getNoSeason());
        episodeDto.setEpisodeNumber(episode.getEpisodeNumber());
        episodeDto.setName(episode.getName());
        episodeDto.setAirDate(episode.getAirDate());
        episodeDto.setOverview(episode.getOverview());
        episodeDto.setStillPath(episode.getStillPath());
        episodeDto.setRating(episode.getRating());
        if (!noTvShow) episodeDto.setTvShow(TvShowMapper.mapModelToInfoDto(episode.getTvShow()));

        return episodeDto;
    }

    public static Episode mapDtoToModel(EpisodeDto episodeDto) {
        Episode episode = new Episode();
        if (episodeDto.getId() != null) episode.setId(episodeDto.getId());
        episode.setNoSeason(episodeDto.getNoSeason());
        episode.setEpisodeNumber(episodeDto.getEpisodeNumber());
        episode.setName(episodeDto.getName());
        episode.setAirDate(episodeDto.getAirDate());
        episode.setOverview(episodeDto.getOverview());
        episode.setStillPath(episodeDto.getStillPath());
        episode.setRating(episodeDto.getRating());

        return episode;
    }

}
