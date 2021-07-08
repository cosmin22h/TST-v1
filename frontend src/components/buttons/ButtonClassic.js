import { withStyles } from "@material-ui/core";
import Button from "@material-ui/core/Button";

const ClassicButton = withStyles((theme) => ({
  root: {
    color: theme.palette.getContrastText("#fff"),
    backgroundColor: "rgba(255, 255, 255, 0.7)",
    "&:hover": {
      backgroundColor: "#fff",
    },
    fontSize: "0.7rem",
  },
}))(Button);

export default ClassicButton;
