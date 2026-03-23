import React from "react";
import { ROLE_LIST, ROLE_LIST_NOADMIN } from "./constant";
import { ROLE } from "../../constant";
import { Select, Tag } from "antd";
import { IdcardOutlined } from "@ant-design/icons";

export default function RoleSelect(props) {
  const { needAdmin = false } = props;
  return (
    <Select
      defaultValue={ROLE.ELDER}
      options={needAdmin ? ROLE_LIST : ROLE_LIST_NOADMIN}
      prefix={<IdcardOutlined />}
      {...props}
    />
  );
}
