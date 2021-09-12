import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

import backend from "../apis/backend";

import Grid from "@material-ui/core/Grid";
import BugReportIcon from "@material-ui/icons/BugReport";
import ReportProblemIcon from "@material-ui/icons/ReportProblem";
import MoodBadIcon from "@material-ui/icons/MoodBad";

const REPORT_TYPE = [
  {
    value: "SPOILER_COMMENT",
    label: "Spoiler",
  },
  {
    value: "INAPPROPRIATE_COMMENT",
    label: "Inappropriate comment",
  },
  {
    value: "BUG",
    label: "Bug",
  },
];

const ListReportsAdminFirstPage = () => {
  const [reports, setReports] = useState([]);
    const [viewAll, setViewAll] = useState(false);

  useEffect(() => {
    const getReports = async () => {
      await backend
        .get("/report/view-all")
        .then((res) => setReports(res.data))
        .catch((err) => console.log(err.response));
    };
    getReports();
  }, []);

  const renderReports = () => {
    return reports.map((r) => {
      let isViewed = "NO";
      if (r.isViewed) isViewed = "YES";
      return (
        (viewAll && r.isViewed) || !r.isViewed ? 
        <tr key={r.id}>
          <td>{r.id}</td>
          <td>
            <Grid item container>
              {r.reportType === REPORT_TYPE[0].value ? (
                <ReportProblemIcon />
              ) : r.reportType === REPORT_TYPE[1].value ? (
                <MoodBadIcon />
              ) : (
                <BugReportIcon />
              )}{" "}
              <span>&nbsp;&nbsp;</span>
              {REPORT_TYPE.find((x) => x.value === r.reportType).label}
            </Grid>
          </td>
          <td>{r.sentDate}</td>
          <td>{isViewed}</td>
          <td className="selectable">
            <Link to={`/admin/report/${r.id}`} className="item">
              View report
            </Link>
          </td>
        </tr>: null
      );
    });
  };

  return (
    <React.Fragment>
      <table className="ui celled table">
        <thead>
          <tr>
            <th colSpan="5">Reports</th>
          </tr>
          <tr>
            <th>ID</th>
            <th>Report type</th>
            <th>Sent date</th>
            <th>Viewed</th>
            <th></th>
          </tr>
        </thead>
        <tbody>{renderReports()}</tbody>
      </table>
      {!viewAll ? <button className="ui left floated button" onClick={() => setViewAll(true)}>View all reports</button>: null}
    </React.Fragment>
  );
};

export default ListReportsAdminFirstPage;
