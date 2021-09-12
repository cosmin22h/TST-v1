import React from "react";
import { Typography } from "@material-ui/core";
import LinkOrg from "./links/LinkOrg";

const Copyright = () => {
  return (
    <Typography variant="body2" align="center" style={{ color: "#fff" }}>
      {"Copyright © "}
      <LinkOrg color="inherit" href="http://localhost:3000/">
        TST
      </LinkOrg>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
};

export default Copyright;
