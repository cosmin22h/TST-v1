import axios from "axios";
import React from "react";
import ReactDOM from "react-dom";
import App from "./components/App";

axios.interceptors.request.use(
  (request) => {
    let basicToken = localStorage.getItem("token");
    if (basicToken) {
      request.headers["Authorization"] = basicToken;
    }
    return request;
  },
  (error) => {
    return Promise.reject(error);
  }
);

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.querySelector("#root")
);
