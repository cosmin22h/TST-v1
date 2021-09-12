import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import GridListTileBar from "@material-ui/core/GridListTileBar";
import Container from "@material-ui/core/Container";
import IconButton from "@material-ui/core/IconButton";
import VisibilityIcon from '@material-ui/icons/Visibility';

import LinkClassic from  '../../components/links/LinkClassic';

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    justify: "flex-end",
    overflow: "hidden",
  },
  icon: {
    color: "#fff",
  },
}));

const List4TvShows = ({ tvShows }) => {
  const classes = useStyles();

  return (
    <React.Fragment>
      <Container className={classes.root}>
        <GridList
          cellHeight={300}
          className={classes.gridList}
          cols={tvShows.length}
          spacing={20}
        >
          {tvShows.map((tvShow) => (
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

export default List4TvShows;
