import React, { useState } from "react";
import { useHistory } from "react-router";

import {
  makeStyles,
  CardContent,
  Typography,
  ButtonBase,
} from "@material-ui/core";
import Box from "@material-ui/core/Box";
import Rating from "@material-ui/lab/Rating";
import SentimentVeryDissatisfiedIcon from "@material-ui/icons/SentimentVeryDissatisfied";
import SentimentDissatisfiedIcon from "@material-ui/icons/SentimentDissatisfied";
import SentimentSatisfiedIcon from "@material-ui/icons/SentimentSatisfied";
import SentimentSatisfiedAltIcon from "@material-ui/icons/SentimentSatisfiedAltOutlined";
import SentimentVerySatisfiedIcon from "@material-ui/icons/SentimentVerySatisfied";

import ButtonClassic from "../../components/buttons/ButtonClassic";
import backend from "../../apis/backend";

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
  },
  details: {
    display: "flex",
    flexDirection: "column",
  },
  content: {
    color: "#fff",
    flex: "1 0 auto",
  },
  controls: {
    color: "#fff",
    maxWidth: "650px",
    paddingLeft: theme.spacing(2),
    paddingBottom: theme.spacing(1),
  },
  image: {
    position: "relative",
    "&:hover, &$focusVisible": {
      zIndex: 1,
      "& $imageBackdrop": {
        opacity: 0.15,
      },
      "& $imageMarked": {
        opacity: 0,
      },
      "& $imageTitle": {
        border: "4px solid currentColor",
      },
    },
  },
  focusVisible: {},
  imageButton: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 0,
    bottom: 0,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    color: theme.palette.common.white,
  },
  imageSrc: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 0,
    bottom: 0,
    backgroundSize: "cover",
    backgroundPosition: "center 40%",
  },
  imageBackdrop: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 0,
    bottom: 0,
    backgroundColor: theme.palette.common.black,
    opacity: 0.4,
    transition: theme.transitions.create("opacity"),
  },
  imageTitle: {
    position: "relative",
    padding: `${theme.spacing(2)}px ${theme.spacing(4)}px ${
      theme.spacing(1) + 6
    }px`,
  },
  imageMarked: {
    height: 3,
    width: 18,
    backgroundColor: theme.palette.common.white,
    position: "absolute",
    bottom: -2,
    left: "calc(50% - 9px)",
    transition: theme.transitions.create("opacity"),
  },
  button: {
    textAlign: "center",
  },
}));

function IconContainer(props) {
  const { value, ...other } = props;
  return <span {...other}>{customIcons[value].icon}</span>;
}

const customIcons = {
  1: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Very Dissatisfied",
  },
  2: {
    icon: <SentimentDissatisfiedIcon />,
    label: "Dissatisfied",
  },
  3: {
    icon: <SentimentSatisfiedIcon />,
    label: "Neutral",
  },
  4: {
    icon: <SentimentSatisfiedAltIcon />,
    label: "Satisfied",
  },
  5: {
    icon: <SentimentVerySatisfiedIcon />,
    label: "Very Satisfied",
  },
};

