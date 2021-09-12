import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

import {
  makeStyles,
  Typography,
  Paper,
  Modal,
  TableBody,
  TableRow,
  TableCell,
  TableContainer,
  Button,
} from "@material-ui/core";

import Grid from "@material-ui/core/Grid";
import ClassicButton from "../../components/buttons/ButtonClassic";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import Accordion from "@material-ui/core/Accordion";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import Table from "@material-ui/core/Table";
import VisibilityIcon from "@material-ui/icons/Visibility";
import TableHead from "@material-ui/core/TableHead";

import backend from "../../apis/backend";
import youtube from "../../apis/youtube";
import EpisodeDetails from "./EpisodeDetails";
import ModalOrg from "../../components/ModalOrg";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    padding: theme.spacing(3),
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: "center",
    backgroundColor: "#424242",
    color: "#fff",
    border: "1px solid #696969",
  },
  text: {
    fontFamily: "ProximaNova",
    color: "#fff",
    padding: theme.spacing(1),
  },
  modal: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  tableCell: {
    color: "#fff",
  },
  icon: {
    color: "#fff",
  },
}));

const options = [
  {
    label: "To Watch",
    value: "TO_WATCH",
    disable: false,
  },
  {
    label: "Favorites",
    value: "FAVORITES",
    disable: false,
  },
  {
    label: "Remove/Stopped",
    value: "TO_WATCH",
    disable: false,
  },
];

