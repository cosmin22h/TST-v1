import React, { useState, useEffect } from "react";
import backend from "../../apis/backend";

import ListTvShows from "../../components/ListTvShows";

const ExploreTvShows = () => {
  const [tvShows, setTvShows] = useState([]);

  useEffect(() => {
    const getTvShows = async () => {
      await backend.get("/tv-show/info")
      .then(res => {
        setTvShows(res.data);
      })
      .catch(err => {
        console.log(err.response);
      });      
    };
    getTvShows();
  }, []);

  return <ListTvShows tvShows={tvShows} />
};

export default ExploreTvShows;
