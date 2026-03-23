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
  PRIMARY: "#7265E3", // 紫色 - 主色调 (更偏向紫色)
  INFO: "#7265E3", // 信息色与主色保持一致

  // ============== 功能色 ==============
  SUCCESS: "#52C41A", // 成功状态使用绿色（传统成功色）
  WARNING: "#FAAD14", // 警告/注意状态（保持橙色，与主色区分）
  ERROR: "#FF4D4F", // 危险/错误状态（保持红色，紧急醒目）

  // ============== 衍生色（基于主色的调色板） ==============
  PRIMARY_BG: "#C8C8FF", // 主色背景（非常浅的紫）
  PRIMARY_BG_HOVER: "#DDDCFD", // 主色背景悬停
  PRIMARY_BORDER: "#B8B0F5", // 主色边框
  PRIMARY_BORDER_HOVER: "#9486E6", // 主色边框悬停
  PRIMARY_HOVER: "#503CD9", // 主色悬停（稍深的紫）
  PRIMARY_ACTIVE: "#4F42C9", // 主色激活（更深的紫）
  PRIMARY_TEXT_HOVER: "#503CD9", // 主色文本悬停
  PRIMARY_TEXT: "#7265E3", // 主色文本
  PRIMARY_TEXT_ACTIVE: "#4F42C9", // 主色文本激活

  // ============== 文本色 ==============
  TEXT_BASE: "#262626", // 基础文本色
  TEXT_SECONDARY: "#666666", // 次要文本色（稍浅）
  TEXT_TERTIARY: "#999999", // 第三级文本色
  TEXT_QUATERNARY: "#BFBFBF", // 第四级文本色

  // ============== 背景色 ==============
  BG_BASE: "#FFFFFF", // 基础背景色
  BG_CONTAINER: "#FFFFFF", // 组件容器背景
  BG_ELEVATED: "#FFFFFF", // 浮层容器背景
  BG_LAYOUT: "#F9F8FF", // 布局背景色（非常浅的紫色调）
  BG_SPOTLIGHT: "#C8C8FF", // 聚光灯背景

  // ============== 边框与分割线 ==============
  BORDER: "#B4B4FF", // 基础边框色（浅紫灰）
  BORDER_SECONDARY: "#C8C8FF", // 次要边框色（更浅的紫灰）

  // ============== 链接色 ==============
  LINK: "#7265E3",
  LINK_HOVER: "#503CD9",
  LINK_ACTIVE: "#4F42C9",

  // ============== 补充色 ==============
  FILL_SECONDARY: "#C8C8FF", // 填充色（浅紫）
  FILL_TERTIARY: "#F6F5FF", // 第三填充色
  FILL_QUATERNARY: "#F9F8FF", // 第四填充色
};
// 间距常量
export const GAP = {
  SMALL: 10,
  MEDIUM: 20,
  LARGE: 30,
};
// 角色设计
export const ROLE = {
  // 老人
  ELDER: 0,
  // 家属
  FAMILY: 1,
  // 监护人
  CAREGIVER: 2,
  // 管理员
  ADMIN: 3,
};

export const PERMISSION_OBJ = {};

// 角色权限映射
export const ROLE_PERMISSION = {
  [ROLE.ELDER]: ["elder"],
  [ROLE.FAMILY]: ["family"],
  [ROLE.CAREGIVER]: ["caregiver"],
  [ROLE.ADMIN]: ["elder", "family", "caregiver", "admin"],
};
