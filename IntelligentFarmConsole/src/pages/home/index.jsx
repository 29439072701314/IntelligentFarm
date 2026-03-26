import React, { useState, useMemo } from "react";
import { Breadcrumb, Layout, Menu, theme, Dropdown, Avatar, Space } from "antd";
import styles from "./index.module.less";
import { NavLink, Outlet, useLocation } from "react-router";
import { publicMenuRoutes } from "../../router";
import Logo from "../../component/Logo";
import { useSelector } from "react-redux";
import { DownOutlined } from "@ant-design/icons";
import { BASE_URL } from "../../utils/request";
import { logout } from "../../utils/utils";
import { filterRoutesByRole } from "../../utils/permission";
import RoleTag from "../../component/RoleTag";
const { Header, Content, Footer, Sider } = Layout;

const App = () => {
  const user = useSelector((state) => state.user);
  const { pathname } = useLocation();
  console.log(pathname);
  // 侧边栏是否折叠
  const [collapsed, setCollapsed] = useState(false);
  // 过滤菜单
  const menuItems = filterRoutesByRole(publicMenuRoutes, user.role.roleId);

  // 根据当前路径生成面包屑
  const breadcrumbItems = useMemo(() => {
    const generateBreadcrumb = (
      routes,
      currentPath,
      parentPath = "",
      result = []
    ) => {
      for (const route of routes) {
        const routePath = parentPath
          ? `${parentPath}/${route.path}`
          : `/home/${route.path}`;

        if (routePath && pathname === routePath) {
          result.push({
            title: (
              <>
                {route.icon}
                <span>{route.label}</span>
              </>
            ),
          });
          return result;
        }

        if (route.children) {
          const childResult = generateBreadcrumb(
            route.children,
            currentPath,
            routePath,
            [
              ...result,
              {
                title: (
                  <>
                    {route.icon}
                    <span>{route.label}</span>
                  </>
                ),
              },
            ]
          );
          if (childResult) {
            return childResult;
          }
        }
      }
      return null;
    };

    const breadcrumbs = generateBreadcrumb(menuItems, pathname);
    return breadcrumbs
      ? [{ title: "首页", href: "/home" }, ...breadcrumbs]
      : [{ title: "首页", href: "/home" }];
  }, [pathname, menuItems]);

  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  return (
    <Layout className={styles.layout}>
      <Sider
        collapsible
        collapsed={collapsed}
        onCollapse={(value) => setCollapsed(value)}
      >
        <Logo isHideTitle={collapsed} />
        <Menu
          theme="dark"
          defaultSelectedKeys={["dataCenter"]}
          mode="inline"
          items={menuItems}
        />
      </Sider>
      <Layout>
        <Header
          className={styles.header}
          style={{ background: colorBgContainer }}
        >
          <Breadcrumb items={breadcrumbItems} />
          <Dropdown
            arrow
            menu={{
              items: [
                {
                  key: "1",
                  label: <NavLink to="/home/user/userinfo">账号信息</NavLink>,
                },
                {
                  key: "2",
                  label: "退出登录",
                },
              ],
              onClick: ({ key }) => {
                if (key === "2") {
                  // 退出登录
                  logout();
                }
              },
            }}
          >
            <NavLink>
              <Space>
                <span>欢迎 {user.userName} 登录</span>
                <DownOutlined />
                <RoleTag role={user.role} />
                <Avatar src={BASE_URL + user.avatar} />
              </Space>
            </NavLink>
          </Dropdown>
        </Header>
        <Content className={styles.content}>
          <div
            style={{
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
            }}
          >
            <Outlet />
          </div>
        </Content>
        {/* <Footer className={styles.footer}>This is footer</Footer> */}
      </Layout>
    </Layout>
  );
};
export default App;
