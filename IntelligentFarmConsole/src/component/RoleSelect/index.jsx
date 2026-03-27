import React from "react";
import { ROLE_LIST, ROLE_LIST_NOADMIN } from "./constant";
import { ROLE } from "../../constant";
import { Select, Tag } from "antd";
import { IdcardOutlined } from "@ant-design/icons";

export default function RoleSelect({ needAdmin = false, value, onChange, style, className, disabled }) {
  return (
    <Select
      defaultValue={ROLE.FARMER}
      options={needAdmin ? ROLE_LIST : ROLE_LIST_NOADMIN}
      prefix={<IdcardOutlined />}
      value={value}
      onChange={onChange}
      style={style}
      className={className}
      disabled={disabled}
    />
  );
}
