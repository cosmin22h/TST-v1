import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import backend from "../../apis/backend";

const Episode = () => {
  const { idShow, idEpisode } = useParams();
  const [episode, setEpisode] = useState({
    id: "",
    noSeason: "",
    episodeNumber: "",
    name: "",
    airDate: "",
    overview: "",
    rating: "",
    stillPath: "",
  });

  useEffect(() => {
    const getEpisode = async () => {
      try {
        const response = await backend.get(`/episode/${idEpisode}`);
        setEpisode(response.data);
      } catch (error) {
        console.log(error.response);
      }
    };
    getEpisode();
  }, [idEpisode]);

  const onChangeForm = (event) => {
    const target = event.target;
    const name = target.name;
    const value = target.value;
    setEpisode({ ...episode, [name]: value });
  };

  const onFormSubmit = async () => {
    try {
      await backend.put(`/episode/edit/${episode.id}`, episode);
    } catch (error) {
      console.log(error.response);
    }
  };

  const onFormSubmitDelete = async () => {
    try {
      await backend.delete(`/episode/delete/${episode.id}`);
    } catch (error) {
      console.log(error.response);
    }
  };

  return (
    <div>
      <form className="ui form" onSubmit={onFormSubmit}>
        <Link to={`/admin/tvshows/show/${idShow}/episodes`}>
          <div className="ui button">Back to episodes</div>
        </Link>
        <br />
        <br />
        <div className="fields">
          <div className="eight wide field">
            <label>Name</label>
            <input
              type="text"
              className="form-control"
              id="name"
              name="name"
              value={episode.name}
              onChange={onChangeForm}
              required
            />
          </div>
          <div className="four wide field">
            <label>Season</label>
            <input
              type="number"
              min="0"
              className="form-control"
              id="noSeason"
              name="noSeason"
              value={episode.noSeason}
              onChange={onChangeForm}
            />
          </div>
          <div className="four wide field">
            <label>Episode</label>
            <input
              type="number"
              min="0"
              className="form-control"
              id="episodeNumber"
              name="episodeNumber"
              value={episode.episodeNumber}
              onChange={onChangeForm}
            />
          </div>
        </div>
        <div className="field">
          <label>Poster</label>
          <input
            type="text"
            className="form-control"
            id="stillPath"
            name="stillPath"
            value={episode.stillPath}
            onChange={onChangeForm}
          />
        </div>
        <div className="fields">
          <div className="eight wide field">
            <label>Air date</label>
            <input
              type="date"
              className="form-control"
              id="airDate"
              name="airDate"
              value={episode.airDate}
              onChange={onChangeForm}
            />
          </div>
          <div className="eight wide field">
            <label>Rating</label>
            <input
              type="number"
              min="0"
              step="0.1"
              className="form-control"
              id="rating"
              name="rating"
              value={episode.rating}
              onChange={onChangeForm}
              readOnly
            />
          </div>
        </div>
        <div className="field">
          <label>
            Overview
            <textarea
              id="overview"
              name="overview"
              value={episode.overview}
              onChange={onChangeForm}
            />
          </label>
        </div>
        <div className="ui buttons">
          <button type="submit" className="ui positive button">
            Save
          </button>
          <div className="or" />
          <button className="ui negative button" onClick={onFormSubmitDelete}>
            Delete
          </button>
        </div>
        <Link to={`/admin/tvshows/show/${idShow}/episode/${episode.id}/comments`}>
            <div className="ui right floated button">View comments</div>
          </Link>
      </form>
    </div>
  );
};

export default Episode;
