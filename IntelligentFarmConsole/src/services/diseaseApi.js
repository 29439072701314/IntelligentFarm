import { post, get, put } from "../utils/request";

// 疾病记录相关API
export const apiGetDiseaseList = (data) => get("/api/disease/list", data);
export const apiGetDiseaseStatistics = (params) => get("/api/disease/statistics", params);
export const apiAddDiseaseRecord = (data) => post("/api/disease", data);
export const apiEditDiseaseRecord = (id, data) => put(`/api/disease/${id}`, data);
export const apiRecoverDiseaseRecord = (id) => put(`/api/disease/${id}/recover`);