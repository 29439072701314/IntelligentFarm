# 牲畜管理模块功能时序图指令（简化版）

## 1. 牲畜管理模块核心功能时序图

### 1.1 生成条件
- 展示核心功能的主要流程
- 包含前端、后端、服务层和数据层
- 突出健康状态处理逻辑

### 1.2 具体指令

```mermaid
sequenceDiagram
    participant Client as 前端
    participant Controller as 控制器
    participant Service as 服务层
    participant DAO as 数据层
    participant DB as 数据库

    %% 添加牲畜
    Client->>Controller: POST /api/livestock (牲畜信息)
    Controller->>Service: addLivestock(livestock)
    Service->>DAO: save(livestock)
    DAO->>DB: 保存数据
    DB-->>DAO: 返回结果
    DAO-->>Service: 返回保存的牲畜
    alt 健康状态异常
        Service->>Service: 创建疾病记录和告警
    end
    Service-->>Controller: 返回成功响应
    Controller-->>Client: 200 OK { "success": true, "data": livestock }

    %% 编辑牲畜
    Client->>Controller: PUT /api/livestock/{id} (更新信息)
    Controller->>Service: editLivestock(livestock)
    Service->>DAO: findById(id)
    DAO->>DB: 查询牲畜
    DB-->>DAO: 返回结果
    DAO-->>Service: 返回牲畜
    alt 牲畜不存在
        Service-->>Controller: 返回错误
        Controller-->>Client: 200 OK { "success": false, "message": "牲畜不存在" }
    else 牲畜存在
        Service->>DAO: save(updatedLivestock)
        DAO->>DB: 更新数据
        DB-->>DAO: 返回结果
        DAO-->>Service: 返回更新后的牲畜
        alt 健康状态变化
            Service->>Service: 处理健康状态（创建记录或消除告警）
        end
        Service-->>Controller: 返回成功响应
        Controller-->>Client: 200 OK { "success": true, "data": livestock }
    end

    %% 获取牲畜列表
    Client->>Controller: GET /api/livestock/list?page=1&size=10
    Controller->>Service: getLivestockList(pageReq)
    Service->>DAO: 查询牲畜列表
    DAO->>DB: 查询数据
    DB-->>DAO: 返回结果
    DAO-->>Service: 返回牲畜列表
    Service->>Service: 分页和过滤
    Service-->>Controller: 返回分页结果
    Controller-->>Client: 200 OK { "success": true, "data": { "list": [...], "total": 100 } }

    %% 获取牲畜详情
    Client->>Controller: GET /api/livestock/{id}/detail
    Controller->>Service: getLivestockDetail(id)
    Service->>DAO: findById(id)
    DAO->>DB: 查询牲畜
    DB-->>DAO: 返回结果
    DAO-->>Service: 返回牲畜
    alt 牲畜不存在
        Service-->>Controller: 返回错误
        Controller-->>Client: 200 OK { "success": false, "message": "牲畜不存在" }
    else 牲畜存在
        Service-->>Controller: 返回成功响应
        Controller-->>Client: 200 OK { "success": true, "data": livestock }
    end
```

### 1.3 时序图说明

1. **添加牲畜**：前端发送请求，后端保存数据，检查健康状态并处理异常情况。

2. **编辑牲畜**：前端发送更新请求，后端检查牲畜是否存在，更新数据，处理健康状态变化。

3. **获取牲畜列表**：前端发送分页请求，后端查询数据，进行分页和过滤后返回结果。

4. **获取牲畜详情**：前端发送详情请求，后端查询并返回牲畜信息。

### 1.4 核心功能点

- **健康状态管理**：自动处理健康状态异常，创建疾病记录和告警。
- **数据操作**：完整的CRUD操作支持。
- **分页查询**：支持分页和条件过滤。
- **错误处理**：处理牲畜不存在等异常情况。