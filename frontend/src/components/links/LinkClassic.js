import { Link, withStyles } from "@material-ui/core";

const LinkClassic = withStyles({
  root: {
    color: "#000",
    "&:hover": {
      textDecoration: "none",
      color: "#000",
    },
  },
})(Link);

export default LinkClassic;
