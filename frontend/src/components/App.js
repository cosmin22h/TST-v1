import React from "react";
import { BrowserRouter, Route, Redirect } from "react-router-dom";
import AdminNavBar from "../admin/AdminNavbar";
import RenderUsers from "../admin/users/RenderUsers";
import AddUser from "../admin/users/AddUser";
import EditUser from "../admin/users/EditUser";
import ViewTvShows from "../admin/tvshows/ViewTvShows";
import AddTvShows from "../admin/tvshows/AddTvShows";
import TvShow from "../admin/tvshows/TvShow";
import ViewEpisodes from "../admin/episodes/ViewEpisodes";
import Episode from "../admin/episodes/Episode";
import AddEpisode from '../admin/episodes/AddEpisode';
import NavbarUser from "../users/NavbarUser";
import IndexPage from "../pages/IndexPage";
import Profile from "../users/Profile";
import EditProfile from "../users/EditProfile";
import ExploreTvShows from "../users/tvshows/ExploreTvShows";
import TvShowDetails from "../users/tvshows/TvShowDetails";
import Search from '../users/tvshows/Search';
import ListTvShows from "./ListTvShows";
import FriendProfile from "../users/FriendProfile";
import Comments from "../users/tvshows/Comments";
import CommentsAdmin from "../admin/Comments";
import backend from "../apis/backend";
import Report from "../pages/Report";
import ListReportsAdminFirstPage from "../admin/indexAdmin";
import ReportAdmin from "../admin/ReportAdmin";

const GUEST_URLS = ["/register", "/login", "/user/change-password/"];
const INDEX_TYPE = ["LOGIN", "SIGUP", "FORGET_PASSWORD", "RESET_PASSWORD"]
const REPORT_TYPE = ["SPOILER_COMMENT", "INAPPROPRIATE_COMMENT", "BUG"]

const App = () => {
  const session = localStorage.getItem("ID session");
  const user = localStorage.getItem("User");

  const logout = () => {
    const logoutUser = async () => {
      await backend.post(`/basic-user/logout/${session}`)
      .then(() => {
        localStorage.removeItem("ID session");
        localStorage.removeItem("User");
        localStorage.removeItem("token");
        window.location.assign("/");
      })
      .catch(err => console.log(err.response));
    };
    logoutUser();
  };

  if (localStorage.getItem("User") === "USER")
    document.body.style.backgroundColor = "#424242";

  const defaultRouteGuest = () => {
    let location = window.location.pathname;
    let checkLocationGuest = GUEST_URLS.find(url => url === location);
    if (checkLocationGuest !== undefined) return null;
    if (location.indexOf(GUEST_URLS[2]) !== -1) return null;
    return <Redirect to="" />;
  };

  const defaultRouteUser = () => {
    let location = window.location.pathname;
    if (location.indexOf("admin") !== -1) return <Redirect to="" />;

    return null;
  }

  return (
    <BrowserRouter>
      {session !== null ? (
        <React.Fragment>
          {user === "ADMIN" ? (
            <Route path="" render={() => <AdminNavBar logout={logout} />} />
          ) : (
            <Route path="" render={() => <NavbarUser logout={logout} />} />
          )}
          <div className="ui container">
            <React.Fragment>
              {user === "ADMIN" ? (
                <React.Fragment>
                  <Route path="/" exact component={ListReportsAdminFirstPage} />
                  <Route path="/admin/report/:id" exact component={ReportAdmin} />
                  <Route path="/admin/users" exact component={RenderUsers} />
                  <Route path="/admin/users/add" exact component={AddUser} />
                  <Route
                    path="/admin/users/update/:id"
                    exact
                    component={EditUser}
                  />
                  <Route
                    path="/admin/tvshows/view"
                    exact
                    component={ViewTvShows}
                  />
                  <Route
                    path="/admin/tvshows/show/:id"
                    exact
                    component={TvShow}
                  />
                  <Route
                    path="/admin/tvshows/add"
                    exact
                    component={AddTvShows}
                  />
                  <Route
                    path="/admin/tvshows/show/:id/episodes"
                    exact
                    component={ViewEpisodes}
                  />
                  <Route
                    path="/admin/tvshows/show/:id/add"
                    exact
                    component={AddEpisode}
                  />
                  <Route
                    path="/admin/tvshows/show/:idShow/episode/:idEpisode"
                    exact
                    component={Episode}
                  />
                  <Route
                    path="/admin/tvshows/show/:idShow/episode/:idEpisode/comments"
                    exact
                    component={CommentsAdmin}
                  />
                </React.Fragment>
              ) : (
                <React.Fragment>
                  {defaultRouteUser()}
                  <Route path="/" exact component={Profile} />
                  <Route path="/profile/edit" exact component={EditProfile} />
                  <Route
                    path="/tv-shows/explore"
                    exact
                    component={ExploreTvShows}
                  />
                  <Route path="/tv-show/:id" exact component={TvShowDetails}/> 
                  <Route
                    path="/search/:term"
                    exact
                    component={Search}
                  />
                  <Route path="/list/:type/:id" exact component={ListTvShows}/> 
                  <Route path="/user/:term" exact component={FriendProfile} />
                  <Route path="/episode/:id/comments/" exact component={Comments} />
                  <Route path="/report/bug" exact component={() => <Report type={REPORT_TYPE[2]}/>} />
                  <Route path="/report/inappropriate/:id" exact component={() => <Report type={REPORT_TYPE[1]}/>} />
                  <Route path="/report/spoiler/:id" exact component={() => <Report type={REPORT_TYPE[0]}/>} />
                </React.Fragment>
              )}
            </React.Fragment>
          </div>
        </React.Fragment>
      ) : (
        <React.Fragment>
          {defaultRouteGuest()}
          <Route path="/" exact render={() => <IndexPage indexType={INDEX_TYPE} typeSelected={INDEX_TYPE[0]}/>} />
          <Route path="/register" exact render={() => <IndexPage indexType={INDEX_TYPE} typeSelected={INDEX_TYPE[1]}/>} />
          <Route path="/forget-password" exact render={() => <IndexPage indexType={INDEX_TYPE} typeSelected={INDEX_TYPE[2]}/>} />
          <Route path="/user/change-password/:token" exact render={() => <IndexPage indexType={INDEX_TYPE} typeSelected={INDEX_TYPE[3]}/>} />
        </React.Fragment>
      )}
    </BrowserRouter>
  );
};

export default App;
