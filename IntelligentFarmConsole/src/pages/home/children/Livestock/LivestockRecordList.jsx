import React, { useState, useEffect } from "react";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Button, Tag } from "antd";
import { apiGetLivestockRecords, apiGetLivestockRecordsByFarm } from "@/services/livestockApi";
import { useLocation } from "react-router";

const getColumns = () => {
  return [
    {
      title: "记录ID",
      dataIndex: "recordId",
      key: "recordId",
    },
    {
      title: "农场ID",
      dataIndex: "farmId",
      key: "farmId",
    },
    {
      title: "牲畜ID",
      dataIndex: "livestockId",
      key: "livestockId",
    },
    {
      title: "操作类型",
      dataIndex: "operationType",
      key: "operationType",
      render: (text) => {
        let color = 'green';
        let typeText = '入库';
        if (text === 2) {
          color = 'red';
          typeText = '出库';
        }
        return <Tag color={color}>{typeText}</Tag>;
      },
    },
    {
      title: "操作时间",
      dataIndex: "operationTime",
      key: "operationTime",
      render: (text) => text ? new Date(text).toLocaleString() : '-',
    },
    {
      title: "操作人",
      dataIndex: "operator",
      key: "operator",
    },
    {
      title: "备注",
      dataIndex: "remark",
      key: "remark",
      render: (text) => text || '-',
    },
  ];
};

export default function LivestockRecordList() {
  const [form] = Form.useForm();
  const location = useLocation();
  const [farmId, setFarmId] = useState(null);

  useEffect(() => {
    // 从URL参数中获取farmId
    const searchParams = new URLSearchParams(location.search);
    const id = searchParams.get('farmId');
    if (id) {
      setFarmId(parseInt(id));
    }
  }, [location.search]);

  // 搜索前处理
  const handleBeforeSearch = (values) => {
    return {
      ...values,
      farmId: farmId,
    };
  };

  // 获取数据的API函数
  const getRecordList = async (params) => {
    if (farmId) {
      const response = await apiGetLivestockRecordsByFarm(farmId);
      return {
        data: response.data || [],
        total: response.data ? response.data.length : 0,
      };
    } else {
      const response = await apiGetLivestockRecords(params);
      return {
        data: response.data || [],
        total: response.data ? response.data.length : 0,
      };
    }
  };

  return (
    <Content title={farmId ? "农场牲畜出入库记录" : "牲畜出入库记录"}>
      <ProTable
        rowKey="recordId"
        form={form}
        api={getRecordList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns()}
      />
    </Content>
  );
}