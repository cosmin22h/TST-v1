import React, { useState, useEffect } from "react";
import { useHistory } from "react-router";

import backend from "../apis/backend";
import TvShowInfo from "./TvShowInfo";

import { ThemeProvider } from "@material-ui/styles";
import { CssBaseline, makeStyles, Grid, Paper } from "@material-ui/core";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import GridListTileBar from "@material-ui/core/GridListTileBar";
import IconButton from "@material-ui/core/IconButton";

import darkTheme from "../components/Theme";
import InfoIconOrg from "../components/icons/InfoIconOrg";
import ModalOrg from "../components/ModalOrg";

import Login from "./Login";
import SignUp from "./SignUp";
import ForgetPassword from "./ForgetPassword";
import ChangePassword from "./ChangePassword";

const useStyles = makeStyles((theme) => ({
  root: {
    height: "100vh",
  },
}));

const IndexPage = ({ indexType, typeSelected }) => {
  const history = useHistory();

  const [posters, setPosters] = useState([]);
  const [openInfo, setOpenInfo] = useState(false);
  const [tvShowInfo, setTvShowInfo] = useState(null);
  const classes = useStyles();

  useEffect(() => {
    const getPosters = async () => {
      await backend
        .get("/tv-show/posters")
        .then((res) => setPosters(res.data))
        .catch((err) => console.log(err.response));
    };
    getPosters();
  }, []);

  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline />
      <Grid container component="main" className={classes.root}>
        <Grid item xs={false} sm={2} md={7}>
          <GridList cellHeight={380} cols={4} spacing={0}>
            {posters.map((tile) => (
              <GridListTile key={tile.id}>
                <img src={tile.posterPath} alt={tile.id} />
                <GridListTileBar
                  title={tile.name}
                  actionIcon={
                    <IconButton
                      className={classes.icon}
                      onClick={() => {
                        setTvShowInfo(tile);
                        setOpenInfo(true);
                      }}
                    >
                      <InfoIconOrg />
                    </IconButton>
                  }
                />
              </GridListTile>
            ))}
          </GridList>
        </Grid>
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6}>
          {typeSelected === indexType[0] ? (
            <Login
              signUp={() => {
                history.push("/register");
              }}
              forgetPassword={() => {
                history.push("/forget-password");
              }}
            />
          ) : typeSelected === indexType[1] ? (
            <SignUp
              login={() => {
                history.goBack();
              }}
            />
          ) : typeSelected === indexType[2] ? (
            <ForgetPassword
              forgetPassword={() => {
                history.goBack();
              }}
            />
          ) : (
            <ChangePassword
              changePassword={() => {
                window.location.assign("/");
              }}
            />
          )}
        </Grid>
      </Grid>
      {openInfo === true ? (
        <ModalOrg
          classes={classes}
          open={openInfo}
          onClose={() => setOpenInfo(false)}
          children={<TvShowInfo tvshow={tvShowInfo} />}
        />
      ) : null}
    </ThemeProvider>
  );
};

export default IndexPage;
