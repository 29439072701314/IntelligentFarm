// HTTP状态码
export const HTTP_STATUS = {
  OK: 200,
  CREATED: 201,
  ACCEPTED: 202,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  SERVER_ERROR: 500,
  GATEWAY_TIMEOUT: 504,
};

// 自定义业务错误码
export const ERROR_CODE = {
  SUCCESS: 200,
  PARAM_ERROR: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  SERVER_ERROR: 500,

  /** 无效token */
  TOKEN_INVALID: 40001,
  TOKEN_EXPIRED: 40002, // token过期
  TOKEN_NOT_FOUND: 40003, // 未找到token
  REFRESH_TOKEN_EXPIRED: 40004, // 刷新token过期
  INSUFFICIENT_SCOPE: 40005, // 权限不足
};

// 颜色常量
export const COLORS = {
  // ============== 主色调系列 ==============
  PRIMARY: "#4CAF50", // 绿色 - 主色调 (代表自然、健康、生长)
  INFO: "#4CAF50", // 信息色与主色保持一致

  // ============== 功能色 ==============
  SUCCESS: "#4CAF50", // 成功状态使用绿色
  WARNING: "#FF9800", // 警告/注意状态（橙色，代表活力、温暖）
  ERROR: "#F44336", // 危险/错误状态（红色，紧急醒目）

  // ============== 衍生色（基于主色的调色板） ==============
  PRIMARY_BG: "#E8F5E8", // 主色背景（非常浅的绿）
  PRIMARY_BG_HOVER: "#F1F8E9", // 主色背景悬停
  PRIMARY_BORDER: "#A5D6A7", // 主色边框
  PRIMARY_BORDER_HOVER: "#81C784", // 主色边框悬停
  PRIMARY_HOVER: "#388E3C", // 主色悬停（稍深的绿）
  PRIMARY_ACTIVE: "#2E7D32", // 主色激活（更深的绿）
  PRIMARY_TEXT_HOVER: "#388E3C", // 主色文本悬停
  PRIMARY_TEXT: "#4CAF50", // 主色文本
  PRIMARY_TEXT_ACTIVE: "#2E7D32", // 主色文本激活

  // ============== 文本色 ==============
  TEXT_BASE: "#333333", // 基础文本色
  TEXT_SECONDARY: "#666666", // 次要文本色（稍浅）
  TEXT_TERTIARY: "#999999", // 第三级文本色
  TEXT_QUATERNARY: "#CCCCCC", // 第四级文本色

  // ============== 背景色 ==============
  BG_BASE: "#FFFFFF", // 基础背景色
  BG_CONTAINER: "#FFFFFF", // 组件容器背景
  BG_ELEVATED: "#FFFFFF", // 浮层容器背景
  BG_LAYOUT: "#F5F5F5", // 布局背景色（浅灰色）
  BG_SPOTLIGHT: "#E8F5E8", // 聚光灯背景

  // ============== 边框与分割线 ==============
  BORDER: "#E0E0E0", // 基础边框色（浅灰）
  BORDER_SECONDARY: "#F0F0F0", // 次要边框色（更浅的灰）

  // ============== 链接色 ==============
  LINK: "#4CAF50",
  LINK_HOVER: "#388E3C",
  LINK_ACTIVE: "#2E7D32",

  // ============== 补充色 ==============
  FILL_SECONDARY: "#E8F5E8", // 填充色（浅绿）
  FILL_TERTIARY: "#F1F8E9", // 第三填充色
  FILL_QUATERNARY: "#F5F5F5", // 第四填充色
};
// 间距常量
export const GAP = {
  SMALL: 10,
  MEDIUM: 20,
  LARGE: 30,
};
// 角色设计
export const ROLE = {
  // 管理员
  ADMIN: 0,
  // 养殖员
  FARMER: 1,
};

export const PERMISSION_OBJ = {};

// 角色权限映射
export const ROLE_PERMISSION = {
  [ROLE.ADMIN]: ["admin", "farmer"],
  [ROLE.FARMER]: ["farmer"],
};
