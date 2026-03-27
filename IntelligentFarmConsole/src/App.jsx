import React, { useMemo } from "react";
import { ConfigProvider, App as AntdApp } from "antd";
import { RouterProvider } from "react-router";
import router, { createDynamicRouter } from "./router";
// import styles from './App.module.less'
import locale from "antd/locale/zh_CN";
import "dayjs/locale/zh-cn";
import { COLORS } from "./constant";
import { useSelector } from "react-redux";
function App() {
  const user = useSelector((state) => state.user);
  const roleId = user.role?.roleId;
  console.log('Role ID:', roleId);

  // 使用useMemo来缓存路由配置，避免不必要的重新创建
  const currentRouter = useMemo(() => {
    if (roleId !== undefined && roleId !== null) {
      console.log('Creating dynamic router for role:', roleId);
      const dynamicrouter = createDynamicRouter(roleId);
      console.log('Dynamic router created:', dynamicrouter);
      return dynamicrouter;
    }
    console.log('Using default router');
    return router;
  }, [roleId]); // roleId变化时重新创建路由

  // 主题配置
  // 渲染路由
  return (
    <>
      <ConfigProvider
        locale={locale}
        theme={{
          token: {
            // ============== 主色调系列 ==============
            colorPrimary: COLORS.PRIMARY, // 紫色 - 主色调 (更偏向紫色)
            colorInfo: COLORS.INFO, // 信息色与主色保持一致

            // ============== 功能色 ==============
            colorSuccess: COLORS.SUCCESS, // 成功状态使用绿色（传统成功色）
            colorWarning: COLORS.WARNING, // 警告/注意状态（保持橙色，与主色区分）
            colorError: COLORS.ERROR, // 危险/错误状态（保持红色，紧急醒目）

            // ============== 衍生色（基于主色的调色板） ==============
            colorPrimaryBg: COLORS.PRIMARY_BG, // 主色背景（非常浅的紫）
            colorPrimaryBgHover: COLORS.PRIMARY_BG_HOVER, // 主色背景悬停
            colorPrimaryBorder: COLORS.PRIMARY_BORDER, // 主色边框
            colorPrimaryBorderHover: COLORS.PRIMARY_BORDER_HOVER, // 主色边框悬停
            colorPrimaryHover: COLORS.PRIMARY_HOVER, // 主色悬停（稍深的紫）
            colorPrimaryActive: COLORS.PRIMARY_ACTIVE, // 主色激活（更深的紫）
            colorPrimaryTextHover: COLORS.PRIMARY_TEXT_HOVER, // 主色文本悬停
            colorPrimaryText: COLORS.PRIMARY_TEXT, // 主色文本
            colorPrimaryTextActive: COLORS.PRIMARY_TEXT_ACTIVE, // 主色文本激活

            // ============== 文本色 ==============
            colorTextBase: COLORS.TEXT_BASE, // 基础文本色
            colorTextSecondary: COLORS.TEXT_SECONDARY, // 次要文本色（稍浅）
            colorTextTertiary: COLORS.TEXT_TERTIARY, // 第三级文本色
            colorTextQuaternary: COLORS.TEXT_QUATERNARY, // 第四级文本色

            // ============== 背景色 ==============
            colorBgBase: COLORS.BG_BASE, // 基础背景色
            colorBgContainer: COLORS.BG_CONTAINER, // 组件容器背景
            colorBgElevated: COLORS.BG_ELEVATED, // 浮层容器背景
            colorBgLayout: COLORS.BG_LAYOUT, // 布局背景色（非常浅的紫色调）
            colorBgSpotlight: COLORS.BG_SPOTLIGHT, // 聚光灯背景

            // ============== 边框与分割线 ==============
            colorBorder: COLORS.BORDER, // 基础边框色（浅紫灰）
            colorBorderSecondary: COLORS.BORDER_SECONDARY, // 次要边框色（更浅的紫灰）
            // ============== 其他 ==============
            borderRadius: 8, // 组件圆角（稍大，更柔和）
            wireframe: false, // 关闭线框模式
            boxShadowSecondary:
              "0 3px 6px -4px rgba(114, 101, 227, 0.12), 0 6px 16px 0 rgba(114, 101, 227, 0.08), 0 9px 28px 8px rgba(114, 101, 227, 0.05)", // 带紫色调的阴影

            // ============== 字体 ==============
            fontFamily: `-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 
                         'Helvetica Neue', Arial, 'Noto Sans', sans-serif, 
                         'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 
                         'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 
                         'Noto Color Emoji'`,
            fontSize: 14,
            lineHeight: 1.5715,

            // ============== 链接 ==============
            colorLink: COLORS.LINK,
            colorLinkHover: COLORS.LINK_HOVER,
            colorLinkActive: COLORS.LINK_ACTIVE,

            // ============== 补充色 ==============
            colorFillSecondary: COLORS.FILL_SECONDARY, // 填充色（浅紫）
            colorFillTertiary: COLORS.FILL_TERTIARY, // 第三填充色
            colorFillQuaternary: COLORS.FILL_QUATERNARY, // 第四填充色
          },
        }}
      >
        <AntdApp>
          <RouterProvider router={currentRouter} />
        </AntdApp>
      </ConfigProvider>
    </>
  );
}

export default App;
