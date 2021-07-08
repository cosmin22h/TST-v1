import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import backend from "../../apis/backend";

import WarningIcon from "@material-ui/icons/Warning";
import FlagIcon from "@material-ui/icons/Flag";
import Divider from "@material-ui/core/Divider";
import { makeStyles } from "@material-ui/core/styles";
import Avatar from "@material-ui/core/Avatar";
import IconButton from "@material-ui/core/IconButton";
import { Grid, Paper, TextareaAutosize, Typography } from "@material-ui/core";
import FavoriteIcon from "@material-ui/icons/Favorite";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import ButtonOrg from "../../components/buttons/ButtonOrg";
import { DeleteForever } from "@material-ui/icons";
import LinkOrg from "../../components/links/LinkOrg";
import ButtonClassic from "../../components/buttons/ButtonClassic";
import MenuItem from "@material-ui/core/MenuItem";
import Menu from "@material-ui/core/Menu";
import LinkClassic from "../../components/links/LinkClassic";

const useStyles = makeStyles((theme) => ({
  paper: {
    border: "1px solid #696969",
    backgroundColor: "#424242",
    margin: theme.spacing(2),
    padding: "40px 20px",
  },
  avatar: {
    width: theme.spacing(7),
    height: theme.spacing(7),
  },
  textarea: {
    color: "#fff",
    backgroundColor: "#424242",
    border: "1px solid #696969",
    margin: theme.spacing(2),
    width: "97%",
    position: "relative",
  },
  button: {
    left: "92.8%",
  },
  title: {
    textAlign: "left",
    color: "white",
  },
  icon: {
    float: "right",
    color: "white",
  },
  favorite: {
    float: "left",
    color: "white",
  },
  time: {
    textAlign: "left",
    color: "gray",
  },
  comment: {
    textAlign: "left",
    color: "white",
  },
  spoiler: {
    marginTop: theme.spacing(-0.5),
    marginLeft: theme.spacing(0.5),
  },
}));

