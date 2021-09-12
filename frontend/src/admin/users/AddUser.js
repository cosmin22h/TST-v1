import React, { useState } from "react";
import backend from "../../apis/backend";

import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";

const roles = [
  {
    label: "Admin",
    value: "ADMIN",
  },
  {
    label: "User",
    value: "USER",
  },
];

const AddUser = () => {
  const [newUser, setNewUser] = useState({
    username: "",
    email: "",
    role: roles[1].value,
    isActive: false,
    password: "",
  });
  const [backdrop, setBackdrop] = useState(false);

  const onChangeForm = (event) => {
    const target = event.target;
    const value = target.type === "checkbox" ? target.checked : target.value;
    const name = target.name;

    setNewUser({
      ...newUser,
      [name]: value,
    });
  };

  const onFormSubmit = async (e) => {
    e.preventDefault();
    setBackdrop(true);
    var userRole;
    if (newUser.role === "ADMIN") {
      userRole = "admin";
    } else {
      userRole = "user";
    }
    if (newUser.password.length < 8) {
      alert("Password must have at least 8 characters");
      setBackdrop(false);
      return;
    }
    await backend
      .post(`/${userRole}/add`, newUser)
      .then(() => {
        setBackdrop(false);
        alert("New " + userRole + " addeed");
        window.location.assign("/admin/users");
      })
      .catch((err) => {
        let error = err.response.data;
        if (error.message.includes("Bad request")) {
          alert("Password must contains  at least 1 uppercase, 1 lowercase, 1 digit, 1 special character")
        }
        else {
          alert(error.message);
        }
        setBackdrop(false);
      });
  };

  const renderedRoles = () => {
    return roles.map((role) => {
      return (
        <option key={role.value} value={role.value}>
          {role.label}
        </option>
      );
    });
  };

  return (
    <React.Fragment>
      {backdrop ? (
        <Backdrop
          open={backdrop}
          style={{
            color: "#fff",
          }}
        >
          <CircularProgress color="inherit" />
        </Backdrop>
      ) : (
        <form className="ui form" onSubmit={onFormSubmit}>
          <div className="field">
            <label>Username</label>
            <input
              type="text"
              className="form-control"
              id="username"
              name="username"
              placeholder="Username"
              required
              value={newUser.username}
              onChange={onChangeForm}
            />
          </div>
          <div className="field">
            <label>Email</label>
            <input
              type="email"
              className="form-control"
              id="email"
              name="email"
              placeholder="Email"
              required
              value={newUser.email}
              onChange={onChangeForm}
            />
          </div>
          <div className="field">
            <label>Role</label>
            <select
              className="ui dropdown"
              name="role"
              value={newUser.role}
              onChange={onChangeForm}
            >
              {renderedRoles()}
            </select>
          </div>
          <div className="field">
            <div className="ui toggle checkbox">
              <input
                type="checkbox"
                id="isActive"
                name="isActive"
                checked={newUser.isActive}
                onChange={onChangeForm}
              />
              <label>Active</label>
            </div>
          </div>
          <div className="field">
            <label>Password</label>
            <input
              type="password"
              className="form-control"
              id="password"
              name="password"
              placeholder="Password"
              required
              value={newUser.password}
              onChange={onChangeForm}
            />
          </div>
          <button type="submit" className="fluid green ui button">
            Add
          </button>
        </form>
      )}
    </React.Fragment>
  );
};

export default AddUser;
