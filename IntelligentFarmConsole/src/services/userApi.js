import { post, get } from "../utils/request";

export const apiRegister = (data) => post("/user/register", data);

export const apiLogin = (data) => post("/user/login", data);

export const apiGetUser = () => get("/user/get");

export const apiChangePassword = (data) => post("/user/changePassword", data);

export const apiUpdateUser = (data) => post("/user/edit", data);

export const apiGetUserList = (data) => post("/user/list", data);

export const apiDeleteUser = (data) => get("/user/delete", data);

export const apiDeleteUserBatch = (data) => post("/user/deleteBatch", data);

export const apiGetUserListByIds = (data) => post("/user/listByIds", data);

// 绑定健康设备
export const apiBindHealthDevice = (data) =>
  post("/user/bindHealthDevice", data);

// 绑定家属
export const apiBindFamily = (data) => post("/user/bindFamily", data);

// 绑定护理人员
export const apiBindCaregiver = (data) => post("/user/bindCaregiver", data);
