import { post, get } from "../utils/request";

export const apiRegister = (data) => post("/api/user/register", data);

export const apiLogin = (data) => post("/api/user/login", data);

export const apiGetUser = () => get("/api/user/get");

export const apiChangePassword = (data) => post("/api/user/changePassword", data);

export const apiUpdateUser = (data) => post("/api/user/edit", data);

export const apiGetUserList = (data) => post("/api/user/list", data);

export const apiDeleteUser = (data) => get("/api/user/delete", data);

export const apiDeleteUserBatch = (data) => post("/api/user/deleteBatch", data);

export const apiGetUserListByIds = (data) => post("/api/user/listByIds", data);

// 审核用户
export const apiApproveUser = (data) => post("/api/user/approve", data);
