import React from "react";
import { Card, Statistic, Tag, Flex, Button } from "antd";
import { TeamOutlined } from "@ant-design/icons";
import CountUp from "react-countup";
import RoomNumberTag from "@/component/RoomNumberTag";
import RateTag from "@/component/RateTag";
import DeviceNameTag from "../../../../../../../component/DeviceNameTag";

const formatter = (value) => <CountUp end={value} />;

export default function RoomCard(props) {
  const { item, onCheckIn } = props;
  const { roomNumber, maxCapacity, currentCount, device } = item;

  const isFull = currentCount >= maxCapacity;
  const isEmpty = currentCount === 0;

  return (
    <Card
      hoverable
      style={{
        backgroundColor: isFull ? "#fff2f0" : isEmpty ? "#f6ffed" : "#e6f7ff",
        borderColor: isFull ? "#ffccc7" : isEmpty ? "#b7eb8f" : "#91d5ff",
      }}
      extra={
        <DeviceNameTag
          deviceName={device?.deviceName || "未绑定"}
          color={device?.deviceId ? "purple" : "orange"}
        />
      }
      title={<RoomNumberTag roomNumber={roomNumber} />}
      actions={[
        <Button
          type="primary"
          icon={<TeamOutlined />}
          onClick={() => onCheckIn?.(item)}
        >
          分配
        </Button>,
      ]}
    >
      <Flex wrap justify="space-between" gap={10}>
        <Statistic
          title="最大容量"
          value={maxCapacity}
          suffix="人"
          formatter={formatter}
        />
        <Statistic
          title="已住人数"
          value={currentCount}
          suffix="人"
          formatter={formatter}
        />
        <Statistic
          title="入住率"
          formatter={() => (
            <RateTag
              showPercentage
              current={currentCount}
              total={maxCapacity}
              variant="solid"
              style={{ fontSize: 20, padding: 5 }}
            />
          )}
        />
      </Flex>
    </Card>
  );
}
