import React from "react";
import { makeStyles, CardContent, Typography } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
  },
  details: {
    display: "flex",
    flexDirection: "column",
  },
  content: {
    flex: "1 0 auto",
  },
  controls: {
    maxWidth: "400px",
    display: "flex",
    alignItems: "center",
    paddingLeft: theme.spacing(2),
    paddingBottom: theme.spacing(1),
  },
}));

const TvShowInfo = ({ tvshow }) => {
  const classes = useStyles();

  return (
    <div className={classes.details}>
      <CardContent className={classes.content}>
        <Typography component="h5" variant="h5">
          {tvshow.name} ({tvshow.noOfSeasons} seasons / {tvshow.noOfEpisodes}{" "}
          episodes)
        </Typography>
        <Typography variant="subtitle1" color="textSecondary">
          {tvshow.genres.join(", ")}
        </Typography>
      </CardContent>
      <div className={classes.controls}>{tvshow.overview}</div>
    </div>
  );
};

export default TvShowInfo;
