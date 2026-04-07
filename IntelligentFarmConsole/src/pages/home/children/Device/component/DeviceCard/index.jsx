import React, { useState } from "react";
import { Card, Statistic, Tag, Flex, Space, Progress, Switch, message } from "antd";
import CountUp from "react-countup";
import { getDeviceStatus } from "./constant.jsx";
import DeviceNameTag from "../../../../../../component/DeviceNameTag";
import FarmTag from "../../../../../../component/FarmTag";
import { apiControlDevice } from "../../../../../../services/deviceApi";
const formatter = (value) => <CountUp end={value} />;

export default function DeviceCard(props) {
  const { item } = props;
  const { deviceName, temperature, humidity, gasConcentration, time, farm } = item;
  const [fanLoading, setFanLoading] = useState(false);
  const [fanStatus, setFanStatus] = useState(false);
  
  // 计算温度、湿度、气体浓度的百分比（用于进度条）
  // 温度范围根据实际数据动态调整，默认使用0-50度的合理范围
  const tempPercent = temperature ? Math.min((temperature / 50) * 100, 100) : 0;
  const humidityPercent = humidity ? Math.min(humidity, 100) : 0; // 湿度范围0-100%
  const gasPercent = gasConcentration ? Math.min((gasConcentration / 1000) * 100, 100) : 0; // 气体浓度范围0-1000ppm
  
  // 控制风扇开关
  const handleFanSwitch = async (checked) => {
    setFanLoading(true);
    try {
      const data = {
        message: checked ? 'motor=1' : 'motor=0',
        device: deviceName
      };
      
      const res = await apiControlDevice(data);
      
      if (res.code === 200) {
        setFanStatus(checked);
        message.success(checked ? '风扇已开启' : '风扇已关闭');
      } else {
        message.error(res.message || '控制失败');
        // 如果失败，保持原状态
        setFanStatus(!checked);
      }
    } catch (error) {
      message.error('网络错误: ' + error.message);
      // 如果失败，保持原状态
      setFanStatus(!checked);
    } finally {
      setFanLoading(false);
    }
  };
  
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
      actions={[
        <Flex justify="center" align="center" gap={8} key="fan-control">
          <span style={{ fontSize: "14px", color: "#666" }}>风扇控制</span>
          <Switch
            checked={fanStatus}
            onChange={handleFanSwitch}
            loading={fanLoading}
            checkedChildren="开"
            unCheckedChildren="关"
          />
        </Flex>
      ]}
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