const EpisodeDetails = ({ episodeToProp, idTvShow, isStopped, isLastEpiosde }) => {
  const classes = useStyles();
  const episode = episodeToProp.ep;
  const [watched, setWatched] = useState(episodeToProp.viewed.isViewed);
  const [rate, setRate] = useState(episodeToProp.viewed.rating);
  const idUser = localStorage.getItem("ID session");
  const history = useHistory();
 
  const onClickChange = () => {
    const setWatchedEpiosde = async () => {
      if (!watched) {
        let data = {
          idUser: idUser,
          idEpisode: episode.id,
          rating: 0,
        };
        // marcam episodul
        await backend.post("/episode-viewed/add", data).catch(err => console.log(err.response));
        // reluare serial
        if (isStopped) {
          await backend.delete(
            `/lists/remove-from-list/STOPPED/${idTvShow}/${idUser}`
          ).catch(err => console.log(err.response));
          await backend.post("/lists/add-to-list", {
            type: "TO_WATCH",
            idUser: idUser,
            idTvShow: idTvShow,
          }).catch(err => console.log(err.response));
        }
        // recently watched 
        let list = {
          type: "RECENTLY_WATCHED",
          idUser: idUser,
          idTvShow: idTvShow,
        };
        await backend.post("/lists/add-to-list", list).catch(err => console.log(err.response));
        // last episode
        if (isLastEpiosde) {
          await backend.delete(
            `/lists/remove-from-list/TO_WATCH/${idTvShow}/${idUser}`
          ).catch(err => console.log(err.response));
          await backend.post("/lists/add-to-list", {
            type: "UP_TO_DATE",
            idUser: idUser,
            idTvShow: idTvShow,
          }).catch(err => console.log(err.response));
        }
      } else {
        await backend.delete(`/episode-viewed/delete/${idUser}/${episode.id}`).catch(err => console.log(err.response));
        await backend.delete(
          `/lists/remove-from-list/RECENTLY_WATCHED/${idTvShow}/${idUser}`
        ).catch(err => console.log(err.response));
        if (isLastEpiosde) {
          await backend.delete(
            `/lists/remove-from-list/UP_TO_DATE/${idTvShow}/${idUser}`
          ).catch(err => console.log(err.response));
          await backend.post("/lists/add-to-list", {
            type: "TO_WATCH",
            idUser: idUser,
            idTvShow: idTvShow,
          }).catch(err => console.log(err.response));
        }
      }
    };
    setWatchedEpiosde();
    setWatched(!watched);
  };

  const onChangeRating = (event) => {
    const newValue = parseInt(event.target.value);
    const rateThisEp = async () => {
      let data = {
        idUser: idUser,
        idEpisode: episode.id,
        rating: newValue,
      };
      await backend.put("/episode-viewed/rate", data).catch(err => console.log(err.response));
    };
    rateThisEp();
    setRate(newValue);
  };

  return (
    <div className={classes.details}>
      <CardContent className={classes.content}>
        <Typography component="h5" variant="h5">
          {episode.name}
        </Typography>
        <Typography variant="subtitle1" color="textSecondary">
          Season: {episode.noSeason} / Episode: {episode.episodeNumber} /
          Airdate: {episode.airDate} / Rating: {episode.rating.toFixed(1)}
        </Typography>
        <ButtonBase
          focusRipple
          className={classes.image}
          focusVisibleClassName={classes.focusVisible}
          style={{
            width: "600px",
            height: "350px",
          }}
          onClick={onClickChange}
        >
          <span
            className={classes.imageSrc}
            style={{
              backgroundImage: `url(${episode.stillPath})`,
            }}
          />
          <span className={classes.imageBackdrop} />
          <span className={classes.imageButton}>
            <Typography
              component="span"
              variant="subtitle1"
              color="inherit"
              className={classes.imageTitle}
            >
              Watched{watched === true ? "!" : "?"}
              <span className={classes.imageMarked} />
            </Typography>
          </span>
        </ButtonBase>
      </CardContent>
      <div className={classes.controls}>{episode.overview}</div>
      {watched === true ? (
        <div>
          <Box component="fieldset" borderColor="transparent">
            <Typography component="legend" style={{ color: "#fff" }}>
              How was it?
            </Typography>
            <Rating
              name="customized-icons"
              IconContainerComponent={IconContainer}
              value={rate}
              onChange={onChangeRating}
            />
          </Box>
          <div className={classes.button}>
            <ButtonClassic onClick={() => {
              history.push(`/episode/${episode.id}/comments`);
            }}>Display comments</ButtonClassic>
          </div>
        </div>
      ) : null}
    </div>
  );
};

export default EpisodeDetails;
