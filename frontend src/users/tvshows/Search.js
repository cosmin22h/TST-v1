import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import backend from "../../apis/backend";

import ListTvShows from '../../components/ListTvShows';

const Search = () => {
    const { term } = useParams();
    const [tvShows, setTvShows] = useState([]);

    useEffect(() => {
        const getMatchs = async () => {
            await backend.get(`/tv-show/search/${term}`)
            .then(res => {
                setTvShows(res.data);
            })
            .catch(err => {
                console.log(err.response);
            });            
        };
        getMatchs();
    }, [term]);

    return <ListTvShows tvShows={tvShows}/>;
};

export default Search;