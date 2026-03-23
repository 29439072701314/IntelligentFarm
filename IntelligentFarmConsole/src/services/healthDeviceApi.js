import { get } from "../utils/request";

// 获取所有设备
export const apiGetAllHealthDevice = () =>
  get("/healthDevice/getAllHealthDevice");
