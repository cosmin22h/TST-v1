import React, { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";

import backend from "../apis/backend";

const Comments = () => {
  const { idEpisode } = useParams();
  const [comments, setComments] = useState([]);

  useEffect(() => {
    const getComments = async () => {
      await backend
        .get(`/comment/all-comments/${idEpisode}/ADMIN`)
        .then((res) => setComments(res.data))
        .catch((err) => console.log(err.response));
    };
    getComments();
  }, [idEpisode]);

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

  const onClickDelete = (idComment) => {
    const deleteComment = async () => {
      await backend
        .delete(`/comment/remove/${idComment}`)
        .then(() => window.location.reload())
        .catch((err) => console.log(err.response));
    };
    deleteComment();
  };

  const onClickSpoiler = (idComment, isSpoiler) => {
    const spoilerComment = async () => {
      let data = {
        isSpoiler: isSpoiler,
      };
      await backend
        .put(`/comment/edit/${idComment}`, data)
        .then(() => window.location.reload())
        .catch((err) => console.log(err.response));
    };
    spoilerComment();
  };

  const renderedList = () => {
    return comments.map((comment) => {
      const user = comment.user;
      const currentTime = new Date().getTime();

      const postDate = new Date(comment.postDate);

      const postTime =
        postDate.getTime() - new Date().getTimezoneOffset() * 60000;
      const diffTime = (currentTime - postTime) / 1000;
      return (
        <div key={comment.id} className="comment">
          <div className="avatar">
            <img
              alt=""
              src={`http://localhost:8080/TST/user/avatar/${user.id}`}
            />
          </div>
          <div className="content">
            <Link to={`/admin/users/update/${user.id}`}>{user.username}</Link>
            <div className="metadata">
              <span className="date">
                {timePosted(diffTime, comment.postDate)}
              </span>
            </div>
            <div className="text">{comment.content}</div>
          </div>
          <br />
          <button
            className="ui negative right floated button"
            onClick={() => onClickDelete(comment.id)}
          >
            Delete comment
          </button>
          {!comment.isSpoiler ? (
            <button
              className="ui yellow right floated button"
              onClick={() => onClickSpoiler(comment.id, true)}
            >
              Mark as spoiler
            </button>
          ) : (
            <button
              className="ui green right floated button"
              onClick={() => onClickSpoiler(comment.id, false)}
            >
              Mark as not spoiler
            </button>
          )}
          <br />
          <br />
        </div>
      );
    });
  };

  return (
    <div className="ui comments">
      <h3 className="ui dividing header">Comments</h3>
      {renderedList()}
      <br/>
    </div>
  );
};

export default Comments;
