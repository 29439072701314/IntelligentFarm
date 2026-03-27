import React from "react";
import { Card, Statistic, Tag, Flex } from "antd";
import CountUp from "react-countup";
import { getDeviceStatus } from "./constant";
import DeviceNameTag from "../../../../../../component/DeviceNameTag";
import FarmTag from "../../../../../../component/FarmTag";
const formatter = (value) => <CountUp end={value} />;

export default function DeviceCard(props) {
  const { item } = props;
  const { deviceName, temperature, humidity, gasConcentration, time, farm } =
    item;
  return (
    <Card
      hoverable
      style={{ backgroundColor: "#c156e108" }}
      extra={
        <FarmTag
          farmName={farm?.farmName}
          color={farm?.farmName ? "blue" : "orange"}
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
