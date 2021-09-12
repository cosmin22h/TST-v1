import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8080/TST",
  headers: {
    post: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Headers":
        "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
    },
  },
});
