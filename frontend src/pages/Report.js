import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

import {
  Container,
  makeStyles,
  TextareaAutosize,
  Grid,
  Typography,
} from "@material-ui/core";
import BugReportIcon from "@material-ui/icons/BugReport";
import ReportProblemIcon from "@material-ui/icons/ReportProblem";
import MoodBadIcon from "@material-ui/icons/MoodBad";

import ButtonOrg from "../components/buttons/ButtonOrg";
import backend from "../apis/backend";

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
    color: "#fff",
    borderBottom: "2px solid #696969",
    padding: "8px 10px",
  },
  form: {
    width: "100%",
    marginTop: theme.spacing(1),
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

const REPORT_TYPE = [
  {
    value: "SPOILER_COMMENT",
    label: "Report spoiler",
  },
  {
    value: "INAPPROPRIATE_COMMENT",
    label: "Report inappropriate comment",
  },
  {
    value: "BUG",
    label: "Report bug",
  },
];

const Report = ({ type }) => {
  const classes = useStyles();
  const [report, setReport] = useState({
    reportType: null,
    content: "",
    idUser: null,
  });
  const id = useParams();
  const [comment, setComment] = useState(null);

  useEffect(() => {
    setReport({
      reportType: type,
      idUser: localStorage.getItem("ID session"),
    });
    const getComment = async () => {
      await backend
        .get(`/comment/${id.id}`)
        .then((res) => setComment(res.data))
        .catch((err) => console.log(err.response));
    };
    if (id.id !== undefined) {
      getComment();
    }
  }, [type, id]);

  const onFormChange = (event) => {
    const target = event.target;
    const name = target.name;
    const value = target.value;

    setReport({
      ...report,
      [name]: value,
    });
  };

  const onFormSubmit = (e) => {
    e.preventDefault();
    const submitReport = async () => {
      let commentId = null;
      if (comment !== null) commentId = comment.id;
      let reportToCreate = {
        reportType: report.reportType,
        content: report.content,
        idUser: report.idUser,
        idComment: commentId,
      };
      await backend
        .post("/report/add", reportToCreate)
        .catch((err) => console.log(err.response));
    };
    submitReport();
    window.location.assign("/");
  };

  return (
    <Container maxWidth="sm" className={classes.root}>
      <div className={classes.paper}>
        <Container maxWidth="sm" className={classes.title}>
          <Typography variant="inherit" style={{ fontSize: "16px" }}>
            <Grid item container>
              {type === REPORT_TYPE[0].value ? (
                <ReportProblemIcon />
              ) : type === REPORT_TYPE[1].value ? (
                <MoodBadIcon />
              ) : (
                <BugReportIcon />
              )}
              <span>&nbsp;</span>
              {REPORT_TYPE.find((x) => x.value === type).label}{" "}
            </Grid>
          </Typography>
        </Container>
        <form className={classes.form} onSubmit={onFormSubmit}>
          {type !== REPORT_TYPE[2].value ? (
            <TextareaAutosize
              disabled
              className={classes.textarea}
              value={comment !== null ? comment.content : ""}
              style={{ resize: "none", width: "100%" }}
            />
          ) : null}
          <TextareaAutosize
            className={classes.textarea}
            required
            rowsMin={10}
            id="content"
            name="content"
            value={report.content}
            onChange={onFormChange}
            placeholder="Give the context on what happened"
          />
          <ButtonOrg type="submit" fullWidth variant="contained">
            Submit
          </ButtonOrg>
        </form>
      </div>
    </Container>
  );
};

export default Report;
