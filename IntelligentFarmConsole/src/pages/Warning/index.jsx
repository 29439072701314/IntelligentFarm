import { jsxDEV } from "react/jsx-dev-runtime";
import React, { useState, useEffect } from "react";
import { Card, Table, Tag, Button, Select, message, Popconfirm, notification, Tabs } from "antd";
import { CheckCircleOutlined, ExclamationCircleOutlined, CloseCircleOutlined, ScanOutlined } from "@ant-design/icons";
import instance from "../../utils/request";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import styles from "./index.module.less";

const { Option } = Select;
const { TabPane } = Tabs;

const WarningPage = () => {
  const [warnings, setWarnings] = useState([]);
  const [total, setTotal] = useState(0);
  const [statistics, setStatistics] = useState({
    total: 0,
    unprocessed: 0,
    livestockAbnormal: 0,
    environmentAbnormal: 0
  });
  const [type, setType] = useState("全部");
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [stompClient, setStompClient] = useState(null);
  const [activeTab, setActiveTab] = useState('current');
  const [status, setStatus] = useState('未处理');

  useEffect(() => {
    fetchWarnings();
    fetchStatistics();
    // connectWebSocket();

    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, []);

  useEffect(() => {
    fetchWarnings();
  }, [type, page, size, status]);

  useEffect(() => {
    setStatus(activeTab === 'current' ? '未处理' : '已处理');
  }, [activeTab]);

  const fetchWarnings = async () => {
    try {
      const params = {
        page,
        size,
        status
      };
      if (type !== "全部") {
        params.type = type;
      }
      const response = await instance.get("/api/warning/list", { params });
      if (response && response.data) {
        setWarnings(response.data.list || []);
        setTotal(response.data.total || 0);
      } else {
        message.error("获取告警列表失败：数据格式错误");
      }
    } catch (error) {
      message.error("获取告警列表失败");
    }
  };

  const fetchStatistics = async () => {
    try {
      const response = await instance.get("/api/warning/statistics");
      if (response && response.data) {
        setStatistics(response.data);
      } else {
        message.error("获取统计数据失败：数据格式错误");
      }
    } catch (error) {
      message.error("获取统计数据失败");
    }
  };

  // const connectWebSocket = () => {
  //   try {
  //     const socket = new SockJS("/api/ws");
  //     const client = Stomp.over(socket);
  //     client.connect({}, () => {
  //       client.subscribe("/topic/warnings", (message) => {
  //         const newWarning = JSON.parse(message.body);
  //         notification.info({
  //           message: "新告警",
  //           description: `类型: ${newWarning.type}, 来源: ${newWarning.source}, 详情: ${newWarning.details}`,
  //           placement: "topRight"
  //         });
  //         fetchWarnings();
  //         fetchStatistics();
  //       });
  //     });
  //     setStompClient(client);
  //   } catch (error) {
  //     console.error("WebSocket 连接失败:", error);
  //   }
  // };

  const handleWarning = async (id) => {
    try {
      await instance.put(`/api/warning/${id}/handle`);
      message.success("处理成功");
      fetchWarnings();
      fetchStatistics();
    } catch (error) {
      message.error("处理失败");
    }
  };

  const batchHandleWarning = async () => {
    const unprocessedIds = warnings.filter(w => w.status === "未处理").map(w => w.id);
    if (unprocessedIds.length === 0) {
      message.warning("没有未处理的告警");
      return;
    }
    try {
      await instance.put("/api/warning/batch-handle", unprocessedIds);
      message.success("批量处理成功");
      fetchWarnings();
      fetchStatistics();
    } catch (error) {
      message.error("批量处理失败");
    }
  };

  const columns = [
    {
      title: "类型",
      dataIndex: "type",
      key: "type"
    },
    {
      title: "来源",
      dataIndex: "source",
      key: "source"
    },
    {
      title: "详情",
      dataIndex: "details",
      key: "details"
    },
    {
      title: "级别",
      dataIndex: "level",
      key: "level",
      render: (level) => {
        let color = "green";
        if (level === "高") color = "red";
        if (level === "中") color = "orange";
        return <Tag color={color}>{level}</Tag>;
      }
    },
    {
      title: "时间",
      dataIndex: "createdAt",
      key: "createdAt"
    },
    {
      title: "状态",
      dataIndex: "status",
      key: "status",
      render: (status) => {
        if (status === "已处理") {
          return <Tag color="green">已处理</Tag>;
        } else {
          return <Tag color="red">未处理</Tag>;
        }
      }
    },
    {
      title: "操作",
      key: "action",
      render: (_, record) => {
        if (record.status === "未处理") {
          return (
            <Popconfirm
              title="确定要处理该告警吗？"
              onConfirm={() => handleWarning(record.id)}
              okText="确定"
              cancelText="取消"
            >
              <Button type="primary" size="small">
                处理
              </Button>
            </Popconfirm>
          );
        }
        return null;
      }
    }
  ];

  const scanForWarnings = async () => {
    try {
      await instance.post("/api/warning/scan");
      message.success("扫描完成，已检查牲畜健康状态和饲料库存");
      fetchWarnings();
      fetchStatistics();
    } catch (error) {
      message.error("扫描失败");
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>异常告警管理</h1>
        <div>
          <Button type="primary" icon={<ScanOutlined />} onClick={scanForWarnings} style={{ marginRight: 8 }}>
            扫描告警
          </Button>
          <Button type="primary" onClick={batchHandleWarning}>
            批量处理
          </Button>
        </div>
      </div>

      <div className={styles.statistics}>
        <Card className={styles.statCard}>
          <div className={styles.statItem}>
            <span className={styles.statValue}>{statistics.total}</span>
            <span className={styles.statLabel}>总告警数</span>
          </div>
        </Card>
        <Card className={styles.statCard}>
          <div className={styles.statItem}>
            <span className={styles.statValue}>{statistics.unprocessed}</span>
            <span className={styles.statLabel}>未处理数</span>
          </div>
        </Card>
        <Card className={styles.statCard}>
          <div className={styles.statItem}>
            <span className={styles.statValue}>{statistics.livestockAbnormal}</span>
            <span className={styles.statLabel}>牲畜异常</span>
          </div>
        </Card>
        <Card className={styles.statCard}>
          <div className={styles.statItem}>
            <span className={styles.statValue}>{statistics.environmentAbnormal}</span>
            <span className={styles.statLabel}>环境异常</span>
          </div>
        </Card>
      </div>

      <div className={styles.filter}>
        <Select
          defaultValue="全部"
          style={{ width: 120 }}
          onChange={(value) => setType(value)}
        >
          <Option value="全部">全部</Option>
          <Option value="环境">环境</Option>
          <Option value="牲畜">牲畜</Option>
          <Option value="饲料">饲料</Option>
        </Select>
      </div>

      <Tabs activeKey={activeTab} onChange={setActiveTab} style={{ marginTop: 16 }}>
        <TabPane tab="当前告警" key="current">
          <Table
            columns={columns}
            dataSource={warnings}
            rowKey="id"
            pagination={{
              total,
              pageSize: size,
              current: page,
              onChange: (page) => setPage(page),
              onShowSizeChange: (_, size) => setSize(size)
            }}
          />
        </TabPane>
        <TabPane tab="历史告警" key="history">
          <Table
            columns={columns}
            dataSource={warnings}
            rowKey="id"
            pagination={{
              total,
              pageSize: size,
              current: page,
              onChange: (page) => setPage(page),
              onShowSizeChange: (_, size) => setSize(size)
            }}
          />
        </TabPane>
      </Tabs>
    </div>
  );
};

export default WarningPage;