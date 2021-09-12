import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';

import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const AdminNavBar = ({ logout }) => {
  const [message, setMessage] = useState("");
  const [newComment, setNewComment] = useState(false);
  const [register, setRegister] = useState("");
  const [newRegister, setNewRegister] = useState(false);

  useEffect(() => {
    const connect = () => {
      const URL = "http://localhost:8080/socket";
      const websocket = new SockJS(URL);
      const stompClient = Stomp.over(websocket);
      stompClient.connect({}, (frame) => {
        stompClient.subscribe("/topic/socket/user/comments", (notification) => {
          let message = notification.body;
          setMessage(message);
          setNewComment(true);
        });
        stompClient.subscribe("/topic/socket/user/register", (notification) => {
          let message = notification.body;
          setRegister(message);
          setNewRegister(true);
        });
      });
    };
    connect();
  }, []);

  return (
    <div>
      <div className="ui secondary pointing large menu">
      <Link to="/" className="item">
          Reports
        </Link>
        <Link to="/admin/users" className="item">
          All Users
        </Link>
        <Link to="/admin/tvshows/view" className="item">
          View TV Shows
        </Link>
        <Link to="/admin/tvshows/add" className="item">
          Add TV Shows
        </Link>
        <Link to="/" className="right item" onClick={logout}>
          Logout
        </Link>
      </div>
      <br />
      <Snackbar open={newComment} autoHideDuration={6000} onClose={() => setNewComment(false)}>
        <Alert onClose={() => setNewComment(false)} severity="info">{message}</Alert>
      </Snackbar>
      <Snackbar open={newRegister} autoHideDuration={6000} onClose={() => setNewRegister(false)}>
        <Alert onClose={() => setNewRegister(false)} severity="warning">{register}</Alert>
      </Snackbar>
    </div>
  );
};

export default AdminNavBar;
