import React, { useState, useEffect } from "react";
import { useHistory } from "react-router";
import { makeStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import Typography from "@material-ui/core/Typography";
import Badge from "@material-ui/core/Badge";
import MenuItem from "@material-ui/core/MenuItem";
import Menu from "@material-ui/core/Menu";
import AccountCircle from "@material-ui/icons/AccountCircle";
import NotificationsIcon from "@material-ui/icons/Notifications";
import ExploreIcon from "@material-ui/icons/Explore";
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';

import LinkClassic from "../components/links/LinkClassic";
import SearchBarUser from "../components/SearchBarUser";

import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import backend from "../apis/backend";
import ModalOrg from "../components/ModalOrg";
import Friends from "../users/Friends";

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  bar: {
    backgroundColor: "#282828",
  },
  explore: {
    marginRight: theme.spacing(2),
  },
  title: {
    display: "none",
    [theme.breakpoints.up("sm")]: {
      display: "block",
      fontFamily: "ProximaNovaSB",
    },
  },
  sectionDesktop: {
    display: "none",

    [theme.breakpoints.up("md")]: {
      display: "flex",
    },
  },
  exploreLabel: {
    marginLeft: theme.spacing(1),
    fontSize: "1.2rem",
    fontFamily: "ProximaNovaSB",
  },
}));

const NavbarUser = ({ logout }) => {
  const classes = useStyles();
  const [tvShowName, setTvShowName] = useState("");
  const [notificationNo, setNotificationNo] = useState(0);
  const [open, setOpen] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  const [messageNotification, setMessageNotification] = useState("");
  const idUser = localStorage.getItem("ID session");
  const history = useHistory();
  const [anchorEl, setAnchorEl] = useState(null);
  
  useEffect(() => {
    const getRequests = async () => {
      await backend
        .get(`friends/all-requests/${idUser}`)
        .then((res) => {
          setNotificationNo(res.data.length);
        })
        .catch((err) => console.log(err.response));
    };
    const connect = () => {
      const URL = "http://localhost:8080/socket";
      const websocket = new SockJS(URL);
      const stompClient = Stomp.over(websocket);
      stompClient.connect({}, (frame) => {
        stompClient.subscribe(
          `/topic/socket/user/send-request/${idUser}`,
          (notification) => {
            let message = notification.body;
            setMessageNotification(message);
            setOpenNotification(true);
            setNotificationNo(notificationNo + 1);           
          }
        );
        stompClient.subscribe(
          `/topic/socket/user/accept-request/${idUser}`,
          (notification) => {
            let message = notification.body;
            setMessageNotification(message);
            setOpenNotification(true);       
          }
        );
      });
    };
    getRequests();
    connect();
  }, [idUser, notificationNo]);

  const onChangeNotification = () => {
    setNotificationNo(notificationNo - 1);
  };

  const isMenuOpen = Boolean(anchorEl);
  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const menuId = "primary-search-account-menu";
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{ vertical: "top", horizontal: "right" }}
      id={menuId}
      keepMounted
      transformOrigin={{ vertical: "top", horizontal: "right" }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <LinkClassic href="/">
        <MenuItem
          onClick={() => {
            handleMenuClose();
          }}
        >
          Profile
        </MenuItem>
      </LinkClassic>
      <LinkClassic href="/profile/edit">
        <MenuItem
          onClick={() => {
            handleMenuClose();
          }}
        >
          Edit
        </MenuItem>
      </LinkClassic>
      <LinkClassic href="/report/bug">
        <MenuItem
          onClick={() => {
            handleMenuClose();
          }}
        >
          Report bug
        </MenuItem>
      </LinkClassic>
      <MenuItem
        onClick={() => {
          handleMenuClose();
          logout();
        }}
      >
        Log out
      </MenuItem>
    </Menu>
  );

  const onFormChange = (event) => {
    setTvShowName(event.target.value);
  };

  const onFormSubmit = () => {
    history.push(`/search/${tvShowName}`);
  };

  return (
    <div className={classes.grow}>
      <AppBar position="static" className={classes.bar}>
        <Toolbar>
          <LinkClassic href="/" style={{ color: "#fff" }}>
            <Typography className={classes.title} variant="h4" noWrap>
              TST
            </Typography>
          </LinkClassic>
          <SearchBarUser
            term={tvShowName}
            placeholder="Search..."
            onFormChange={onFormChange}
            onFormSubmit={onFormSubmit}
          />
          <LinkClassic href="/tv-shows/explore" style={{ color: "#fff" }}>
            <IconButton className={classes.explore} color="inherit">
              <ExploreIcon fontSize="large" />
              <Typography className={classes.exploreLabel} noWrap>
                Explore
              </Typography>
            </IconButton>
          </LinkClassic>
          <div className={classes.grow} />
          <div className={classes.sectionDesktop}>
            <IconButton
              aria-label="show new notifications"
              color="inherit"
              onClick={() => {
                if (notificationNo !== 0) setOpen(true);
              }}
            >
              <Badge badgeContent={notificationNo} color="secondary">
                <NotificationsIcon fontSize="large" />
              </Badge>
            </IconButton>
            <IconButton
              edge="end"
              aria-label="account of current user"
              aria-controls={menuId}
              aria-haspopup="true"
              onClick={handleProfileMenuOpen}
              color="inherit"
            >
              <AccountCircle fontSize="large" />
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
      {open ? (
        <ModalOrg
          open={open}
          onClose={() => {
            setOpen(false);          
          }}
          children={<Friends viewFriends={false} onChangeNotification={onChangeNotification} />}
        />
      ) : null}
      <Snackbar open={openNotification} autoHideDuration={6000} onClose={() => setOpenNotification(false)}>
        <Alert onClose={() => setOpenNotification(false)} severity="info">{messageNotification}</Alert>
      </Snackbar>
      {renderMenu}
    </div>
  );
};

export default NavbarUser;
