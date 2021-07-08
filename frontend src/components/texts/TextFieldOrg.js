import { TextField, withStyles } from "@material-ui/core";

const TextFieldOrg = withStyles({
  root: {
    "& label.Mui-focused": {
      color: "#ffa25f",
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "#fff",
      },
      "&:hover fieldset": {
        borderColor: "#ffa25f",
      },
      "&.Mui-focused fieldset": {
        borderColor: "#ffa25f",
      },
    },
    "& .MuiOutlinedInput-input:-webkit-autofill": {
      WebkitBoxShadow: "0 0 0 100px #424242 inset",
    },
  },
})(TextField);

export default TextFieldOrg;
