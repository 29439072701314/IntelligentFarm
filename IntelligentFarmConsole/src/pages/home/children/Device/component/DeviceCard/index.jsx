import React from "react";
import { Card, Statistic, Tag, Flex } from "antd";
import { AlertFilled } from "@ant-design/icons";
import CountUp from "react-countup";
import { getDeviceStatus } from "./constant";
import DeviceNameTag from "../../../../../../component/DeviceNameTag";
import RoomNumberTag from "../../../../../../component/RoomNumberTag";
const formatter = (value) => <CountUp end={value} />;
import { COLORS } from "../../../../../../constant";

export default function DeviceCard(props) {
  const { item } = props;
  const { deviceName, temperature, humidity, gasConcentration, time, room } =
    item;
  return (
    <Card
      hoverable
      style={{ backgroundColor: "#c156e108" }}
      extra={
        <RoomNumberTag
          color={room?.roomNumber ? "blue" : "orange"}
          roomNumber={room?.roomNumber || "未绑定"}
        />
      }
      title={<DeviceNameTag deviceName={deviceName} />}
      actions={[
        getDeviceStatus(time),
        <Tag>{new Date(time).toLocaleString()}</Tag>,
      ]}
    >
      <Flex wrap justify="space-between" gap={10}>
        <Statistic
          title="温度"
          value={temperature}
          suffix="℃"
          formatter={formatter}
        />
        <Statistic
          title="湿度"
          value={humidity}
          suffix="%"
          formatter={formatter}
        />
        <Statistic
          title="气体浓度"
          value={gasConcentration}
          suffix="ppm"
          formatter={formatter}
        />
      </Flex>
    </Card>
  );
}
