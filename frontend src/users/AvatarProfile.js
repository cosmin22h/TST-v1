import React, { useState, useEffect } from "react";
import backend from "../apis/backend";

import {
  makeStyles,
  createMuiTheme,
  ThemeProvider,
} from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardMedia from "@material-ui/core/CardMedia";
import { Button } from "@material-ui/core";
import { red, green } from "@material-ui/core/colors";


const useStyles = makeStyles({
  root: {
    maxWidth: 300,
    backgroundColor: "#424242",
  },
  media: {
    height: 300,
  },
});

const theme = createMuiTheme({
  palette: {
    primary: red,
    secondary: green,
  },
});

const AvatarProfile = () => {
  const classes = useStyles();
  const id = localStorage.getItem("ID session");
  const [avatar, setAvatar] = useState("");
  const [isAvatar, setIsAvatar] = useState(false);

  useEffect(() => {
    const getAvatar = async () => {
      await backend
        .get(`http://localhost:8080/TST/user/avatar/${id}`)
        .then((res) => {
            if (res.data !== "") setIsAvatar(true);
        })
        .catch((err) => console.log(err));
    };
    getAvatar();
  }, [id]);

  const selectFile = (e) => {
    setAvatar(e.target.files);
  };

  const onUpdateAvatar = () => {
    const updateAvatar = async () => {
      let formData = new FormData();
      formData.append("avatar", avatar[0]);

      await backend
        .post(`/user/upload-avatar/${id}`, formData)
        .catch((err) => console.log(err));
    };
    updateAvatar();
    window.location.reload();
  };

  const onRemoveAvatar = () => {
    const removeAvatar = async () => {

      await backend
        .delete(`/user/delete-avatar/${id}`)
        .catch((err) => console.log(err));
    };
    removeAvatar();
    window.location.reload();
  };

  return (
    <Card className={classes.root}>
      {avatar !== "" ? (
        <CardMedia
          className={classes.media}
          image={URL.createObjectURL(avatar[0])}
        />
      ) : null}
      <CardActions>
        <input type="file" onChange={selectFile} style={{ color: "white" }} />
      </CardActions>
      <CardActions>
        <ThemeProvider theme={theme}>
          <Button
            color="secondary"
            variant="contained"
            fullWidth
            size="small"
            disabled={avatar === ""}
            onClick={onUpdateAvatar}
          >
            Upload
          </Button>

          <Button
            color="primary"
            variant="contained"
            fullWidth
            size="small"
            disabled={!isAvatar}
            onClick={onRemoveAvatar}
          >
            Remove avatar
          </Button>
        </ThemeProvider>
      </CardActions>
    </Card>
  );
};

export default AvatarProfile;
