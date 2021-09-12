import React, { useState } from "react";

import backend from "../apis/backend";

import {
  Typography,
  makeStyles,
  Grid,
  Avatar,
  Box,
  InputAdornment,
  IconButton,
} from "@material-ui/core";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";

import LinkOrg from "../components/links/LinkOrg";
import Copyright from "../components/Copyright";
import TextFieldOrg from "../components/texts/TextFieldOrg";
import ButtonOrg from "../components/buttons/ButtonOrg";
import { Visibility, VisibilityOff } from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
  paper: {
    margin: theme.spacing(12, 4),
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
}));

const Login = ({ signUp, forgetPassword }) => {
  const classes = useStyles();
  const [userRequest, setUserRequets] = useState({
    username: "",
    password: "",
  });
  const [visibility, setVisibilty] = useState(false);

  const onFormChange = (event) => {
    const target = event.target;
    const name = target.name;
    const value = target.value;

    setUserRequets({
      ...userRequest,
      [name]: value,
    });
  };

  const acitivateSession = ({ id, role }) => {
    localStorage.setItem("ID session", id.toString());
    localStorage.setItem("User", role.toString());
    localStorage.setItem("token", "Basic " + btoa(userRequest.username + ':' + userRequest.password));
    window.location.reload();
  };

  const onFormSubmit = async (e) => {
    e.preventDefault();
    await backend
      .post("/basic-user/login", userRequest)
      .then((res) => {
        const data = res.data;
        acitivateSession({ id: data.id, role: data.role });
      })
      .catch((err) => alert(err.response.data.message));
  };

  return (
    <div className={classes.paper}>
      <Avatar className={classes.avatar}>
        <LockOutlinedIcon />
      </Avatar>
      <Typography component="h1" variant="h5">
        Log in
      </Typography>
      <form className={classes.form} onSubmit={onFormSubmit}>
        <TextFieldOrg
          variant="outlined"
          margin="normal"
          required
          fullWidth
          id="username"
          label="Username"
          name="username"
          value={userRequest.username}
          onChange={onFormChange}
        />
        <TextFieldOrg
          variant="outlined"
          margin="normal"
          required
          fullWidth
          name="password"
          label="Password"
          type={!visibility ? "password" : "text"}
          id="password"
          value={userRequest.password}
          onChange={onFormChange}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton
                  aria-label="toggle password visibility"
                  onClick={() => setVisibilty(!visibility)}
                >
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
          Log In
        </ButtonOrg>
      </form>
      <Grid container>
        <Grid item xs>
          <LinkOrg component="button"
            variant="body2"
            color="inherit"
            onClick={forgetPassword}>
            Forgot password?
          </LinkOrg>
        </Grid>
        <Grid item>
          <LinkOrg
            component="button"
            variant="body2"
            color="inherit"
            onClick={signUp}
          >
            {"Don't have an account? Sign Up"}
          </LinkOrg>
        </Grid>
      </Grid>
      <Box mt={5}>
        <Copyright />
      </Box>
    </div>
  );
};

export default Login;
