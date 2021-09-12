import React, { useState } from "react";
import backend from "../apis/backend";

import { Typography, makeStyles, Grid, Avatar, Box } from "@material-ui/core";

import LinkOrg from "../components/links/LinkOrg";
import Copyright from "../components/Copyright";
import TextFieldOrg from "../components/texts/TextFieldOrg";
import ButtonOrg from "../components/buttons/ButtonOrg";
import DraftsIcon from "@material-ui/icons/Drafts";
import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";

const useStyles = makeStyles((theme) => ({
  paper: {
    margin: theme.spacing(20, 4),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: "#ffa25f",
  },
  form: {
    width: "100%", 
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: "#fff",
  },
}));

const ForgetPassword = ({ forgetPassword }) => {
  const classes = useStyles();
  const [email, setEmail] = useState("");
  const [backdrop, setBackdrop] = useState(false);

  const onFormSubmit = (e) => {
    e.preventDefault();
    const resetPasswordRequest = async () => {
      setBackdrop(true);
      await backend
        .post(`/reset-password?email=${email}`)
        .then(() => {
          setBackdrop(false);
          alert("Email sent");
          forgetPassword();
        })
        .catch(() => {
          setBackdrop(false);
          alert(
            "Something went wrong.\nPlease, contact us at tvshowstracker2021@gmail.com"
          );
        });
    };
    resetPasswordRequest();
  };

  return (
    <React.Fragment>
      {backdrop ? (
        <Backdrop className={classes.backdrop} open={backdrop}>
          <CircularProgress color="inherit" />
        </Backdrop>
      ) : (
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <DraftsIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Forget password?
          </Typography>
          <form className={classes.form} onSubmit={onFormSubmit}>
            <TextFieldOrg
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Your email"
              name="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <ButtonOrg
              type="submit"
              fullWidth
              variant="contained"
              className={classes.submit}
            >
              Reset password
            </ButtonOrg>
          </form>
          <Grid container>
            <Grid item xs>
              <LinkOrg
                component="button"
                variant="body2"
                color="inherit"
                onClick={forgetPassword}
              >
                {"Missclick? Go to first page"}
              </LinkOrg>
            </Grid>
          </Grid>
          <Box mt={5}>
            <Copyright />
          </Box>
        </div>
      )}
    </React.Fragment>
  );
};

export default ForgetPassword;
