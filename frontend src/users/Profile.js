import React, { useState, useEffect } from "react";
import { useHistory } from "react-router";
import backend from "../apis/backend";
import Friends from "./Friends";

import {
  Avatar,
  Box,
  Grid,
  Typography,
  Paper,
  Button,
  IconButton,
  Modal,
} from "@material-ui/core";
import PeopleAltIcon from "@material-ui/icons/PeopleAlt";
import { makeStyles } from "@material-ui/core/styles";
import List4TvShows from "./tvshows/List4TvShow";
import ModalOrg from "../components/ModalOrg";
import AvatarProfile from "./AvatarProfile";

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
}));

const Profile = () => {
  const classes = useStyles();
  const [user, setUser] = useState({
    displayName: "",
  });
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
  const [open, setOpen] = useState(false);
  const [openAvatar, setOpenAvatar] = useState(false);
  const history = useHistory();

  const id = localStorage.getItem("ID session");

  
  useEffect(() => {
    const getUser = async () => {
      await backend
        .get(`/user/profile/${id}`)
        .then((res) => {
          setUser(res.data);
        })
        .catch((err) => {
          console.log(err.response);
        });
    };
  
    const getLists = async () => {
      await backend
        .get(`/lists/only-four-tv-show/${id}`)
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
    const getMetrics = async () => {
      await backend
        .get(`/episode-viewed/get-metrics/${id}`)
        .then((res) => setMetrics(res.data))
        .catch((err) => console.log(err));
    };
    getUser();
    getLists();
    getMetrics();
  }, [id]);

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
                    onClick={() => history.push(`/list/${list.value}/${id}`)}
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
    <div className={classes.root}>
      <Grid container spacing={3}>
        <Grid item xs={3}>
          <Paper className={classes.paper}>
            <Box display="flex" alignItems="center">
              <Box>
                {" "}
                <IconButton onClick={() => setOpenAvatar(true)}>
                  <Avatar
                    alt=""
                    src={`http://localhost:8080/TST/user/avatar/${id}`}
                    className={classes.large}
                  />
                </IconButton>
              </Box>
              <Box width="100%">
                <Typography
                  className={classes.text}
                  component="h5"
                  variant="h5"
                  style={{ textAlign: "left" }}
                >
                  {" "}
                  {user.displayName}
                </Typography>
              </Box>
            </Box>
          </Paper>
        </Grid>
        <Grid item xs={3}>
          <Paper className={classes.paper}>
            <Box display="flex" alignItems="center">
              <Box width="100%" margin={1.4}>
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
              {metrics.months} months {metrics.days} days {metrics.hours} hours
            </Typography>
          </Paper>
        </Grid>
        <Grid item xs={3}>
          <Paper className={classes.paper}>
            <Box display="flex" alignItems="center">
              <Box width="100%" margin={1.4}>
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
              <Box margin={1.4}>
                {" "}
                <IconButton
                  onClick={() => setOpen(true)}
                  style={{ color: "#fff" }}
                >
                  {" "}
                  <PeopleAltIcon fontSize="large" />
                </IconButton>
              </Box>
              <Box width="100%">
                <Typography
                  className={classes.text}
                  component="h5"
                  variant="h5"
                  style={{ textAlign: "left" }}
                >
                  {" "}
                  Friends
                </Typography>
              </Box>
            </Box>
          </Paper>
        </Grid>
        {renderLists()}
      </Grid>
      {open ? (
        <ModalOrg
          open={open}
          onClose={() => setOpen(false)}
          children={<Friends viewFriends={true}/>}
        />
      ) : null}
      {openAvatar ? (
        <Modal
          open={openAvatar}
          onClose={() => setOpenAvatar(false)}
          style={{ position: "absolute", top: "20%", left: "40%" }}
        >
          <div>
            <AvatarProfile />
          </div>
        </Modal>
      ) : null}
    </div>
  );
};

export default Profile;
