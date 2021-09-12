import React, { useState } from "react";
import tmdb from "../../apis/tmdb";
import SearchBar from "../../components/SearchBar";
import PopupTV from "./PopupTV";
import backend from "../../apis/backend";

const KEY = "340e6dcf860d1c30d2dbdf83219935f8";
const URL_PHOTOS = "https://www.themoviedb.org/t/p/original";
const MSGS = ["IN DATABASE", "NOT IN DATABASE"];

const AddTvShows = () => {
  const [popout, setPopout] = useState(false);
  const [tvshow, setTvShow] = useState({
    tmdbId: 0,
    name: "",
    genres: [],
    posterPath: "",
    noOfSeasons: 0,
    noOfEpisodes: 0,
    status: "",
    overview: "",
    networks: [],
    episodeRunTime: 0,
    firstAirDate: "",
    lastAirDate: "",
    nextAirDate: "",
  });
  const [episodes, setEpisodes] = useState(null);

  const [inDatabase, setInDatabase] = useState({
    isIn: false,
    msg: MSGS[1],
  });

  const onSearchSubmit = async (idTv) => {
    setInDatabase({
      isIn: false,
      msg: MSGS[1],
    });

    try {
      const responseInDatabse = await backend.get(`/tv-show/add/check/${idTv}`);
      setInDatabase({
        isIn: true,
        msg: MSGS[0],
      });
      setTvShow(responseInDatabse.data);
    } catch (error) {
      if (error.response.data.status === "NOT_FOUND") {
        try {
          const responseTvShows = await tmdb.get(`/tv/${idTv}?api_key=${KEY}`);
          const episodesToBackend = [];

          for (var i = 1; i <= responseTvShows.data.number_of_seasons; i++) {
            try {
              const season = await tmdb.get(
                `/tv/${idTv}/season/${i}?api_key=${KEY}`
              );
              for (var j = 0; j < season.data.episodes.length; j++) {
                var epData = season.data.episodes[j];
                episodesToBackend.push({
                  id: null,
                  noSeason: epData.season_number,
                  episodeNumber: epData.episode_number,
                  name: epData.name,
                  airDate: epData.air_date,
                  overview: epData.overview,
                  stillPath: `${URL_PHOTOS}${epData.still_path}`,
                });
              }
            } catch (error) {
              console.log(error.response);
            }
          }

          const dataTvShow = responseTvShows.data;

          setTvShow({
            tmdbId: dataTvShow.id,
            name: dataTvShow.name,
            genres: dataTvShow.genres.map((genre) => {
              return genre.name;
            }),
            posterPath: `${URL_PHOTOS}/${dataTvShow.poster_path}`,
            noOfSeasons: dataTvShow.number_of_seasons,
            noOfEpisodes: dataTvShow.number_of_episodes,
            status: dataTvShow.status,
            overview: dataTvShow.overview,
            networks: dataTvShow.networks.map((network) => {
              return network.name;
            }),
            episodeRunTime: Math.max(...dataTvShow.episode_run_time),
            firstAirDate: dataTvShow.first_air_date,
            lastAirDate: dataTvShow.last_air_date,
            nextAirDate:
              dataTvShow.next_episode_to_air !== null
                ? dataTvShow.next_episode_to_air.air_date
                : null,
          });
          setEpisodes(episodesToBackend);
        } catch (error) {
          console.log(error.response);
        }
      } else {
        console.log(error.response);
      }
    }
    setPopout(true);
  };

  const togglePopout = () => {
    setPopout(!popout);
  };

  const addTvShow = async () => {
    try {
      await backend.post("/tv-show/add", tvshow);
      await backend.post(`/episode/add/tv-show/${tvshow.tmdbId}`, episodes);
    } catch (error) {
      console.log(error.response);
    }
    setPopout(false);
  };

  return (
    <div>
      <SearchBar
        labelText="Add TV Show (Enter the ID - TMDB)"
        onFormSubmit={onSearchSubmit}
      />
      {popout ? (
        <PopupTV
          image={tvshow.posterPath}
          header={`${tvshow.name} (First air date: ${tvshow.firstAirDate})`}
          content={tvshow.overview}
          extrabtn={inDatabase.isIn}
          msg={inDatabase.msg}
          togglePopout={togglePopout}
          onAddEvent={addTvShow}
        />
      ) : null}
    </div>
  );
};

export default AddTvShows;
