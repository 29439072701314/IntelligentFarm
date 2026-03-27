import { get, post } from "../utils/request";

// 获取设备列表
export const apiDeviceList = (data) => post("/api/device/list", data);

// 获取所有设备
export const apiGetAllDevice = () => get("/api/device/getAllDevice");

// 绑定设备到农场
export const apiBindDeviceToFarm = (data) => post("/api/device/bindFarm", data);

// 解绑设备与农场
export const apiUnbindDeviceFromFarm = (data) => post("/api/device/unbindFarm", data);

// 根据农场ID获取设备列表
export const apiGetDevicesByFarmId = (farmId) => get("/api/device/getByFarmId", { farmId });
