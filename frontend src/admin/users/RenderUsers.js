import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import backend from "../../apis/backend";
import DeleteUser from "./DeleteUser";

const RenderUsers = () => {
  const [users, setUsers] = useState({ list: [], seletedUser: null });
  const [deleteUser, setDeleteUser] = useState(false);
  const [refresh, setRefresh] = useState(0);

  useEffect(() => {
    getUsers();
  }, [refresh]);

  const getUsers = async () => {
    try {
      const response = await backend.get("/basic-user/view");
      setUsers({ list: response.data });
    } catch (error) {
      console.log(error.response);
    }
  };

  const handleInputChange = (user) => {
    setDeleteUser(false);
    var seletedUser = null;
    if (user !== users.seletedUser) {
      seletedUser = user;
    }
    setUsers({ list: users.list, seletedUser: seletedUser });
  };

  const renderedList = () => {
    if (users.list.length !== 0) {
      return users.list.map((user) => {
        var isActive = "False";
        if (user.isActive) {
          isActive = "True";
        }
        var isLogin = "False";
        if (user.authSession.isActive) {
          isLogin = "True";
        }
        return (
          <tr key={user.id}>
            <td className="collapsing">
              <div className="ui fitted slider check checkbox">
                <input
                  type="checkbox"
                  checked={user === users.seletedUser ? true : false}
                  onChange={() => handleInputChange(user)}
                />
                <label></label>
              </div>
            </td>
            <td>{user.username}</td>
            <td>{user.email}</td>
            <td>{isActive}</td>
            <td>{user.role}</td>
            <td>{user.dateJoined}</td>
            <td>{isLogin}</td>
            <td>{user.authSession.dateLastLogin}</td>
            <td>{user.authSession.isActive ? "" : user.authSession.dateLastLogout}</td>
          </tr>
        );
      });
    }
    return null;
  };

  const onCancel = () => {
    setDeleteUser(false);
    setUsers({ list: users.list, seletedUser: null });
  };

  const confirmDeleteUser = async () => {
    onCancel();
    try {
      await backend.delete(`/basic-user/${users.seletedUser.id}/delete`);
    } catch (error) {
      console.log(error.response);
    }
    setRefresh(!refresh);
  };
  return (
    <div>
      <table className="ui compact celled definition table">
        <thead className="full-width">
          <tr>
            <th></th>
            <th>Username</th>
            <th>Email</th>
            <th>Active account</th>
            <th>Role</th>
            <th>Date Joined</th>
            <th>Active session</th>
            <th>Last login</th>
            <th>Last logout</th>
          </tr>
        </thead>
        <tbody>{renderedList()}</tbody>
        <tfoot className="full-width">
          <tr>
            <th colSpan="9">
              <Link to="/admin/users/add">
                <div className="ui right floated small primary labeled icon button">
                  <i className="user icon" />
                  Add User
                </div>
              </Link>
              {users.seletedUser !== undefined && users.seletedUser !== null ? (
                <div>
                  <div
                    className="ui small red button"
                    onClick={() => setDeleteUser(true)}
                  >
                    Delete User
                  </div>
                  <Link to={`/admin/users/update/${users.seletedUser.id}`}>
                    <div className="ui small black button">Edit User</div>
                  </Link>
                </div>
              ) : null}
            </th>
          </tr>
        </tfoot>
      </table>
      {deleteUser ? (
        <DeleteUser
          user={users.seletedUser}
          cancelDelete={onCancel}
          deleteUser={confirmDeleteUser}
        />
      ) : null}
      <br/>
    </div>
  );
};

export default RenderUsers;
