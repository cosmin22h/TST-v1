import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import backend from "../../apis/backend";

const EditUser = () => {
  const { id } = useParams();
  const [user, setUser] = useState({
    username: "",
    email: "",
    isActive: false,
  });
  const [updatedUser, setUpdatedUser] = useState({
    username: "",
    email: "",
    isActive: false,
  });

  useEffect(() => {
    const getUser = async () => {
      try {
        const response = await backend.get(`/basic-user/${id}`);
        setUser(response.data);
        setUpdatedUser(response.data);
      } catch (error) {
        console.log(error.response);
      }
    };

    getUser();
  }, [id]);

  const onChangeForm = (event) => {
    const target = event.target;
    const value = target.type === "checkbox" ? target.checked : target.value;
    const name = target.name;

    setUpdatedUser({
      ...updatedUser,
      [name]: value,
    });
  };

  const onFormSubmit = async () => {
    try {
      await backend.put(`/basic-user/${id}/edit`, updatedUser);
    } catch {
      alert("Username/Email already exists");
    }
  };

  return (
    <form className="ui form" onSubmit={onFormSubmit}>
      <div className="field">
        <label>Username</label>
        <input
          type="text"
          className="form-control"
          id="username"
          name="username"
          value={updatedUser.username}
          required
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
          value={updatedUser.email}
          required
          onChange={onChangeForm}
        />
      </div>
      <div className="field">
        <div className="ui toggle checkbox">
          <input
            type="checkbox"
            id="isActive"
            name="isActive"
            checked={updatedUser.isActive}
            onChange={onChangeForm}
          />
          <label>Active</label>
        </div>
      </div>
      {JSON.stringify(user) !== JSON.stringify(updatedUser) ? (
        <div>
          <button type="submit" className="fluid green ui button">
            Save
          </button>
        </div>
      ) : null}
    </form>
  );
};

export default EditUser;