const Comments = () => {
  const classes = useStyles();
  const { id } = useParams();
  const idUser = localStorage.getItem("ID session");
  const [comments, setComments] = useState([]);
  const [likedComments, setLikedComments] = useState([]);
  const [content, setContent] = useState("");
  const [anchorEl, setAnchorEl] = useState(null);
  const [comment, setComment] = useState(null);

  useEffect(() => {
    const getComments = async () => {
      await backend
        .get(`/comment/all-comments/${id}/USER`)
        .then((res) => {
          setComments(res.data);
        })
        .catch((err) => console.log(err));
    };
    const getLikedComments = async () => {
      await backend
        .get(`/comment/all-liked-comments/${idUser}`)
        .then((res) => {
          setLikedComments(res.data);
        })
        .catch((err) => console.log(err));
    };
    getComments();
    getLikedComments();
  }, [id, idUser]);

  const timePosted = (diffTime) => {
    let diffTimeMinutes = Math.round(diffTime / 60);
    let diffTimeHours = 0;
    while (diffTimeMinutes > 59) {
      diffTimeMinutes -= 60;
      diffTimeHours++;
    }
    let diffTimeDays = 0;
    while (diffTimeHours > 23) {
      diffTimeHours -= 24;
      diffTimeDays++;
    }

    let timeAgo = "";
    if (diffTimeMinutes === 0 && diffTimeHours === 0 && diffTimeDays === 0) {
      timeAgo = "now";
    } else {
      if (diffTimeDays > 0) {
        if (diffTimeDays === 1) {
          timeAgo += "1 day ";
        } else {
          timeAgo += `${diffTimeDays} days `;
        }
      }
      if (diffTimeHours > 0) {
        if (diffTimeHours === 1) {
          timeAgo += "1 hour ";
        } else {
          timeAgo += `${diffTimeHours} hours `;
        }
      }
      if (diffTimeMinutes > 0) {
        if (diffTimeMinutes === 1) {
          timeAgo += "1 minute ";
        } else {
          timeAgo += `${diffTimeMinutes} minutes `;
        }
      }
      timeAgo += "ago";
    }

    return timeAgo;
  };

  const onDeleteComment = (commId) => {
    const deleteAllReports = async () => {
      await backend
        .delete(`/report/delete-by-comment/${commId}`)
        .then(() => removeComment())
        .catch((err) => console.log(err.response));
    };
    const removeComment = async () => {
      await backend
        .delete(`/comment/remove/${commId}`)
        .catch((err) => console.log(err.response));
    };
    deleteAllReports(commId);
    window.location.reload();
  };

  const onClickSpoiler = (index) => {
    let commentsAux = [...comments];
    let commentSpoiler = { ...commentsAux[index] };
    commentSpoiler.isSpoiler = false;
    commentsAux[index] = commentSpoiler;
    setComments(commentsAux);
  };

  const isMenuOpen = Boolean(anchorEl);
  const handleMenuOpen = (event, comment) => {
    setAnchorEl(event.currentTarget);
    setComment(comment);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const menuId = "primary-search-account-menu";
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{ vertical: "top", horizontal: "right" }}
      id={menuId}
      keepMounted
      transformOrigin={{ vertical: "top", horizontal: "right" }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <LinkClassic href={`/report/spoiler/${comment}`}>
        <MenuItem
          onClick={() => {
            handleMenuClose();
          }}
        >
          Spoiler
        </MenuItem>
      </LinkClassic>
      <LinkClassic href={`/report/inappropriate/${comment}`}>
        <MenuItem
          onClick={() => {
            handleMenuClose();
          }}
        >
          Inappropriate
        </MenuItem>
      </LinkClassic>
    </Menu>
  );

  const isLiked = (idComment) => {
    const comment = likedComments.find((e) => e.id === idComment);
    if (comment !== undefined) return true;
    return false;
  };

  const onClickLikeComment = (idComment) => {
    const likeComment = async () => {
      await backend
        .post(`/comment/${idComment}/like-comment/${idUser}`)
        .catch((err) => console.log(err.response));
    };
    likeComment();
    window.location.reload();
  };

  const onClickDislikeComment = (idComment) => {
    const dislikeComment = async () => {
      await backend
        .delete(`/comment/${idComment}/remove-like/${idUser}`)
        .catch((err) => console.log(err.response));
    };
    dislikeComment();
    window.location.reload();
  };

  const renderComments = () => {
    return comments.map((comm, index) => {
      const user = comm.user;
      const currentTime = new Date().getTime();

      const postDate = new Date(comm.postDate);

      const postTime =
        postDate.getTime() - new Date().getTimezoneOffset() * 60000;
      const diffTime = (currentTime - postTime) / 1000;

      return (
        <Paper key={comm.id} className={classes.paper}>
          <Grid container wrap="nowrap" spacing={2}>
            <Grid item>
              <Avatar
                className={classes.avatar}
                alt=""
                src={`http://localhost:8080/TST/user/avatar/${user.id}`}
              />
            </Grid>
            <Grid item xs>
              <Typography variant="h5" className={classes.title}>
                <LinkOrg href={`/user/${user.username}`}>
                  {" "}
                  {user.displayName}
                </LinkOrg>
                {user.id == idUser ? (
                  <IconButton
                    className={classes.icon}
                    onClick={() => onDeleteComment(comm.id)}
                  >
                    <DeleteForever />
                  </IconButton>
                ) : (
                  <IconButton
                    className={classes.icon}
                    onClick={(event) => handleMenuOpen(event, comm.id)}
                  >
                    <FlagIcon
                      edge="end"
                      aria-label="account of current user"
                      aria-controls={menuId}
                      aria-haspopup="true"
                      color="inherit"
                    />
                  </IconButton>
                )}
              </Typography>
              <Typography
                variant="subtitle2"
                component="p"
                className={classes.time}
              >
                posted {timePosted(diffTime, comm.postDate)}
              </Typography>{" "}
              <br />
              {!comm.isSpoiler ? (
                <Typography
                  variant="body1"
                  component="p"
                  className={classes.comment}
                >
                  {comm.content}
                </Typography>
              ) : (
                <Typography className={classes.comment} component="h1">
                  <Grid item container>
                    <WarningIcon />
                    <span>
                      &nbsp;<b>SPOILER ALERT!</b> Some users flagged this
                      comment as spoiler.&nbsp;
                    </span>{" "}
                    <ButtonClassic
                      className={classes.spoiler}
                      onClick={() => onClickSpoiler(index)}
                    >
                      Display anyway
                    </ButtonClassic>
                  </Grid>
                </Typography>
              )}
            </Grid>
          </Grid>
          <br />
          <Divider style={{ backgroundColor: "#696969" }} />
          {user.id == idUser ? (
            <Typography
              className={classes.favorite}
              variant="subtitle2"
              component="h2"
              style={{ marginTop: "10px", marginLeft: "9px" }}
            >
              <Grid item container>
                <FavoriteIcon fontSize="small" />
                <span>&nbsp;</span> {comm.likes}
              </Grid>
            </Typography>
          ) : !isLiked(comm.id) ? (
            <IconButton
              className={classes.favorite}
              onClick={() => onClickLikeComment(comm.id)}
            >
              {" "}
              <Typography variant="subtitle2" component="h2">
                <Grid item container>
                  <FavoriteBorderIcon fontSize="small" />
                  <span>&nbsp;</span>
                  {comm.likes}{" "}
                </Grid>
              </Typography>
            </IconButton>
          ) : (
            <IconButton
              className={classes.favorite}
              onClick={() => onClickDislikeComment(comm.id)}
            >
              {" "}
              <Typography variant="subtitle2" component="h2">
                <Grid item container>
                  <FavoriteIcon fontSize="small" />
                  <span>&nbsp;</span>
                  {comm.likes}{" "}
                </Grid>
              </Typography>
            </IconButton>
          )}
          {renderMenu}
        </Paper>
      );
    });
  };

  const onCommentChange = (e) => {
    setContent(e.target.value);
  };

  const onPostComment = (e) => {
    const postComment = async () => {
      let data = {
        content: content,
        idEpisode: id,
        idUser: idUser,
      };
      await backend
        .post("/comment/post", data)
        .catch((err) => alert(err.response.data.errors));
    };
    postComment();
    window.location.reload();
  };

  return (
    <React.Fragment>
      <TextareaAutosize
        className={classes.textarea}
        placeholder="Enter your comment here"
        rowsMin={5}
        onChange={onCommentChange}
        value={content}
      />
      <ButtonOrg className={classes.button} onClick={onPostComment}>
        POST
      </ButtonOrg>
      {renderComments()}
      <br />
    </React.Fragment>
  );
};

export default Comments;
