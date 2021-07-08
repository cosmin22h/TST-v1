import { Link, withStyles } from "@material-ui/core";

const LinkOrg = withStyles({
  root: {
    color: "#fff",
    "&:hover": {
      textDecoration: "none",
      color: "#ffa25f",
    },
  },
})(Link);

export default LinkOrg;
