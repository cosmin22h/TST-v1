import React from "react";
import {
  makeStyles,
  ThemeProvider,
  Modal,
  Backdrop,
  Fade,
} from "@material-ui/core";
import darkTheme from "../components/Theme";

const useStyles = makeStyles((theme) => ({
  modal: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    overflow:'scroll',

  },
  paperFade: {
    position: "absolute",
    top: "10%",
    backgroundColor: "#30302F",
    border: "2px solid #fff",
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
  },
}));

const ModalOrg = ({ children, open, onClose }) => {
  const classes = useStyles();

  return (
    <ThemeProvider theme={darkTheme}>
      <Modal
        aria-labelledby="transition-modal-title"
        aria-describedby="transition-modal-description"
        className={classes.modal}
        open={open}
        onClose={onClose}
        closeAfterTransition
        BackdropComponent={Backdrop}
        BackdropProps={{
          timeout: 500,
        }}
      >
        <Fade in={open}>
          <div className={classes.paperFade}>{children}</div>
        </Fade>
      </Modal>
    </ThemeProvider>
  );
};

export default ModalOrg;
