import React, { useState, useEffect } from "react";
import { useHistory } from "react-router";
import backend from "../apis/backend";
import LinkClassic from "../components/links/LinkClassic";

import {
  makeStyles,
  createMuiTheme,
  ThemeProvider,
} from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import { green, red } from "@material-ui/core/colors";
import SearchBarUser from "../components/SearchBarUser";

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
    maxWidth: 360,
  },
  text: {
    color: "#fff",
  },
  button: {
    margin: theme.spacing(1),
    marginLeft: theme.spacing(3),
  },
  button2: {
    margin: theme.spacing(1),
  },
}));

const theme = createMuiTheme({
  palette: {
    primary: red,
    secondary: green,
  },
});

const Friends = ({ viewFriends, onChangeNotification }) => {
  const classes = useStyles();
  const [user, setUser] = useState("");
  const [friends, setFriends] = useState([]);
  const [refresh, setRefresh] = useState(false);
  const history = useHistory();

  const id = localStorage.getItem("ID session");

  useEffect(() => {
    const getFriends = async () => {
      await backend
        .get(`/friends/all-friends/${id}`)
        .then((res) => setFriends(res.data))
        .catch((err) => console.log(err.response));
    };
    const getRequests = async () => {
      await backend
        .get(`/friends/all-requests/${id}`)
        .then((res) => setFriends(res.data))
        .catch((err) => console.log(err.response));
    };
    if (viewFriends) getFriends();
    else getRequests();
  }, [id, refresh, viewFriends]);

  const onFormChange = (event) => {
    setUser(event.target.value);
  };

  const onFormSubmit = () => {
    history.push(`/user/${user}`);
  };

  const onClickConfirm = (idFriend) => {
    const confirmFriend = async () => {
      let data = {
        idFriend: idFriend,
        idUser: id,
      };
      await backend
        .put("/friends/accept", data)
        .catch((err) => console.log(err));
      setRefresh(!refresh);
    };
    confirmFriend();
    onChangeNotification();
  };

  const onClickReject = (idFriend) => {
    const rejectFriend = async () => {
      await backend
        .delete(`/friends/reject/${id}/${idFriend}`)
        .catch((err) => console.log(err));
      setRefresh(!refresh);
    };
    rejectFriend();
    onChangeNotification();
  };

  const onClickUnfriend = (idFriend) => {
    const unfriend = async () => {
      await backend
        .delete(`/friends/unfriend/${id}/${idFriend}`)
        .catch((err) => console.log(err));
      setRefresh(!refresh);
    };
    unfriend();
  };
  return (
    <React.Fragment>
      {viewFriends ?
      <SearchBarUser
        term={user}
        placeholder="Search user"
        onFormChange={onFormChange}
        onFormSubmit={onFormSubmit}
      />: null}
      <List dense className={classes.root}>
        {friends.map((friend, index) => {
          const labelId = `checkbox-list-secondary-label-${friend}`;
          const friendData = friend.friend;
          return (
            <ListItem key={index}>
              <ListItemAvatar>
                <Avatar src={`http://localhost:8080/TST/user/avatar/${friendData.id}`}/>
              </ListItemAvatar>
              <ListItemText id={labelId} className={classes.text}>
                <LinkClassic
                  href={`/user/${friendData.username}`}
                  style={{ color: "white" }}
                >
                  {friendData.displayName}
                </LinkClassic>
              </ListItemText>
              <ThemeProvider theme={theme}>
                {viewFriends ? (
                  <Button
                    variant="contained"
                    color="primary"
                    className={classes.button}
                    onClick={() => onClickUnfriend(friendData.id)}
                  >
                    Unfriend
                  </Button>
                ) : (
                  <React.Fragment>
                    <Button
                      variant="contained"
                      color="secondary"
                      className={classes.button}
                      onClick={() => onClickConfirm(friendData.id)}
                    >
                      Confirm
                    </Button>
                    <Button
                      color="primary"
                      variant="contained"
                      className={classes.button2}
                      onClick={() => onClickReject(friendData.id)}
                    >
                      Reject
                    </Button>
                  </React.Fragment>
                )}
              </ThemeProvider>
            </ListItem>
          );
        })}
      </List>
    </React.Fragment>
  );
};

export default Friends;
