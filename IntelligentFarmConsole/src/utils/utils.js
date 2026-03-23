/**
 * 退出登录
 */
export const logout = () => {
  localStorage.removeItem("token");
  window.location.href = '/login';
};
