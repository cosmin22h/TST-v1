import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { saveAs } from "file-saver";

import backend from "../apis/backend";

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

const ReportAdmin = () => {
  const { id } = useParams();
  const [report, setReport] = useState({
    id: 0,
    reportType: "",
    content: "",
    sentDate: "",
    username: "",
    comment: null,
  });
  const [episode, setEpisode] = useState(null);
  const [viewComment, setViewComment] = useState(false);

  useEffect(() => {
    const getReport = async () => {
      await backend
        .get(`/report/${id}`)
        .then((res) => {
          setReport(res.data);
          if (!res.data.isViewed) markReport();
          if (res.data.reportType !== REPORT_TYPE[2].value) {
            const idComment = res.data.comment.id;
            getComment(idComment);
          }
        })
        .catch((err) => console.log(err.response));
    };
    const markReport = async () => {
      await backend
        .put(`/report/mark/${id}`)
        .catch((err) => console.log(err.response));
    };
    const getComment = async (idComment) => {
      await backend
        .get(`/comment/${idComment}`)
        .then((res) => setEpisode(res.data.episode))
        .catch((err) => console.log(err.response));
    };
    getReport();
  }, [id]);

  const type = REPORT_TYPE.find((x) => x.value === report.reportType);
  const commentReported = report.comment;
  var userReported = null;
  if (report.comment !== null) {
    userReported = report.comment.user;
  }

  const onClickDelete = (idComment) => {
    const deleteComment = async () => {
      onClickExport("xml");
      await backend
        .delete(`/report/delete/${id}`)
        .then(() => {
          onDeleteReport();
        })
        .catch((err) => console.log(err.response));
    };
    const onDeleteReport = async () => {
      await backend
        .delete(`/comment/remove/${idComment}`)
        .then(() => window.location.assign("/"))
        .catch((err) => console.log(err.response));
    };
    deleteComment();
  };

  const onClickSpoiler = (idComment, isSpoiler) => {
    const spoilerComment = async () => {
      let data = {
        isSpoiler: isSpoiler,
      };
      await backend
        .put(`/comment/edit/${idComment}`, data)
        .then(() => window.location.reload())
        .catch((err) => console.log(err.response));
    };
    spoilerComment();
  };

  const onClickExport = (fileType) => {
    const exportReport = async () => {
      await backend
        .get(`/report/export/${report.id}`, {
          params: { fileType: fileType },
          responseType: "text",
        })
        .then((res) => {
          let typeForBlob =
            fileType == "txt"
              ? "text/plain;charset=utf-8"
              : "text/xml;charset=utf-8";
          let blob = new Blob([res.data], { type: typeForBlob });
          saveAs(blob, `report-data-${report.id}.` + fileType);
        })
        .catch((err) => console.log(err.response));
    };
    exportReport();
  };

  return (
    <React.Fragment>
      <form className="ui form">
        <div className="fields">
          <div className="six wide field">
            <label>ID</label>
            <input type="number" readOnly value={report.id} />
          </div>
          <div className="six wide field">
            <label>Report type</label>
            <input
              type="text"
              readOnly
              className="form-control"
              value={type === undefined ? "" : type.label}
            />
          </div>
          <div className="six wide field">
            <label>Sent date</label>
            <input
              type="text"
              readOnly
              className="form-control"
              value={report.sentDate}
            />
          </div>
        </div>
        <div className="field">
          <label>
            User
            <input
              type="text"
              readOnly
              className="form-control"
              value={report.username}
            />
          </label>
        </div>
        <div className="field">
          <label>
            Report
            <textarea readOnly value={report.content} />
          </label>
        </div>
      </form>
      {viewComment ? (
        <div>
          <br />
          <div className="ui segment">
            <span>
              <b>User reported:</b> &nbsp;&nbsp;
            </span>
            <Link to={`/admin/users/update/${userReported.id}`}>
              {userReported.username}
            </Link>
            <div className="metadata">
              <span className="date">
                <b>Post date:</b> {commentReported.postDate}
              </span>
            </div>
            <div className="text">
              {" "}
              <span>
                <b>Comment:</b> &nbsp;&nbsp;
              </span>
              {commentReported.content}
            </div>
          </div>
        </div>
      ) : null}
      {episode !== null ? (
        <div>
          <br />
          <Link
            to={`/admin/tvshows/show/${episode.tvShow.id}/episode/${episode.id}`}
          >
            <button className="ui left floated button">View episode</button>
          </Link>
          {!viewComment ? (
            <button
              className="ui left floated button"
              onClick={() => setViewComment(true)}
            >
              View comment
            </button>
          ) : null}
          {viewComment ? (
            <div>
              <button
                className="ui negative right floated button"
                onClick={() => onClickDelete(commentReported.id)}
              >
                Delete comment
              </button>
              {!commentReported.isSpoiler ? (
                <button
                  className="ui yellow right floated button"
                  onClick={() => onClickSpoiler(commentReported.id, true)}
                >
                  Mark as spoiler
                </button>
              ) : (
                <button
                  className="ui green right floated button"
                  onClick={() => onClickSpoiler(commentReported.id, false)}
                >
                  Mark as not spoiler
                </button>
              )}
            </div>
          ) : null}
        </div>
      ) : (
        <br />
      )}
      <button
        className="ui left floated button"
        onClick={() => onClickExport("xml")}
      >
        Export XML
      </button>
      <button
        className="ui left floated button"
        onClick={() => onClickExport("txt")}
      >
        Export TXT
      </button>
    </React.Fragment>
  );
};

export default ReportAdmin;
