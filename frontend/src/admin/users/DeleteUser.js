import React from "react";

const DeleteUser = ({ user, cancelDelete, deleteUser }) => {
  return (
    <div className="ui segment">
      <div className="ui popout">
        <div className="ui items">
          <div className="item">
            <h3 className="header center">
              Are you sure you want to delele {user.username} ?
            </h3>
          </div>
        </div>
        <div className="ui two bottom attached buttons">
          <div className="ui green button" onClick={deleteUser}>
            <i className="x icon"></i>
            Confirm
          </div>
          <div className="ui red button" onClick={cancelDelete}>
            <i className="x icon"></i>
            Cancel
          </div>
        </div>
      </div>
    </div>
  );
};

export default DeleteUser;
