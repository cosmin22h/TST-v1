import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import backend from "../apis/backend";
import { makeStyles } from "@material-ui/core/styles";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import GridListTileBar from "@material-ui/core/GridListTileBar";
import Container from "@material-ui/core/Container";
import IconButton from "@material-ui/core/IconButton";
import VisibilityIcon from '@material-ui/icons/Visibility';

import LinkClassic from './links/LinkClassic';

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    justifyContent: "space-around",
    overflow: "hidden",
  },
  gridList: {
    position: "absolute",
    width: "90%",
    top: "12%",
  },
  icon: {
    color: "#fff",
  },
}));

const ListTvShows = ({ tvShows }) => {
  const classes = useStyles();
  const [tvShowsList, setTvShowsList] = useState([]);
  const { type, id } = useParams();

  useEffect(() => {
    const getTvShows = async () => {
      let typeList;
      if (type === '"recently-watched') typeList = 'RECENTLY_WATCHED'; 
      else if (type === 'favorites') typeList = 'FAVORITES';
      else if (type === 'to-watch') typeList = 'TO_WATCH';
      else if (type === 'stopped') typeList = 'STOPPED';
      const response = await backend.get(`/lists/all-tv-show/${typeList}/${id}`);
      const data = response.data;
      let list = [];
      for (var i = 0; i < data.length; i++) {
        list.push(data[i].tvShowInfoDto);
      }
      setTvShowsList(list);
    };
    if (tvShows !== undefined) {
      setTvShowsList(tvShows);
    } else {
      getTvShows();
    }
  }, [tvShows, type, id]);

  return (
    <React.Fragment>
      <Container className={classes.root}>
        <GridList
          cellHeight={300}
          className={classes.gridList}
          cols={6}
          spacing={20}
        >
          {tvShowsList.map((tvShow) => (
            <GridListTile key={tvShow.id}>
              <img
                src={tvShow.posterPath}
                alt={tvShow.id}
                height="280"
                width="215"
              />
              <GridListTileBar
                title={tvShow.name}
                subtitle={tvShow.noOfSeasons + " seasons / " + tvShow.status}
                actionIcon={
                  <LinkClassic href={`/tv-show/${tvShow.id}`}>
                  <IconButton
                    className={classes.icon}
                  >
                    <VisibilityIcon />
                  </IconButton>
                  </LinkClassic>
                }
              />
            </GridListTile>
          ))}
        </GridList>
      </Container>
    </React.Fragment>
  );
};

export default ListTvShows;
