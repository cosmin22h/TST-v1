import axioas from "axios";

const KEY = "AIzaSyA9EMMnCT7UAhmZM9R3PqOrp1SjyNAwhJk";

export default axioas.create({
  baseURL: "https://www.googleapis.com/youtube/v3",
  params: {
    part: "snippet",
    maxResults: 1,
    type: "video",
    key: KEY,
  },
});
