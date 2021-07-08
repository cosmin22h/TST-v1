import { withStyles } from "@material-ui/core";
import Button from "@material-ui/core/Button";

const ColorButton = withStyles((theme) => ({
  root: {
    color: theme.palette.getContrastText("#ffa25f"),
    backgroundColor: "#ffa25f",
    "&:hover": {
      backgroundColor: "#ffa25f",
    },
    fontSize: "1.0rem",
  },
}))(Button);

export default ColorButton;
