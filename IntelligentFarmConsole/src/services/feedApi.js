import { post, get, put, del } from "../utils/request";

// 配方相关API
export const apiGetFormulaList = (data) => get("/api/feed/formula/list", data);
export const apiAddFormula = (data) => post("/api/feed/formula", data);
export const apiEditFormula = (id, data) => put(`/api/feed/formula/${id}`, data);
export const apiDeleteFormula = (id) => del(`/api/feed/formula/${id}`);

// 入库相关API
export const apiAddStockRecord = (data) => post("/api/feed/stock", data);
export const apiGetStockRecordList = (data) => get("/api/feed/stock/list", data);
export const apiGetStockRecordsByFormulaId = (formulaId) => get(`/api/feed/stock/formula/${formulaId}`);

// 计划相关API
export const apiGetPlanList = (data) => get("/api/feed/plan/list", data);
export const apiAddPlan = (data) => post("/api/feed/plan", data);
export const apiEditPlan = (id, data) => put(`/api/feed/plan/${id}`, data);
export const apiDeletePlan = (id) => del(`/api/feed/plan/${id}`);
export const apiUpdateStatus = (id, status) => put(`/api/feed/plan/${id}/status`, { status });
export const apiExecutePlan = (id) => put(`/api/feed/plan/${id}/execute`);
