import React, { useState, useEffect } from "react";
import backend from "../apis/backend";

import {
  Container,
  makeStyles,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  InputAdornment,
  TextareaAutosize,
  OutlinedInput,
} from "@material-ui/core";
import FacebookIcon from "@material-ui/icons/Facebook";
import InstagramIcon from "@material-ui/icons/Instagram";
import TwitterIcon from "@material-ui/icons/Twitter";
import RedditIcon from "@material-ui/icons/Reddit";

import ToggleButton from "@material-ui/lab/ToggleButton";
import ToggleButtonGroup from "@material-ui/lab/ToggleButtonGroup";
import TextFieldClassic from "../components/texts/TextFieldClassic";
import ButtonOrg from "../components/buttons/ButtonOrg";

const useStyles = makeStyles((theme) => ({
  root: {
    position: "absolute",
    top: "18%",
    left: "32%",
    border: "solid 1px #696969",
  },
  paper: {
    margin: theme.spacing(1, 4),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  title: {
    backgroundColor: "#585858",
    height: "40px",
    fontSize: "16px",
    color: "#fff",
    borderBottom: "2px solid #696969",
    padding: "8px 10px",
  },
  buttons: {
    display: "flex",
    flexDirection: "column",
    position: "absolute",
    top: "12%",
    left: "32%",
    "& .MuiToggleButton-root.Mui-selected": {
      color: "#000",
      backgroundColor: "#fff",
    },
  },
  button: {
    border: "solid 2px #696969",
    color: "#fff",
  },
  form: {
    width: "100%", 
    marginTop: theme.spacing(1),
  },
  cssLabel: {
    color: "#fff",
    "&.Mui-focused": {
      color: "#fff",
    },
  },
  formControl: {
    margin: theme.spacing(1),
    left: "-1.5%",
  },
  select: {
    "&:before": {
      color: "#fff",
      borderColor: "#fff",
    },
    "&:after": {
      borderColor: "#fff",
    },
  },
  textarea: {
    color: "#fff",
    backgroundColor: "#424242",
    border: "1px solid #696969",
    margin: theme.spacing(2),
    width: "99.8%",
    position: "relative",
    left: "-3.3%",
  },
  icon: {
    fill: "#fff",
  },
}));

const useOutlinedInputStyles = makeStyles((theme) => ({
  root: {
    "& .MuiOutlinedInput-input": {
      color: "#fff",
    },
    "& $notchedOutline": {
      borderColor: "#fff",
    },
    "&:hover $notchedOutline": {
      borderColor: "#fff",
    },
    "&$focused $notchedOutline": {
      borderColor: "#fff",
    },
  },
  focused: {},
  notchedOutline: {},
}));

const edit = [
  { value: "general", label: "General" },
  { value: "about", label: "About me" },
  { value: "advanced", label: "Advanced" },
];

const genders = [
  {
    label: "Female",
    value: "Famele",
  },
  {
    label: "Male",
    value: "Male",
  },
  {
    label: "Other",
    value: "Other",
  },
];

const countries = ["United States", "Romania"];

const currentDate = () => {
  var today = new Date();
  var dd = String(today.getDate()).padStart(2, "0");
  var mm = String(today.getMonth() + 1).padStart(2, "0");
  var yyyy = today.getFullYear();

  return yyyy + "-" + mm + "-" + dd;
};

const EditUser = () => {
  const classes = useStyles();
  const outlinedInputClasses = useOutlinedInputStyles();
  const [showInfo, setShowInfo] = useState(edit[0]);
  const [user, setUser] = useState({
    username: "",
    email: "",
    displayName: "",
    gender: "",
    country: "",
    birthday: "",
    facebook: "",
    instagram: "",
    twitter: "",
    reddit: "",
  });

  useEffect(() => {
    const getUser = async () => {
      await backend
        .get(`/user/get/${localStorage.getItem("ID session")}`)
        .then((res) => {
          setUser(res.data);
        })
        .catch((err) => {
          console.log(err.response);
        });
    };
    getUser();
  }, []);

  const onFormChange = (event) => {
    const target = event.target;
    const name = target.name;
    const value = target.value;

    setUser({
      ...user,
      [name]: value,
    });
  };

  const onFormSubmit = async (e) => {
    e.preventDefault();
    await backend
      .put(`user/edit/${localStorage.getItem("ID session")}`, user)
      .then(() => window.location.reload())
      .catch((err) => {
        console.log(err.response);
        alert(err.response.data.errors);
      });
  };

  const renderedGenders = () => {
    return genders.map((g) => {
      return (
        <MenuItem key={g.value} value={g.value}>
          {g.label}
        </MenuItem>
      );
    });
  };

  const renderedCountries = () => {
    return countries.map((c) => {
      return (
        <MenuItem key={c} value={c}>
          {c}
        </MenuItem>
      );
    });
  };

  const general = (
    <React.Fragment>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        name="username"
        label="Username"
        type="text"
        id="username"
        focused
        required
        value={user.username}
        onChange={onFormChange}
        InputProps={{ className: classes.cssLabel }}
        InputLabelProps={{ className: classes.cssLabel }}
      ></TextFieldClassic>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        focused
        name="email"
        label="Email"
        type="email"
        id="email"
        required
        value={user.email}
        onChange={onFormChange}
        InputProps={{ className: classes.cssLabel }}
        InputLabelProps={{ className: classes.cssLabel }}
      ></TextFieldClassic>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        name="displayName"
        label="Display name"
        type="text"
        focused
        id="displayName"
        required
        value={user.displayName}
        onChange={onFormChange}
        InputProps={{ className: classes.cssLabel }}
        InputLabelProps={{ className: classes.cssLabel }}
      ></TextFieldClassic>
      <FormControl variant="outlined" fullWidth className={classes.formControl}>
        <InputLabel style={{ color: "#fff" }}>Gender</InputLabel>
        <Select
          value={user.gender !== null ? user.gender : ""}
          onChange={onFormChange}
          input={
            <OutlinedInput
              label="Gender"
              name="gender"
              id="gender"
              classes={outlinedInputClasses}
            />
          }
          inputProps={{
            classes: {
              icon: classes.icon,
            },
          }}
        >
          <MenuItem value="" disabled>
            <em>None</em>
          </MenuItem>
          {renderedGenders()}
        </Select>
      </FormControl>
      <FormControl variant="outlined" fullWidth className={classes.formControl}>
        <InputLabel style={{ color: "#fff" }}>Country</InputLabel>
        <Select
          value={user.country !== null ? user.country : ""}
          onChange={onFormChange}
          input={
            <OutlinedInput
              label="Country"
              name="country"
              id="country"
              classes={outlinedInputClasses}
            />
          }
          inputProps={{
            classes: {
              icon: classes.icon,
            },
          }}
        >
          <MenuItem value="" disabled>
            <em>None</em>
          </MenuItem>
          {renderedCountries()}
        </Select>
      </FormControl>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        label="Birthday"
        name="birthday"
        type="date"
        id="birthday"
        value={user.birthday !== null ? user.birthday : ""}
        focused
        inputProps={{ max: currentDate() }}
        onChange={onFormChange}
        InputProps={{
          className: classes.cssLabel,
        }}
      ></TextFieldClassic>
      <br />
      <br />
    </React.Fragment>
  );
  const about = (
    <TextareaAutosize
      className={classes.textarea}
      rowsMin={10}
      id="about"
      name="about"
      value={user.about !== null ? user.about : ""}
      onChange={onFormChange}
    />
  );

  const advanced = (
    <React.Fragment>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        name="facebook"
        label="Facebook"
        type="text"
        id="facebook"
        value={user.facebook !== null ? user.facebook : ""}
        onChange={onFormChange}
        InputProps={{
          className: classes.cssLabel,
          startAdornment: (
            <InputAdornment position="start">
              <FacebookIcon />
            </InputAdornment>
          ),
        }}
        InputLabelProps={{ className: classes.cssLabel }}
      ></TextFieldClassic>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        name="instagram"
        label="Instagram"
        type="text"
        id="instagram"
        value={user.instagram !== null ? user.instagram : ""}
        onChange={onFormChange}
        InputProps={{
          className: classes.cssLabel,
          startAdornment: (
            <InputAdornment position="start">
              <InstagramIcon />
            </InputAdornment>
          ),
        }}
        InputLabelProps={{ className: classes.cssLabel }}
      ></TextFieldClassic>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        name="twitter"
        label="Twitter"
        type="text"
        id="twitter"
        value={user.twitter !== null ? user.twitter : ""}
        onChange={onFormChange}
        InputProps={{
          className: classes.cssLabel,
          startAdornment: (
            <InputAdornment position="start">
              <TwitterIcon />
            </InputAdornment>
          ),
        }}
        InputLabelProps={{ className: classes.cssLabel }}
      ></TextFieldClassic>
      <TextFieldClassic
        variant="outlined"
        margin="normal"
        fullWidth
        name="reddit"
        label="Reddit"
        type="text"
        id="reddit"
        value={user.reddit !== null ? user.reddit : ""}
        onChange={onFormChange}
        InputProps={{
          className: classes.cssLabel,
          startAdornment: (
            <InputAdornment position="start">
              <RedditIcon />
            </InputAdornment>
          ),
        }}
        InputLabelProps={{ className: classes.cssLabel }}
      ></TextFieldClassic>
      <br />
      <br />
    </React.Fragment>
  );

  const renderedToggleButton = () => {
    return edit.map((e) => {
      return (
        <ToggleButton
          key={e.value}
          value={e.value}
          className={classes.button}
          onClick={() => setShowInfo(e)}
        >
          {e.label}
        </ToggleButton>
      );
    });
  };

  return (
    <React.Fragment>
      <div className={classes.buttons}>
        <ToggleButtonGroup size="small" value={showInfo.value} exclusive>
          {renderedToggleButton()}
        </ToggleButtonGroup>
      </div>
      <Container maxWidth="sm" className={classes.root}>
        <div className={classes.paper}>
          <Container maxWidth="sm" className={classes.title}>
            {showInfo.label}
          </Container>
          <form className={classes.form} onSubmit={onFormSubmit}>
            {showInfo.value === "general"
              ? general
              : showInfo.value === "about"
              ? about
              : advanced}
            <ButtonOrg type="submit" fullWidth variant="contained">
              Save
            </ButtonOrg>
          </form>
        </div>
      </Container>
    </React.Fragment>
  );
};

export default EditUser;
