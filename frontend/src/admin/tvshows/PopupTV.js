import React from "react";

const PopupTV = ({
  image,
  header,
  content,
  extrabtn,
  msg,
  togglePopout,
  onAddEvent,
}) => {
  const renderedAddButton = () => {
    if (extrabtn) {
      return null;
    }

    return (
      <div className="ui positive button" onClick={onAddEvent}>
        <i className="add icon"></i>
        Add
      </div>
    );
  };

  const renderedMsg = () => {
    var typeMsg = "yellow";
    if (extrabtn) typeMsg = "red";
    return (
      <div className={`ui ${typeMsg} message`}>
        <div className="header" style={{ textAlign: "center" }}>
          {msg}
        </div>
      </div>
    );
  };

  return (
    <div className="ui segment">
      <div className="ui popout">
        <div className="ui items">
          <div className="item">
            <div className="image">
              <img alt="Not found" src={image} widtg="200" hight="200" />
            </div>
            <div className="content">
              <h3 className="header">{header}</h3>
              <div className="meta">
                <span>Overview</span>
              </div>
              <div className="description">
                <p>{content}</p>
              </div>
            </div>
          </div>
        </div>
        {renderedMsg()}
        <div className="ui two bottom attached buttons">
          {renderedAddButton()}
          <div className="ui red button" onClick={togglePopout}>
            <i className="x icon"></i>
            Cancel
          </div>
        </div>
      </div>
    </div>
  );
};

export default PopupTV;
