import React, { useState, useEffect } from "react";
import { useParams, useHistory } from "react-router-dom";
import backend from "../apis/backend";
import {
  Avatar,
  Box,
  Grid,
  Typography,
  Paper,
  Button,
} from "@material-ui/core";
import PeopleAltIcon from "@material-ui/icons/PeopleAlt";
import { makeStyles } from "@material-ui/core/styles";
import List4TvShows from "./tvshows/List4TvShow";
import { Facebook, Instagram, Reddit, Twitter } from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    padding: theme.spacing(3),
  },
  paper: {
    padding: theme.spacing(1),
    textAlign: "center",
    backgroundColor: "#424242",
    color: "#fff",
    border: "1px solid #696969",
  },
  text: {
    fontFamily: "ProximaNova",
    color: "#fff",
    paddingLeft: theme.spacing(1),
  },
  text2: {
    fontFamily: "ProximaNova",
    color: "#fff",
    paddingLeft: theme.spacing(1),
    fontSize: "1.1rem",
  },
  modal: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  tableCell: {
    color: "#fff",
  },
  large: {
    width: theme.spacing(7),
    height: theme.spacing(7),
  },
  textList: {
    color: "#fff",
    paddingLeft: theme.spacing(1),
  },
  button: {
    color: "#fff",
    float: "left",
    fontFamily: "ProximaNova",
    fontSize: "1.2rem",
  },
  social: {
    color: "white",
    alignItems: "center",
    flexWrap: "wrap",
    marginTop: theme.spacing(1),
  },
}));

const friendStatus = [
  "Add friend",
  "Unfriend",
  "Request sent",
  "Accept friend",
];

