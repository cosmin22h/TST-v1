import React, { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import backend from "../../apis/backend";

const ViewEpisodes = () => {
  const { id } = useParams();
  const [episodes, setEpisodes] = useState([]);

  useEffect(() => {
    const getEpisodes = async () => {
      try {
        const response = await backend.get(`/episode/episodes/tv-show/${id}`);
        setEpisodes(response.data);
      } catch (error) {
        console.log(error.response);
      }
    };

    getEpisodes();
  }, [id]);
  const renderedList = () => {
    if (episodes.length === 0) return null;

    return episodes.map((ep) => {
      return (
        <tr key={ep.id}>
          <td>{ep.noSeason}</td>
          <td>{ep.episodeNumber}</td>
          <td>
            <Link to={`/admin/tvshows/show/${id}/episode/${ep.id}`}>
              {ep.name}
            </Link>
          </td>
          <td>{ep.airDate}</td>
          <td>{ep.rating}</td>
        </tr>
      );
    });
  };

  return (
    <div>
      <Link to={`/admin/tvshows/show/${id}/add`}>
        <div className="ui green button">
          Add new Episode
        </div>
      </Link>
      <table className="ui celled padded table">
        <thead>
          <tr>
            <th>Season</th>
            <th>No.</th>
            <th>Name</th>
            <th>Air Date</th>
            <th>Rating</th>
          </tr>
        </thead>
        <tbody>{renderedList()}</tbody>
      </table>
      <br />
    </div>
  );
};

export default ViewEpisodes;
