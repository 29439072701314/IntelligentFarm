import React from "react";
import { Tag } from "antd";

export default function FarmTag({ farmName, color = "blue" }) {
  return (
    <Tag color={color}>
      {farmName || "未绑定农场"}
    </Tag>
  );
}
