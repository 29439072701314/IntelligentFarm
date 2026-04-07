import { Tag } from "antd";
const deviceStatusMap = [
  {
    color: "green",
    label: "正常",
  },
  {
    color: "orange",
    label: "异常",
  },
  {
    color: "red",
    label: "离线",
  },
];
export const getDeviceStatus = (time) => {
  const now = new Date();
  const diff = now - new Date(time);
  const min = diff / (1000 * 60);
  let status;
  // 1分钟内有数据为正常
  if (min < 1) {
    status = deviceStatusMap[0];
  }
  // 1分钟没有数据则显示异常
  else {
    status = deviceStatusMap[1];
  }
  return <Tag color={status.color}>{status.label}</Tag>;
};
