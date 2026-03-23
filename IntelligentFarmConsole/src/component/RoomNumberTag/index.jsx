import React from "react";
import { Tag } from "antd";
import { FieldNumberOutlined } from "@ant-design/icons";
export default function RoomNumberTag(props) {
  const { roomNumber, color } = props;
  return (
    <Tag
      icon={<FieldNumberOutlined />}
      color={color || "blue"}
      variant="solid"
      style={{ fontSize: 18, fontWeight: "bold" }}
      {...props}
    >
      {roomNumber}
    </Tag>
  );
}
