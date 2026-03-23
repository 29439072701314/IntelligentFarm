import { createBrowserRouter } from "react-router";
import { NavLink } from "react-router";
import {
  FileOutlined,
  PieChartOutlined,
  TeamOutlined,
  HomeOutlined,
  AlertOutlined,
  UserOutlined,
  UsergroupAddOutlined,
} from "@ant-design/icons";
import Error from "../pages/error";
import Login from "../pages/login";
import LayOut from "../pages/home";
import Options from "../pages/home/children/Options";
import UserInfo from "../pages/home/children/User/UserInfo";
import Files from "../pages/home/children/Files";
import ChangePassword from "../pages/home/children/User/ChangePassword";
import UserList from "../pages/home/children/User/UserList";
import { filterRoutesByRole } from "../utils/permission";
import RoomList from "../pages/home/children/Room/RommList";
import RoomOccupancy from "../pages/home/children/Room/RoomOccupancy";
import Device from "../pages/home/children/Device";
import DataCenter from "../pages/home/children/DataCenter";
import DigitalHealth from "../pages/home/children/Elder/DigitalHealth";
import ElderList from "../pages/home/children/Elder/ElderList";

export const publicMenuRoutes = [
  {
    label: <NavLink to="">首页</NavLink>,
    key: "options",
    icon: <PieChartOutlined />,
    path: "",
    element: <Options />,
  },
  {
    label: <NavLink to="/home/dataCenter">数据中心</NavLink>,
    key: "dataCenter",
    icon: <PieChartOutlined />,
    path: "dataCenter",
    element: <DataCenter />,
    permission: ["admin"],
  },
  {
    label: <NavLink to="/home/device">环境设备</NavLink>,
    key: "device",
    icon: <AlertOutlined />,
    path: "device",
    permission: ["admin"],
    element: <Device />,
  },
  {
    label: "房间管理",
    key: "room",
    icon: <HomeOutlined />,
    path: "room",
    permission: ["admin"],
    children: [
      {
        label: <NavLink to="/home/room/roomList">房间列表</NavLink>,
        key: "roomList",
        path: "roomList",
        element: <RoomList />,
      },
      {
        label: <NavLink to="/home/room/roomOccupancy">入住管理</NavLink>,
        key: "roomOccupancy",
        path: "roomOccupancy",
        element: <RoomOccupancy />,
      },
    ],
  },
  {
    label: "老人管理",
    key: "elder",
    icon: <UserOutlined />,
    path: "elder",
    permission: ["admin", "caregiver", "family"],
    children: [
      {
        label: <NavLink to="/home/elder/elderList">老人列表</NavLink>,
        key: "elderList",
        path: "elderList",
        element: <ElderList />,
        permission: ["admin"],
      },
      {
        label: <NavLink to="/home/elder/digitalHealth">数字健康</NavLink>,
        key: "digitalHealth",
        path: "digitalHealth",
        element: <DigitalHealth />,
      },
    ],
  },
  {
    label: "账号管理",
    key: "user",
    icon: <UsergroupAddOutlined />,
    path: "user",
    children: [
      {
        label: <NavLink to="/home/user/addUser">添加账号</NavLink>,
        key: "addUser",
        path: "addUser",
        element: <UserInfo isAddUser />,
        permission: ["admin"],
      },
      {
        label: <NavLink to="/home/user/userList">账号列表</NavLink>,
        key: "userList",
        path: "userList",
        element: <UserList />,
        permission: ["admin"],
      },
      {
        label: <NavLink to="/home/user/userInfo">个人中心</NavLink>,
        key: "userInfo",
        path: "userInfo",
        element: <UserInfo />,
      },
      {
        label: <NavLink to="/home/user/changePassword">修改密码</NavLink>,
        key: "changePassword",
        path: "changePassword",
        element: <ChangePassword />,
      },
    ],
  },
  // {
  //   label: <NavLink to="/home/files">files</NavLink>,
  //   key: "files",
  //   icon: <FileOutlined />,
  //   path: "files",
  //   element: <Files />,
  // },
];

// 将路由配置转换为React Router格式
const convertToRouterFormat = (routes) => {
  return routes.map((item) => ({
    path: item.path,
    element: item.element,
    children: item.children ? convertToRouterFormat(item.children) : undefined,
  }));
};

const baseRouter = [
  {
    path: "/",
    element: <Login />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Login />,
  },
  {
    path: "*",
    element: <Error />,
  },
];

// 创建动态路由
export const createDynamicRouter = (userRole) => {
  // 根据用户角色过滤路由
  const filteredRoutes = filterRoutesByRole(publicMenuRoutes, userRole);
  const homeRoutes = convertToRouterFormat(filteredRoutes);
  const router = createBrowserRouter([
    ...baseRouter,
    {
      path: "/home",
      element: <LayOut />,
      children: homeRoutes,
    },
  ]);
  console.log(router);
  return router;
};

const router = createBrowserRouter(baseRouter);
export default router;
