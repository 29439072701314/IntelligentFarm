import React from "react";
import { Card, Statistic, Tag, Flex, Space, Progress } from "antd";
import CountUp from "react-countup";
import { getDeviceStatus } from "./constant.jsx";
import DeviceNameTag from "../../../../../../component/DeviceNameTag";
import FarmTag from "../../../../../../component/FarmTag";
const formatter = (value) => <CountUp end={value} />;

export default function DeviceCard(props) {
  const { item } = props;
  const { deviceName, temperature, humidity, gasConcentration, time, farm } = item;
  
  // 计算温度、湿度、气体浓度的百分比（用于进度条）
  const tempPercent = ((temperature - 12) / (20 - 12)) * 100; // 温度范围12-20度
  const humidityPercent = humidity; // 湿度范围0-100%
  const gasPercent = (gasConcentration / 500) * 100; // 气体浓度范围0-500ppm
  
  return (
    <Card
      hoverable
      style={{
        borderRadius: "12px",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.08)",
        transition: "all 0.3s ease",
        overflow: "hidden",
        background: "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)"
      }}
      bodyStyle={{
        padding: "20px",
      }}
      extra={
        <FarmTag
          farmName={farm?.farmName}
          color={farm?.farmName ? "green" : "orange"}
          style={{ fontSize: "12px", padding: "2px 8px" }}
        />
      }
      title={
        <Flex justify="space-between" align="center">
          <DeviceNameTag deviceName={deviceName} />
          {getDeviceStatus(time)}
        </Flex>
      }
    >
      <Flex wrap justify="space-between" gap={16} style={{ marginBottom: "16px" }}>
        <Statistic
          title="温度"
          value={temperature}
          suffix="℃"
          formatter={formatter}
          valueStyle={{ color: "#1890ff" }}
          titleStyle={{ fontSize: "12px", color: "#666" }}
        />
        <Statistic
          title="湿度"
          value={humidity}
          suffix="%"
          formatter={formatter}
          valueStyle={{ color: "#52c41a" }}
          titleStyle={{ fontSize: "12px", color: "#666" }}
        />
        <Statistic
          title="气体浓度"
          value={gasConcentration}
          suffix="ppm"
          formatter={formatter}
          valueStyle={{ color: "#fa8c16" }}
          titleStyle={{ fontSize: "12px", color: "#666" }}
        />
      </Flex>
      
      <Space direction="vertical" style={{ width: "100%" }}>
        <div>
          <Flex justify="space-between" style={{ marginBottom: "4px" }}>
            <span style={{ fontSize: "12px", color: "#666" }}>温度状态</span>
            <span style={{ fontSize: "12px", color: "#1890ff" }}>{temperature}℃</span>
          </Flex>
          <Progress 
            percent={tempPercent} 
            strokeColor="#1890ff" 
            size="small" 
            showInfo={false} 
          />
        </div>
        
        <div>
          <Flex justify="space-between" style={{ marginBottom: "4px" }}>
            <span style={{ fontSize: "12px", color: "#666" }}>湿度状态</span>
            <span style={{ fontSize: "12px", color: "#52c41a" }}>{humidity}%</span>
          </Flex>
          <Progress 
            percent={humidityPercent} 
            strokeColor="#52c41a" 
            size="small" 
            showInfo={false} 
          />
        </div>
        
        <div>
          <Flex justify="space-between" style={{ marginBottom: "4px" }}>
            <span style={{ fontSize: "12px", color: "#666" }}>气体浓度状态</span>
            <span style={{ fontSize: "12px", color: "#fa8c16" }}>{gasConcentration}ppm</span>
          </Flex>
          <Progress 
            percent={gasPercent} 
            strokeColor="#fa8c16" 
            size="small" 
            showInfo={false} 
          />
        </div>
      </Space>
      
      <div style={{ marginTop: "16px", fontSize: "12px", color: "#999" }}>
        <span>更新时间: {new Date(time).toLocaleString()}</span>
      </div>
    </Card>
  );
}
