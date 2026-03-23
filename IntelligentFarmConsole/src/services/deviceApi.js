import { get, post } from "../utils/request";

// 获取设备列表
export const apiDeviceList = (data) => post("/device/list", data);

// 获取所有设备
export const apiGetAllDevice = () => get("/device/getAllDevice");
