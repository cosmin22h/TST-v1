import { TextField, withStyles } from "@material-ui/core";

const TextFieldClassic = withStyles({
  root: {
    "& label.Mui-focused": {
      color: "#fff",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: "#fff",
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "white",
      },
      "&:hover fieldset": {
        borderColor: "#fff",
      },
      "&.Mui-focused fieldset": {
        borderColor: "#fff",
      },
    },
    "& .MuiOutlinedInput-input:-webkit-autofill": {
      WebkitTextFillColor: "#fff",
      WebkitBoxShadow: "0 0 0 100px #424242 inset",
    },
  },
})(TextField);

export default TextFieldClassic;
