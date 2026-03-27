import { createBrowserRouter, Navigate, NavLink } from "react-router";
import {
  FileOutlined,
  PieChartOutlined,
  TeamOutlined,
  HomeOutlined,
  AlertOutlined,
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
import Device from "../pages/home/children/Device";
import DataCenter from "../pages/home/children/DataCenter";
import FarmList from "../pages/home/children/Farm/FarmList";
import LivestockList from "../pages/home/children/Livestock/LivestockList";
import FeedManage from "../pages/home/children/Feed/FeedManage";
import DiseaseManage from "../pages/home/children/Disease/DiseaseManage";
import Warning from "../pages/Warning";

export const publicMenuRoutes = [
  {
    label: <NavLink to="/home/dataCenter">数据中心</NavLink>,
    key: "dataCenter",
    icon: <PieChartOutlined />,
    path: "dataCenter",
    element: <DataCenter />,
    permission: ["admin", "farmer"],
  },
  {
    label: <NavLink to="/home/device">环境设备</NavLink>,
    key: "device",
    icon: <AlertOutlined />,
    path: "device",
    permission: ["admin", "farmer"],
    element: <Device />,
  },
  {
    label: "农场管理",
    key: "farm",
    icon: <HomeOutlined />,
    path: "farm",
    permission: ["admin", "farmer"],
    children: [
      {
        label: <NavLink to="/home/farm/farmList">农场列表</NavLink>,
        key: "farmList",
        path: "farmList",
        element: <FarmList />,
      },
    ],
  },
  {
    label: "牲畜管理",
    key: "livestock",
    icon: <HomeOutlined />,
    path: "livestock",
    permission: ["admin", "farmer"],
    children: [
      {
        label: <NavLink to="/home/livestock/livestockList">牲畜列表</NavLink>,
        key: "livestockList",
        path: "livestockList",
        element: <LivestockList />,
      },
    ],
  },
  {
    label: "饲料管理",
    key: "feed",
    icon: <HomeOutlined />,
    path: "feed",
    permission: ["admin", "farmer"],
    children: [
      {
        label: <NavLink to="/home/feed/feedManage">饲料管理</NavLink>,
        key: "feedManage",
        path: "feedManage",
        element: <FeedManage />,
      },
    ],
  },
  {
    label: "疾病管理",
    key: "disease",
    icon: <AlertOutlined />,
    path: "disease",
    permission: ["admin", "farmer"],
    children: [
      {
        label: <NavLink to="/home/disease/diseaseManage">疾病管理</NavLink>,
        key: "diseaseManage",
        path: "diseaseManage",
        element: <DiseaseManage />,
      },
    ],
  },
  {
    label: <NavLink to="/home/warning">告警管理</NavLink>,
    key: "warning",
    icon: <AlertOutlined />,
    path: "warning",
    permission: ["admin", "farmer"],
    element: <Warning />,
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
  console.log('Creating dynamic router with userRole:', userRole);
  // 根据用户角色过滤路由
  const filteredRoutes = filterRoutesByRole(publicMenuRoutes, userRole);
  console.log('Filtered routes:', filteredRoutes);
  const homeRoutes = convertToRouterFormat(filteredRoutes);
  console.log('Home routes:', homeRoutes);
  const router = createBrowserRouter([
    ...baseRouter,
    {
      path: "/home",
      element: <LayOut />,
      children: [
        {
          index: true,
          element: <Navigate to="/home/dataCenter" replace />,
        },
        ...homeRoutes,
      ],
    },
  ]);
  console.log('Created router:', router);
  return router;
};

const router = createBrowserRouter(baseRouter);
export default router;
