import React from "react";
import { GENDER_LIST, GENDER } from "./constant";
import { Select } from "antd";
import { ManOutlined } from "@ant-design/icons";

export default function GenderSelect({ value, onChange, style, className, disabled }) {
  return (
    <Select
      defaultValue={GENDER.MALE}
      options={GENDER_LIST}
      prefix={<ManOutlined />}
      value={value}
      onChange={onChange}
      style={style}
      className={className}
      disabled={disabled}
    />
  );
}
