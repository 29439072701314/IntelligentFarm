import { post, get, put, del } from "../utils/request";

export const apiGetLivestockList = (data) => get("/api/livestock/list", data);

export const apiAddLivestock = (data) => post("/api/livestock", data);

export const apiEditLivestock = (id, data) => put(`/api/livestock/${id}`, data);

export const apiDeleteLivestock = (id) => del(`/api/livestock/${id}`);

export const apiGetLivestockDetail = (id) => get(`/api/livestock/${id}/detail`);

export const apiInStock = (id, operator, remark) => post(`/api/livestock/${id}/inStock`, { operator, remark });

export const apiOutStock = (id, operator, remark) => post(`/api/livestock/${id}/outStock`, { operator, remark });

export const apiGetLivestockRecords = (data) => get("/api/livestock/record/getAll", data);

export const apiGetLivestockRecordsByFarm = (farmId) => get(`/api/livestock/record/getByFarm/${farmId}`);

export const apiGetLivestockRecordsByLivestock = (livestockId) => get(`/api/livestock/record/getByLivestock/${livestockId}`);