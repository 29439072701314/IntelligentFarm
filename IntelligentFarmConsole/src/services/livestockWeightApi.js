import { post, get } from "../utils/request";

// 体重记录相关API
export const apiAddWeightRecord = (data) => post("/api/livestock/weight", data);
export const apiGetWeightRecordsByLivestockId = (livestockId) => get(`/api/livestock/weight/livestock/${livestockId}`);
export const apiGetAverageWeightByFarmId = (farmId) => get(`/api/livestock/weight/farm/${farmId}/average`);
export const apiInitializeWeightRecords = () => post("/api/livestock/weight/initialize");
