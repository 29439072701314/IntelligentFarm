import axios from "axios";
import { ERROR_CODE, HTTP_STATUS } from "../constant";
import { message } from "antd";
export const BASE_URL = "";

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
    message.error("网络错误，请稍后重试");
    return Promise.reject(error);
  }
);

export const get = (url, params) => instance.get(url, { params });
export const post = (url, data) => instance.post(url, data);
export const put = (url, data) => instance.put(url, data);
export const del = (url) => instance.delete(url);
export const isRequestSuccess = (res) => res.code === HTTP_STATUS.OK;
export default instance;
