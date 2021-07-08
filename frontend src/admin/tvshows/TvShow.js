import React, { useState, useEffect } from "react";
import { Link, Redirect, useParams } from "react-router-dom";
import backend from "../../apis/backend";

const status = [
  {
    label: "Returning Series",
    value: "Returning Series",
  },
  {
    label: "Ended",
    value: "Ended",
  },
];

const TvShow = () => {
  const { id } = useParams();
  const [isDelele, setIsDelete] = useState(false);
  const [tvShow, setTvShow] = useState({
    id: "",
    tmdbId: "",
    name: "",
    genres: "",
    posterPath: "",
    noOfSeasons: "",
    noOfEpisodes: "",
    status: "",
    overview: "",
    networks: "",
    episodeRunTime: "",
    firstAirDate: "",
    lastAirDate: "",
    nextAirDate: "",
    rating: "",
  });

  useEffect(() => {
    const getTvShow = async () => {
      try {
        const response = await backend.get(`/tv-show/${id}`);
        setTvShow(response.data);
      } catch (error) {
        if (error.response.data.status === "NOT_FOUND") {
          setIsDelete(true);
        }
      }
    };
    getTvShow();
  }, [id]);

  const onChangeForm = (event) => {
    const target = event.target;
    const name = target.name;
    var value;
    if (name === "genres" || name === "networks") {
      value = target.value.split(",");
    } else {
      value = target.value;
    }
    setTvShow({
      ...tvShow,
      [name]: value,
    });
  };

  const renderedStatus = () => {
    return status.map((s) => {
      return (
        <option key={s.value} value={s.value}>
          {s.label}
        </option>
      );
    });
  };

  const onFormSubmitUpdate = async () => {
    if (tvShow.name === "") {
      alert("Field 'Name' missing");
      return;
    }
    try {
      await backend.put(`/tv-show/${tvShow.id}/edit`, tvShow);
    } catch (error) {
      console.log(error.response);
    }
  };
  const onFormSubmitDelete = async () => {
    try {
      await backend.delete(`/tv-show/${tvShow.id}/delete`);
    } catch (error) {
      console.log(error.response);
    }
  };

  return (
    <React.Fragment>
      {isDelele ? (
        <Redirect to="/admin/tvshows/view" />
      ) : (
        <form className="ui form">
          <div className="field">
            <label>TMDB ID</label>
            <input
              type="text"
              className="form-control"
              id="tmdbId"
              name="tmdbId"
              value={tvShow.tmdbId}
              onChange={onChangeForm}
              readOnly
            />
          </div>
          <div className="field">
            <label>Name</label>
            <input
              type="text"
              className="form-control"
              id="name"
              name="name"
              value={tvShow.name}
              onChange={onChangeForm}
              required
            />
          </div>
          <div className="field">
            <label>Genres</label>
            <input
              type="text"
              className="form-control"
              id="genres"
              name="genres"
              value={tvShow.genres}
              onChange={onChangeForm}
            />
          </div>
          <div className="field">
            <label>Poster path</label>
            <input
              type="text"
              className="form-control"
              id="posterPath"
              name="posterPath"
              value={tvShow.posterPath}
              onChange={onChangeForm}
            />
          </div>
          <div className="fields">
            <div className="six wide field">
              <label>No. of seasons</label>
              <input
                type="number"
                min="0"
                className="form-control"
                id="noOfSeasons"
                name="noOfSeasons"
                value={tvShow.noOfSeasons}
                onChange={onChangeForm}
              />
            </div>
            <div className="six wide field">
              <label>No. of episodes</label>
              <input
                type="number"
                min="0"
                className="form-control"
                id="noOfEpisodes"
                name="noOfEpisodes"
                value={tvShow.noOfEpisodes}
                onChange={onChangeForm}
              />
            </div>
            <div className="six wide field">
              <label>Status</label>
              <select
                className="ui dropdown"
                name="status"
                value={tvShow.status}
                onChange={onChangeForm}
              >
                {renderedStatus()}
              </select>
            </div>
          </div>
          <div className="field">
            <label>
              Overview
              <textarea
                id="overview"
                name="overview"
                value={tvShow.overview}
                onChange={onChangeForm}
              />
            </label>
          </div>
          <div className="fields">
            <div className="eight wide field">
              <label>Rating</label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="5"
                className="form-control"
                id="rating"
                name="rating"
                value={tvShow.rating}
                readOnly
              />
            </div>
            <div className="eight wide field">
              <label>Network</label>
              <input
                type="text"
                className="form-control"
                id="networks"
                name="networks"
                value={tvShow.networks}
                onChange={onChangeForm}
              />
            </div>
          </div>
          <div className="fields">
            <div className="six wide field">
              <label>Episode Run Time</label>
              <input
                type="text"
                className="form-control"
                id="episodeRunTime"
                name="episodeRunTime"
                value={tvShow.episodeRunTime}
                onChange={onChangeForm}
              />
            </div>
            <div className="six wide field">
              <label>First Air Date</label>
              <input
                type="date"
                className="form-control"
                id="firstAirDate"
                name="firstAirDate"
                value={tvShow.firstAirDate}
                onChange={onChangeForm}
              />
            </div>
            <div className="six wide field">
              <label>Next Air Date</label>
              <input
                type={tvShow.status === status[1].value ? "text" : "date"}
                className="form-control"
                id="nextAirDate"
                name="nextAirDate"
                value={tvShow.nextAirDate === null ? "" : tvShow.nextAirDate}
                onChange={onChangeForm}
                readOnly={tvShow.status === status[1].value ? true : false}
              />
            </div>
          </div>
          <div className="ui buttons">
            <button type="submit" className="ui positive button" onClick={onFormSubmitUpdate}>
              Save
            </button>
            <div className="or" />
            <button className="ui negative button" onClick={onFormSubmitDelete}>
              Delete
            </button>
          </div>
          <Link to={`/admin/tvshows/show/${tvShow.id}/episodes`}>
            <button type="submit" className="ui right floated button">
              View Episodes
            </button>
          </Link>
          <br />
          <br />
        </form>
      )}
    </React.Fragment>
  );
};

export default TvShow;
