import React from "react";
import { Tag } from "antd";
import { AlertFilled, EnvironmentOutlined } from "@ant-design/icons";
import { MyIcon } from "../Icons";

export default function DeviceNameTag(props) {
  const { deviceName, isHealthDevice = false } = props;
  const icon = isHealthDevice ? (
    <MyIcon type="icon-health-device" />
  ) : (
    <EnvironmentOutlined />
  );
  const color = isHealthDevice ? "green" : "blue";
  return (
    <Tag 
      icon={deviceName ? icon : null} 
      color={color}
      style={{
        fontSize: "14px",
        padding: "4px 12px",
        borderRadius: "16px",
        fontWeight: "500"
      }}
    >
      {deviceName || "-"}
    </Tag>
  );
}
