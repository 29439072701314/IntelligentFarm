/**
 * 权限相关工具
 */

import { ROLE_PERMISSION } from "../constant";

/**
 * 检查用户角色是否有权限
 * @param {string} role 用户角色
 * @param {string|Array} permission 所需权限（单个权限或权限数组）
 * @returns {boolean} 是否有权限
 */
export const hasPermission = (role, permission) => {
  if (!role || !ROLE_PERMISSION[role]) {
    return false;
  }

  const userPermissions = ROLE_PERMISSION[role];

  // 如果permission是数组，检查用户是否有任一权限
  if (Array.isArray(permission)) {
    return permission.some((perm) => userPermissions.includes(perm));
  }

  // 如果permission是字符串，直接检查
  return userPermissions.includes(permission);
};

/**
 * 根据用户角色过滤路由表
 * @param {Array} routes 原始路由表
 * @param {string} userRole 用户角色
 * @returns {Array} 过滤后的路由表
 */
export const filterRoutesByRole = (routes, userRole) => {
  if (!routes || !Array.isArray(routes)) {
    return [];
  }
  const newRoutes = routes
    .filter((route) => {
      // 如果路由没有权限要求，则对所有角色可见
      if (!route.permission) {
        return true;
      }
      // 检查用户角色是否有权限访问该路由
      return hasPermission(userRole, route.permission);
    })
    .map((route) => {
      // 递归处理子路由
      if (route.children && Array.isArray(route.children)) {
        const filteredChildren = filterRoutesByRole(route.children, userRole);

        // 如果子路由全部被过滤掉，则父路由也不显示
        if (filteredChildren.length === 0) {
          return null;
        }

        return {
          ...route,
          children: filteredChildren,
        };
      }

      return route;
    })
    .filter((route) => route !== null); // 过滤掉为null的路由
  return newRoutes;
};
