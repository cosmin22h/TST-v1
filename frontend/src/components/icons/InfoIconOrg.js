import { withStyles } from "@material-ui/core";
import InfoIcon from "@material-ui/icons/Info";

const InfoIconOrg = withStyles({
  root: {
    "&:hover": {
      color: "#ffa25f",
    },
  },
})(InfoIcon);

export default InfoIconOrg;
