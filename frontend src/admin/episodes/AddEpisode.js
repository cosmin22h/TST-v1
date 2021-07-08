import React, { useState } from "react";
import { useParams, Link } from "react-router-dom";
import backend from "../../apis/backend";

const AddEpisode = () => {
  const { id } = useParams();
  const [episode, setEpisode] = useState({
    noSeason: "",
    episodeNumber: "",
    name: "",
    airDate: "",
    overview: "",
    stillPath: "",
  });

  const onChangeForm = (event) => {
    const target = event.target;
    const name = target.name;
    const value = target.value;
    setEpisode({ ...episode, [name]: value });
  };

  console.log(id)
  const onFormSubmit = async () => {
    try {
        await backend.post(`/episode/add-one/tv-show/${id}`, episode);
    } catch (error) {
      console.log(error.response);
    }
  };

  return (
    <div>
      <form className="ui form" onSubmit={onFormSubmit}>
        <Link to={`/admin/tvshows/show/${id}/episodes`} className="ui b">
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
        <button type="submit" className="ui fluid positive button">
          Add
        </button>
      </form>
    </div>
  );
};

export default AddEpisode;
