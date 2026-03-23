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
  // 1分钟内为正常
  if (min < 1) {
    status = deviceStatusMap[0];
  }
  // 5分钟内为异常
  else if (min < 5) {
    status = deviceStatusMap[1];
  }
  // 大于5分钟为离线
  else {
    status = deviceStatusMap[2];
  }
  return <Tag color={status.color}>{status.label}</Tag>;
};
