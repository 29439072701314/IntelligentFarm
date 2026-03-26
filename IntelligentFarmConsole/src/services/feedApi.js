import { post, get, put, del } from "../utils/request";

// 配方相关API
export const apiGetFormulaList = (data) => get("/api/feed/formula/list", data);
export const apiAddFormula = (data) => post("/api/feed/formula", data);
export const apiEditFormula = (id, data) => put(`/api/feed/formula/${id}`, data);
export const apiDeleteFormula = (id) => del(`/api/feed/formula/${id}`);
export const apiUpdateStock = (id, stock) => put(`/api/feed/formula/${id}/stock`, { stock });

// 计划相关API
export const apiGetPlanList = (data) => get("/api/feed/plan/list", data);
export const apiAddPlan = (data) => post("/api/feed/plan", data);
export const apiEditPlan = (id, data) => put(`/api/feed/plan/${id}`, data);
export const apiDeletePlan = (id) => del(`/api/feed/plan/${id}`);
export const apiUpdateStatus = (id, status) => put(`/api/feed/plan/${id}/status`, { status });
export const apiExecutePlan = (id) => put(`/api/feed/plan/${id}/execute`);