const TvShowDetails = () => {
  const { id } = useParams();
  const classes = useStyles();

  const [tvshow, setTvshow] = useState({
    id: null,
    name: "",
    genres: [],
    noOfSeasons: 0,
    noOfEpisodes: 0,
    status: "",
    overview: "",
    networks: [],
    episodeRunTime: 0,
    firstAirDate: null,
    lastAirDate: null,
    nextAirDate: null,
    rating: 0,
    episodes: [],
  });
  const [open, setOpen] = useState(false);
  const [trailer, setTrailer] = useState(null);
  const [openEpisode, setOpenEpisode] = useState(false);
  const [episode, setEpisode] = useState({ ep: null, viewed: { isViewed: false, rating: 0 } });
  const [viewedEpisodes, setViewedEpisodes] = useState([]);
  const [lastEpisodeId, setLastEpisodeId] = useState(null);
  const [anchorEl, setAnchorEl] = useState(null);
  const [expanded, setExpanded] = useState(false);
  const openAddMenu = Boolean(anchorEl);
  const [refresh, setRefresh] = useState(false);

  useEffect(() => {
    const idUser = localStorage.getItem("ID session");
    const getTvShow = async () => {
      await backend.get(`tv-show/${id}`)
      .then(res => {
        const data = res.data;
        setTvshow(data);
        setLastEpisodeId(data.episodes[data.episodes.length - 1].id);
      })
      .catch(err => {
        console.log(err.response);
      });
      
    };
    const getActiveLists = async () => {
      await backend.get(
        `/lists/find-in-list/${idUser}/TO_WATCH/${id}`
      ).then(res => {
        const lists = res.data;
        if (lists.length === 0) {
          options[0].disable = false;
          options[1].disable = true;
          options[2].disable = true;
        } else if (lists.includes(options[1].value)) {
          options[0].disable = true;
          options[1].disable = false;
          options[2].disable = true;
          options[1].label = "Remove from Favorites";
        } else if (lists.includes('STOPPED')) {
          options[0].disable = true;
          options[1].disable = true;
          options[2].disable = true;
        } else {
          options[0].disable = true;
          options[1].disable = false;
          options[2].disable = false;
          options[1].label = "Favorites";
        }
      }).catch(err => {
        console.log(err.response);
      });
    };
    const getViewdEpiosodes = async () => {
      await backend.get(`/episode-viewed/tv-show/${id}/${idUser}`)
      .then(res => {
        setViewedEpisodes(res.data);
      })
      .catch(err => {
        console.log(err.response);
      });
      
    };
    getTvShow();
    getActiveLists();
    getViewdEpiosodes();
  }, [id, refresh]);

  const getTrailer = async (name) => {
    const response = await youtube.get("/search", {
      params: {
        q: name + " official trailer season 1",
      },
    });
    const data = response.data.items[0];
    setTrailer(data.id.videoId);
  };

  const handleOpen = () => {
    getTrailer(tvshow.name);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleCloseEpisode = () => {
    setOpenEpisode(false);
    setRefresh(!refresh);
  };

  const handleOpenEpisode = (ep) => {
    let rating = isViewed(ep);
    let isViewedBool = false;
    if (rating !== -1) isViewedBool = true;
    setEpisode({ep :ep, viewed: { isViewed: isViewedBool, rating: rating}});
    setOpenEpisode(true);
  };
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleCloseAddMenu = () => {
    setAnchorEl(null);
  };

  const handleChange = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : false);
  };

  const isViewed = (ep) => {
    for (var i = 0; i < viewedEpisodes.length; i++) {
      if (ep.id === viewedEpisodes[i].episodeId)
        return viewedEpisodes[i].rating;
    }

    return -1;
  };

  const getEpisodesBySeason = (episodes) => {
    return episodes.map((ep) => {
      return (
        <TableRow key={ep.id}>
          <TableCell className={classes.tableCell} component="th" scope="row">
            {ep.episodeNumber}
          </TableCell>

          <TableCell className={classes.tableCell} align="left">
            {options[0].disable ?
            <Button
              onClick={() => handleOpenEpisode(ep)}
              style={{ color: "#fff" }}
            >
              {ep.name}
            </Button> : ep.name
            }
          </TableCell>

          <TableCell className={classes.tableCell} align="left">
            {ep.airDate}
          </TableCell>
          <TableCell className={classes.tableCell} align="right">
            {isViewed(ep) !== -1 ? <VisibilityIcon className={classes.icon} style={{ color: "#ffa25f" }} />:
            <VisibilityIcon className={classes.icon} />}
          </TableCell>
        </TableRow>
      );
    });
  };

  const getAccordions = (no, episodes) => {
    const episodesSeasons = episodes.filter((ep) => ep.noSeason === no);
    return (
      <Accordion
        className={classes.paper}
        key={no}
        expanded={expanded === `panel${no}`}
        onChange={handleChange(`panel${no}`)}
      >
        <AccordionSummary
          expandIcon={<ExpandMoreIcon className={classes.icon} />}
        >
          <Typography className={classes.heading}>Seasons {no}</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell className={classes.tableCell}>No.</TableCell>
                  <TableCell className={classes.tableCell} align="left">
                    Name
                  </TableCell>
                  <TableCell className={classes.tableCell} align="left">
                    Air date
                  </TableCell>
                  <TableCell
                    className={classes.tableCell}
                    align="right"
                  ></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>{getEpisodesBySeason(episodesSeasons)}</TableBody>
            </Table>
          </TableContainer>
        </AccordionDetails>
      </Accordion>
    );
  };

  const getSeaons = () => {
    const seasons = [];
    for (var i = 0; i < tvshow.noOfSeasons; i++) {
      seasons.push(getAccordions(i + 1, tvshow.episodes));
    }
    return seasons;
  };

  const onAddSubmit = (list) => {
    let tvShowToBack = {
      type: list.value,
      idUser: localStorage.getItem("ID session"),
      idTvShow: tvshow.id,
    };
    const addToList = async (type) => {
      if (type === 'STOPPED') tvShowToBack.type = type;
      await backend.post("/lists/add-to-list", tvShowToBack)
      .then(() => {
        setRefresh(!refresh);
      })
      .catch(err => {
        console.log(err.response);
      });
    
    };
    const removeFromList = async () => {
      await backend.delete(
        `/lists/remove-from-list/${tvShowToBack.type}/${tvShowToBack.idTvShow}/${tvShowToBack.idUser}`
      ).then(() => {
        setRefresh(!refresh);
      })
      .catch(err => {
        console.log(err.response);
      });
    };
    const addValues = ["To Watch", "Favorites"];
    if (addValues.indexOf(list.label) !== -1) {
      addToList();
    } else {
      removeFromList();
      if (list.value === 'TO_WATCH' && viewedEpisodes.length !== 0) {  
        addToList('STOPPED');
      }
    }
    
  };

  return (
    <div className={classes.root}>
      {tvshow !== null ? (
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              {tvshow !== null ? (
                <Typography
                  className={classes.text}
                  component="h4"
                  variant="h4"
                  style={{ textAlign: "left" }}
                >
                  {tvshow.name} (Score: {tvshow.rating.toFixed(1)}/5){" "}
                  <ClassicButton
                    onClick={handleClick}
                    style={{ fontSize: "0.8rem", width: "15%", float: "right" }}
                  >
                    Add TVshow
                  </ClassicButton>
                  <Menu
                    anchorEl={anchorEl}
                    keepMounted
                    open={openAddMenu}
                    onClose={handleCloseAddMenu}
                  >
                    {options.map((option) => (
                      <MenuItem
                        key={option.label}
                        onClick={() => {
                          handleCloseAddMenu();
                          onAddSubmit(option);
                        }}
                        disabled={option.disable}
                      >
                        {option.label}
                      </MenuItem>
                    ))}
                  </Menu>
                </Typography>
              ) : null}
            </Paper>
          </Grid>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              {" "}
              <Typography
                className={classes.text}
                style={{ textAlign: "left" }}
              >
                {tvshow.status} / {tvshow.noOfSeasons} seasons /{" "}
                {tvshow.noOfEpisodes} episodes / {tvshow.episodeRunTime} minutes
              </Typography>
              <Typography
                className={classes.text}
                style={{ textAlign: "left", color: "rgba(255, 255, 255, 0.7)" }}
              >
                {tvshow.overview}
              </Typography>
              <Typography
                className={classes.text}
                style={{ textAlign: "left" }}
              >
                Genres: {tvshow.genres.join(", ")}
              </Typography>
              <Typography
                className={classes.text}
                style={{ textAlign: "left" }}
              >
                Networks: {tvshow.networks.join(", ")}
              </Typography>
              <Typography
                className={classes.text}
                style={{ textAlign: "left" }}
              >
                First episode: {tvshow.firstAirDate} / Last episode:{" "}
                {tvshow.lastAirDate}{" "}
                {tvshow.nextAirDate !== null
                  ? "/ Next episode: " + tvshow.nextAirDate
                  : ""}
              </Typography>
              <br />
              <ClassicButton onClick={handleOpen}>View trailer</ClassicButton>
            </Paper>
          </Grid>
          <Grid item xs={12}>
            {getSeaons()}
          </Grid>
        </Grid>
      ) : null}
      {open === true ? (
        <Modal
          className={classes.modal}
          open={open}
          onClose={handleClose}
          children={
            <iframe
              title="video player"
              src={`https://www.youtube.com/embed/${trailer}`}
              width="854"
              height="480"
            />
          }
        />
      ) : null}
      {openEpisode === true ? (
        <ModalOrg
          className={classes.modal}
          open={openEpisode}
          onClose={handleCloseEpisode}
          children={<EpisodeDetails 
            episodeToProp={episode}
            idTvShow={id}
            isStopped={options[0].disable && options[1].disable && options[2].disable}
            isLastEpiosde={episode.ep.id === lastEpisodeId}
            />}
        />
      ) : null}
    </div>
  );
};

export default TvShowDetails;
