import React from "react";
import { Tag } from "antd";
import { AlertFilled } from "@ant-design/icons";
import { MyIcon } from "../Icons";

export default function DeviceNameTag(props) {
  const { deviceName, isHealthDevice = false } = props;
  const icon = isHealthDevice ? (
    <MyIcon type="icon-health-device" />
  ) : (
    <AlertFilled />
  );
  const color = isHealthDevice ? "green" : "purple";
  return (
    <Tag icon={deviceName ? icon : null} color={color}>
      {deviceName || "-"}
    </Tag>
  );
}
