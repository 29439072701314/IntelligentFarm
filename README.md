# 智能农场系统

## 项目结构

- `IntelligentFarmConsole`: 前端项目，使用 React + Ant Design
- `IntelligentFarmCore`: 后端项目，使用 Spring Boot + JPA
- `环境监测`: 硬件监测代码

## 告警功能实现

### 后端实现

1. **数据库表结构**
   - `tb_warning` 表用于存储告警信息
   - 包含字段：id, created_at, detail, level, source, status, type, details, handled_at

2. **后端 API**
   - `GET /api/warning/list`: 获取告警列表
   - `GET /api/warning/statistics`: 获取告警统计数据
   - `PUT /api/warning/{id}/handle`: 处理单个告警
   - `PUT /api/warning/batch-handle`: 批量处理告警
   - `POST /api/warning/scan`: 扫描告警

3. **后端服务**
   - `WarningService`: 告警服务，负责告警的生成、处理和扫描
   - `WarningController`: 告警控制器，处理 HTTP 请求
   - `WarningDao`: 告警数据访问对象，操作数据库

### 前端实现

1. **告警页面**
   - 路径：`/home/warning`
   - 组件：`src/pages/Warning/index.jsx`

2. **功能**
   - 显示告警列表
   - 显示告警统计数据
   - 处理单个告警
   - 批量处理告警
   - 扫描告警
   - WebSocket 实时接收新告警

### 修复的问题

1. **前端 API 请求方式**
   - 将直接使用 axios 发起请求修改为使用封装后的 instance 方法
   - 修复响应数据的获取方式，确保正确获取后端返回的数据

2. **后端数据传输对象**
   - 修改 WarningController 类，确保返回 WarningDTO 对象，而不是 Warning 实体类对象
   - 确保字段名与前端使用的字段名一致

3. **WebSocket 配置**
   - 注释掉 WarningService 中对 SimpMessagingTemplate 的使用，以确保服务能够正常启动

### 如何测试

1. **启动后端服务**
   - 进入 `IntelligentFarmCore` 目录
   - 执行 `mvn spring-boot:run` 命令启动后端服务

2. **启动前端服务**
   - 进入 `IntelligentFarmConsole` 目录
   - 执行 `npm install` 命令安装依赖
   - 执行 `npm run dev` 命令启动前端服务

3. **访问告警页面**
   - 打开浏览器，访问 `http://localhost:5173`
   - 登录系统
   - 点击左侧菜单中的 "告警管理" 选项
   - 查看告警列表和统计数据
   - 测试处理告警和批量处理告警功能
   - 测试扫描告警功能

### 注意事项

1. **数据库配置**
   - 确保数据库连接配置正确，数据库中已经创建了 `tb_warning` 表

2. **WebSocket 连接**
   - 由于 WebSocket 配置问题，目前暂时注释掉了 WebSocket 相关功能
   - 如果需要启用 WebSocket 功能，需要确保 WebSocket 配置正确

3. **告警数据**
   - 数据库中已经存在一些告警数据，可以直接查看
   - 可以通过扫描告警功能生成新的告警数据
