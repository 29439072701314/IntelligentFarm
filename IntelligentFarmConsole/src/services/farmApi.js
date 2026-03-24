import { post, get, put, del } from "../utils/request";

export const apiGetFarmList = (data) => get("/api/farm/list", data);

export const apiAddFarm = (data) => post("/api/farm", data);

export const apiEditFarm = (id, data) => put(`/api/farm/${id}`, data);

export const apiDeleteFarm = (id) => del(`/api/farm/${id}`);

export const apiGetFarmDetail = (id) => get(`/api/farm/${id}/detail`);