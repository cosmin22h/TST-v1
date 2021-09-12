import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import backend from "../apis/backend";

import {
  Typography,
  makeStyles,
  Avatar,
  Box,
  InputAdornment,
  IconButton,
} from "@material-ui/core";
import Copyright from "../components/Copyright";
import TextFieldOrg from "../components/texts/TextFieldOrg";
import ButtonOrg from "../components/buttons/ButtonOrg";
import LockOpenIcon from "@material-ui/icons/LockOpen";
import { Visibility, VisibilityOff } from "@material-ui/icons";

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
}));

const ChangePassword = ({ changePassword }) => {
  const classes = useStyles();
  const token = useParams();
  const [newPassword, setNewPassword] = useState({
    username: "",
    password: "",
    confirmPassword: "",
  });
  const [isErrToken, setIsErrToken] = useState(false);
  const [visibility, setVisibilty] = useState(false);

  useEffect(() => {
    const validateToken = async () => {
      await backend
        .get(`/user/change-password?token=${token.token}`)
        .then((res) =>
          setNewPassword({
            username: res.data,
            password: "",
            confirmPassword: "",
          })
        )
        .catch((err) => {
          alert(err.response.data.message);
          setIsErrToken(true);
        });
    };
    validateToken();
  }, [token]);

  const onFormChange = (event) => {
    const target = event.target;
    const name = target.name;
    const value = target.value;

    setNewPassword({
      ...newPassword,
      [name]: value,
    });
  };

  const onFormSubmit = (e) => {
    e.preventDefault();
    const setNewPassword = async () => {
      let data = {
        username: newPassword.username,
        password: newPassword.password,
      };
      await backend.put("/user/set-new-password", data)
      .then(() => {
          alert("New password set");
          changePassword();
      })
      .catch(err => {
        alert(err.response.data.message);
    })
    };
    if (newPassword.password !== newPassword.confirmPassword) {
        alert("Passwords do not match");
    } else {
        setNewPassword();
    }
  };

  if (isErrToken) {
    changePassword();
  }

  return (
    <div className={classes.paper}>
      <Avatar className={classes.avatar}>
        <LockOpenIcon />
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
          id="password"
          label="New password"
          name="password"
          type={!visibility ? "password" : "text"}
          value={newPassword.password}
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
          label="Confirm new password"
          name="confirmPassword"
          type={!visibility ? "password" : "text"}
          value={newPassword.confirmPassword}
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
          Reset password
        </ButtonOrg>
      </form>
      <Box mt={5}>
        <Copyright />
      </Box>
    </div>
  );
};

export default ChangePassword;
