import axios from "axios";
import { ERROR_CODE, HTTP_STATUS } from "../constant";
import { message } from "antd";
export const BASE_URL = "http://localhost:8080";

// 封装axios
const instance = axios.create({
  baseURL: BASE_URL,
  timeout: 5000,
  // 跨域请求时是否需要使用凭证
  withCredentials: true,
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
});

// 请求拦截器
instance.interceptors.request.use(
  // 请求成功
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },

  // 请求失败
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code === HTTP_STATUS.OK) {
      // 通用success消息不显示
      if (res.message !== "success") {
        message.success(res.message);
      }
    }
    if (res.code === HTTP_STATUS.SERVER_ERROR) {
      message.error(res.message);
    }
    if (res.code === ERROR_CODE.TOKEN_INVALID) {
      console.log("token无效");
      // 跳转登录页
      window.location.href = "/login";
    }
    return response.data;
  },
  (error) => {
    console.error("Request error:", error);
    console.error("Error response:", error.response);
    console.error("Error request:", error.request);
    
    if (error.response) {
      // 服务器返回了错误状态码
      const status = error.response.status;
      if (status === 403) {
        message.error("服务器拒绝了请求(403)，请检查权限配置");
      } else if (status === 401) {
        message.error("未授权(401)，请重新登录");
      } else if (status === 404) {
        message.error("接口不存在(404)，请检查后端服务");
      } else {
        message.error(`服务器错误(${status})，请稍后重试`);
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      message.error("无法连接到服务器，请检查网络或后端服务是否启动");
    } else {
      // 请求配置出错
      message.error("请求配置错误，请稍后重试");
    }
    return Promise.reject(error);
  }
);

export const get = (url, params) => instance.get(url, { params });
export const post = (url, data) => instance.post(url, data);
export const put = (url, data) => instance.put(url, data);
export const del = (url) => instance.delete(url);
export const isRequestSuccess = (res) => res.code === HTTP_STATUS.OK;
export default instance;