const FriendProfile = () => {
  const { term } = useParams();
  const classes = useStyles();
  const [friend, setFriend] = useState({
    displayName: "",
  });
  const [isFriend, setIsFriend] = useState(friendStatus[0]);
  const [recently, setRecently] = useState([]);
  const [favorites, setFavorites] = useState([]);
  const [toWatch, setToWatch] = useState([]);
  const [upToDate, setUpToDate] = useState([]);
  const [stopped, setStopped] = useState([]);
  const [metrics, setMetrics] = useState({
    hours: 0,
    days: 0,
    months: 0,
    episodesWatched: 0,
  });
  const id = localStorage.getItem("ID session");
  const history = useHistory();

  useEffect(() => {
    const getUser = async () => {
      await backend
        .get(`/user/${id}/get-user/${term}`)
        .then((res) => {
          const data = res.data;
          if (data.id.toString() === localStorage.getItem("ID session")) {
            window.location.assign("/");
          }
          setFriend(data);
          checkFriend(data.id);
          getMetrics(data.id);
        })
        .catch(() => {
          window.location.assign("/");
        });
    };
    const checkIsAccepted = async (friendId) => {
      await backend
        .get(`/friends/friend/${friendId}/${id}`)
        .then((res) => {
          if (res.data !== "") {
            setIsFriend(friendStatus[3]);
          } 
        })
        .catch((err) => console.log(err.response));
    };
    const checkFriend = async (friendId) => {
      await backend
        .get(`/friends/friend/${id}/${friendId}`)
        .then((res) => {
          if (res.data !== "") {
            if (res.data.isAccepted) {
              setIsFriend(friendStatus[1]);
              getLists(friendId);
            } else {
              setIsFriend(friendStatus[2]);
            }
          } else {
            checkIsAccepted(friendId);
          }
        })
        .catch((err) => console.log(err.response));
    };
    const getMetrics = async (friendId) => {
      await backend
        .get(`/episode-viewed/get-metrics/${friendId}`)
        .then((res) => setMetrics(res.data))
        .catch((err) => console.log(err));
    };
    const getLists = async (friendId) => {
      await backend
        .get(`/lists/only-four-tv-show/${friendId}`)
        .then((res) => {
          const data = res.data;
          let listRecently = [];
          let listFavorites = [];
          let listToWatch = [];
          let listUpToDate = [];
          let listStopped = [];
          for (let i = 0; i < data.length; i++) {
            if (data[i].type === "RECENTLY_WATCHED") {
              listRecently.push(data[i].tvShowInfoDto);
            }
            if (data[i].type === "FAVORITES") {
              listFavorites.push(data[i].tvShowInfoDto);
            }
            if (data[i].type === "TO_WATCH") {
              listToWatch.push(data[i].tvShowInfoDto);
            }
            if (data[i].type === "UP_TO_DATE") {
              listUpToDate.push(data[i].tvShowInfoDto);
            }
            if (data[i].type === "STOPPED") {
              listStopped.push(data[i].tvShowInfoDto);
            }
          }
          setRecently(listRecently);
          setFavorites(listFavorites);
          setToWatch(listToWatch);
          setUpToDate(listUpToDate);
          setStopped(listStopped);
        })
        .catch((err) => {
          console.log(err.response);
        });
    };
    getUser();
  }, [term, id]);

  const onClickAddFriend = () => {
    const sentRequest = async () => {
      await backend
        .post(`/friends/request/${id}/${friend.id}`)
        .catch((err) => console.log(err.response));
    };
    sentRequest();
    window.location.reload();
  };

  const onClickUnfriend = () => {
    const unfriendRequest = async () => {
      await backend
        .delete(`/friends/unfriend/${id}/${friend.id}`)
        .catch((err) => console.log(err.response));
    };
    unfriendRequest();
    window.location.reload();
  };

  const onClickAccept = () => {
    const confirmFriend = async () => {
      let data = {
        idFriend: friend.id,
        idUser: id,
      };
      await backend
        .put("/friends/accept", data)
        .catch((err) => console.log(err));
    };
    confirmFriend();
    window.location.reload();
  };

  const renderLists = () => {
    const lists = [
      {
        label: "RECENTLY WATCHED",
        value: "recently-watched",
        tvShows: recently,
      },
      {
        label: "FAVORITES ",
        value: "favorites",
        tvShows: favorites,
      },
      {
        label: "TO WATCH",
        value: "to-watch",
        tvShows: toWatch,
      },
      {
        label: "UP TO DATE / FINISHED",
        value: "up-to-date",
        tvShows: upToDate,
      },
      {
        label: "STOPPED",
        value: "stopped",
        tvShows: stopped,
      },
    ];
    return lists.map((list) => {
      return (
        <Grid item xs={12} key={list.label}>
          <Paper className={classes.paper}>
            <Box display="flex" alignItems="center">
              <Box margin={1}>
                <Typography style={{ textAlign: "left", minWidth: "150px" }}>
                  {list.label}
                </Typography>
              </Box>
              {list.tvShows.length !== 0 &&
              list.value !== "recently-watched" ? (
                <Box width="100%">
                  <Button
                    onClick={() =>
                      history.push(`/list/${list.value}/${friend.id}`)
                    }
                    style={{ color: "#fff", float: "right" }}
                  >
                    See all
                  </Button>
                </Box>
              ) : null}
            </Box>
            <Box width="100%">
              <List4TvShows tvShows={list.tvShows} />
            </Box>
          </Paper>
        </Grid>
      );
    });
  };

  return (
    <React.Fragment>
      <div className={classes.root}>
        <Grid container spacing={3}>
          <Grid item xs={3}>
            <Paper className={classes.paper}>
              <Box display="flex" alignItems="center">
                <Box margin={1.2}>
                  {" "}
                  <Avatar
                    className={classes.large}
                    src={`http://localhost:8080/TST/user/avatar/${friend.id}`}
                  />
                </Box>
                <Box width="100%">
                  <Typography
                    className={classes.text}
                    component="h5"
                    variant="h5"
                    style={{ textAlign: "left" }}
                  >
                    {" "}
                    {friend.displayName}
                  </Typography>
                </Box>
              </Box>
            </Paper>
          </Grid>
          <Grid item xs={3}>
            <Paper className={classes.paper}>
              <Box display="flex" alignItems="center">
                <Box width="100%" margin={1.2}>
                  <Typography
                    className={classes.text}
                    component="h5"
                    variant="h5"
                    style={{ textAlign: "center" }}
                  >
                    {" "}
                    My TV time
                  </Typography>
                </Box>
              </Box>
              <Typography
                className={classes.text}
                variant="subtitle1"
                style={{ textAlign: "center", fontSize: "1.2rem" }}
              >
                {" "}
                {metrics.months} months {metrics.days} days {metrics.hours}{" "}
                hours
              </Typography>
            </Paper>
          </Grid>
          <Grid item xs={3}>
            <Paper className={classes.paper}>
              <Box display="flex" alignItems="center">
                <Box width="100%" margin={1.2}>
                  <Typography
                    className={classes.text}
                    component="h5"
                    variant="h5"
                    style={{ textAlign: "center" }}
                  >
                    {" "}
                    Episodes watched
                  </Typography>
                </Box>
              </Box>
              <Typography
                className={classes.text}
                variant="subtitle1"
                style={{ textAlign: "center", fontSize: "1.2rem" }}
              >
                {" "}
                {metrics.episodesWatched}
              </Typography>
            </Paper>
          </Grid>
          <Grid item xs={3}>
            <Paper className={classes.paper}>
              <Box display="flex" alignItems="center">
                <Box margin={2.4}>
                  {" "}
                  <PeopleAltIcon fontSize="large" />
                </Box>
                <Box width="100%">
                  {isFriend === friendStatus[0] ? (
                    <Button
                      className={classes.button}
                      onClick={onClickAddFriend}
                    >
                      {friendStatus[0]}
                    </Button>
                  ) : isFriend === friendStatus[1] ? (
                    <Button
                      className={classes.button}
                      onClick={onClickUnfriend}
                    >
                      {friendStatus[1]}
                    </Button>
                  ) : isFriend === friendStatus[2] ? (
                    <Typography
                      className={classes.text}
                      component="h5"
                      variant="h5"
                      style={{ textAlign: "left" }}
                    >
                      {" "}
                      {friendStatus[2]}
                    </Typography>
                  ) : (
                    <Button className={classes.button} onClick={onClickAccept}>
                      {friendStatus[3]}
                    </Button>
                  )}
                </Box>
              </Box>
            </Paper>
          </Grid>
          {isFriend === friendStatus[1] ? (
            <React.Fragment>
              {friend.gender !== null ||
              friend.country !== null ||
              friend.birthday !== null ||
              friend.about !== null ||
              friend.facebook !== null ||
              friend.instagram !== null ||
              friend.twitter !== null ||
              friend.reddit !== null ? (
                <Grid item xs={12}>
                  <Paper className={classes.paper}>
                    <Typography
                      className={classes.text2}
                      style={{ textAlign: "left" }}
                    >
                      {friend.gender !== null ? "Gender: " : null}
                      {friend.gender}{" "}
                      {friend.gender !== null && friend.country !== null
                        ? "/"
                        : null}{" "}
                      {friend.country !== null ? "Country: " : null}{" "}
                      {friend.country}{" "}
                      {friend.gender !== null && friend.country !== null
                        ? "/"
                        : null}{" "}
                      {friend.birthday !== null ? "Birthday: " : null}
                      {friend.birthday}
                    </Typography>
                    {friend.about !== null ? (
                      <Typography
                        className={classes.text2}
                        style={{
                          paddingTop: "1%",
                          paddingBottom: "1%",
                          textAlign: "left",
                          color: "rgba(255, 255, 255, 0.7)",
                        }}
                      >
                        {friend.about}
                      </Typography>
                    ) : null}
                    {friend.facebook ||
                    friend.instagram ||
                    friend.twitter ||
                    friend.reddit ? (
                      <Grid
                        container
                        direction="row"
                        alignItems="center"
                        spacing={1}
                      >
                        <Grid item>
                          <Typography
                            className={classes.text2}
                            style={{ textAlign: "left" }}
                          >
                            You can find me on:
                          </Typography>
                        </Grid>

                        {friend.facebook ? (
                          <Grid item>
                            <a
                              href={`https://www.facebook.com/${friend.facebook}/`}
                            >
                              <Facebook
                                fontSize="small"
                                className={classes.social}
                              />
                            </a>
                          </Grid>
                        ) : null}
                        {friend.instagram ? (
                          <Grid item>
                            <a
                              href={`https://www.instagram.com/${friend.instagram}/`}
                            >
                              <Instagram
                                fontSize="small"
                                className={classes.social}
                              />
                            </a>
                          </Grid>
                        ) : null}
                        {friend.twitter ? (
                          <Grid item>
                            <a href={`https://twitter.com/${friend.twitter}`}>
                              <Twitter
                                fontSize="small"
                                className={classes.social}
                              />
                            </a>
                          </Grid>
                        ) : null}
                        {friend.reddit ? (
                          <Grid item>
                            <a
                              href={`https://www.reddit.com/user/${friend.reddit}`}
                            >
                              <Reddit
                                fontSize="small"
                                className={classes.social}
                              />
                            </a>
                          </Grid>
                        ) : null}
                      </Grid>
                    ) : null}
                  </Paper>
                </Grid>
              ) : null}
              {renderLists()}
            </React.Fragment>
          ) : null}
        </Grid>
      </div>
    </React.Fragment>
  );
};

export default FriendProfile;
