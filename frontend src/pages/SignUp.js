import React, { useState } from "react";
import backend from "../apis/backend";
import { useHistory } from "react-router";

import {
  Typography,
  makeStyles,
  Grid,
  Avatar,
  Box,
  InputAdornment,
  IconButton,
} from "@material-ui/core";
import PersonAddIcon from "@material-ui/icons/PersonAdd";

import TextFieldOrg from "../components/texts/TextFieldOrg";
import ButtonOrg from "../components/buttons/ButtonOrg";
import LinkOrg from "../components/links/LinkOrg";
import Copyright from "../components/Copyright";
import { Visibility, VisibilityOff } from "@material-ui/icons";
import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";

const useStyles = makeStyles((theme) => ({
  paper: {
    margin: theme.spacing(8, 4),
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

const SignUp = ({ login }) => {
  const [newUser, setNewUser] = useState({
    username: "",
    email: "",
    role: "USER",
    password: "",
  });
  const [confirmPass, setConfirmPass] = useState("");
  const [visibility, setVisibilty] = useState(false);
  const [backdrop, setBackdrop] = useState(false);
  const classes = useStyles();
  const history = useHistory();

  const onFormChange = (event) => {
    const target = event.target;
    const name = target.name;
    const value = target.value;

    if (name === "confirmPassword") {
      setConfirmPass(value);
    } else {
      setNewUser({
        ...newUser,
        [name]: value,
      });
    }
  };

  const onFormSubmit = async (e) => {
    e.preventDefault();
    if (newUser.password !== confirmPass) {
      alert("Passwords do not match");
      return;
    }
    if (newUser.password.length < 8) {
      alert("Password must have at least 8 characters");
      return;
    }
    setBackdrop(true);
    await backend
      .post("/user/add", newUser)
      .then(() => {
        setBackdrop(false);
        history.goBack(2);
      })
      .catch((err) => {
        let error = err.response.data;
        if (error.message.includes("Bad request")) {
          alert(
            "Password must contains  at least 1 uppercase, 1 lowercase, 1 digit, 1 special character"
          );
        } else {
          alert(error.message);
        }
        setBackdrop(false);
      });
  };
  return (
    <React.Fragment>
      {backdrop ? (
        <Backdrop
          className={classes.backdrop}
          open={backdrop}
        >
          <CircularProgress color="inherit" />
        </Backdrop>
      ) : (
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <PersonAddIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <form className={classes.form} onSubmit={onFormSubmit}>
            <Grid container spacing={2}>
              <TextFieldOrg
                className={classes.field}
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="username"
                label="Username"
                name="username"
                value={newUser.username}
                onChange={onFormChange}
              />
              <TextFieldOrg
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email"
                name="email"
                type="email"
                value={newUser.email}
                onChange={onFormChange}
              />
              <TextFieldOrg
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="password"
                label="Password"
                name="password"
                type={!visibility ? "password" : "text"}
                value={newUser.password}
                onChange={onFormChange}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={() => setVisibilty(!visibility)}
                      >
                        {" "}
                        {visibility ? <Visibility /> : <VisibilityOff />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
              <TextFieldOrg
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="confirmPassword"
                label="Confirm password"
                name="confirmPassword"
                type={!visibility ? "password" : "text"}
                value={newUser.confirmPassword}
                onChange={onFormChange}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={() => setVisibilty(!visibility)}
                      >
                        {" "}
                        {visibility ? <Visibility /> : <VisibilityOff />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
              <ButtonOrg
                type="submit"
                fullWidth
                variant="contained"
                className={classes.submit}
              >
                Sing Up
              </ButtonOrg>
            </Grid>
          </form>
          <Grid container>
            <Grid item>
              <LinkOrg
                component="button"
                variant="body2"
                color="inherit"
                onClick={login}
              >
                {"Already have an account? Log in"}
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

export default SignUp;
