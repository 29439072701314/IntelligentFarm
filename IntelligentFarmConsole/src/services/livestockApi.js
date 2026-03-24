import { post, get, put, del } from "../utils/request";

export const apiGetLivestockList = (data) => get("/api/livestock/list", data);

export const apiAddLivestock = (data) => post("/api/livestock", data);

export const apiEditLivestock = (id, data) => put(`/api/livestock/${id}`, data);

export const apiDeleteLivestock = (id) => del(`/api/livestock/${id}`);

export const apiGetLivestockDetail = (id) => get(`/api/livestock/${id}/detail`);