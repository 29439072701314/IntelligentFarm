import React from "react";
import { GENDER_LIST, GENDER } from "./constant";
import { Select } from "antd";
import { ManOutlined } from "@ant-design/icons";

export default function GenderSelect(props) {
  return (
    <Select
      defaultValue={GENDER.NONE}
      options={GENDER_LIST}
      prefix={<ManOutlined />}
      {...props}
    />
  );
}
