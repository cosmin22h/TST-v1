import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import backend from "../../apis/backend";

const ViewTvShows = () => {
  const [tvShows, setTvShows] = useState([]);

  const getTvShows = async () => {
    try {
      const response = await backend.get("/tv-show/view");
      setTvShows(response.data);
    } catch (error) {
      console.log(error.response);
    }
  };

  useEffect(() => {
    getTvShows();
  }, []);

  const renderedList = () => {
    if (tvShows.length === 0) return null;

    return tvShows.map((show) => {
      const status =
        show.status === "Returning Series" ? "warning" : "positive";
      return (
        <tr key={show.id}>
          <td>
            <Link to={`/admin/tvshows/show/${show.id}`}>{show.name}</Link>
          </td>
          <td>{show.tmdbId}</td>
          <td>{show.noOfSeasons}</td>
          <td>{show.noOfEpisodes}</td>
          <td className={status}>
            {show.nextAirDate !== null ? (
              <i className="attention icon"></i>
            ) : null}
            {show.status}
          </td>
          <td>{show.nextAirDate}</td>
        </tr>
      );
    });
  };

  return (
    <React.Fragment>
    <table className="ui celled padded table">
      <thead>
        <tr>
          <th>Name</th>
          <th>TMDB ID</th>
          <th>No. Seasons</th>
          <th>No. Episodes</th>
          <th>Status</th>
          <th>Next Episode</th>
        </tr>
      </thead>
      <tbody>{renderedList()}</tbody>
    </table>
    <br/>
    </React.Fragment>
  );
};

export default ViewTvShows;
