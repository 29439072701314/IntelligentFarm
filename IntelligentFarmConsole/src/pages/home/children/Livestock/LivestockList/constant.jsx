import React from 'react';
import { Button, Input, Tag, DatePicker, Modal, Form } from 'antd';
import { EditOutlined, EyeOutlined, InboxOutlined, ExportOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';

export const getColumns = (handleEdit, handleDelete, handleDetail, handleInStock, handleOutStock) => {
  return [
    {
      title: "牲畜编码",
      dataIndex: "livestockCode",
      key: "livestockCode",
      render: (text) => {
        if (text && typeof text === 'string') {
          // 确保显示的是纯字符串
          return text.trim();
        }
        return text || '-';
      },
      formItemProps: {
        render: () => <Input placeholder="请输入牲畜编码" />,
      },
    },
    {
      title: "牲畜名称",
      dataIndex: "livestockName",
      key: "livestockName",
      formItemProps: {
        render: () => <Input placeholder="请输入牲畜名称" />,
      },
    },
    {
      title: "牲畜类型",
      dataIndex: "livestockType",
      key: "livestockType",
      formItemProps: {
        render: () => <Input placeholder="请输入牲畜类型" />,
      },
    },
    {
      title: "健康状态",
      dataIndex: "healthStatus",
      key: "healthStatus",
      render: (text) => {
        let color = 'green';
        if (text === '不健康' || text === '患病') color = 'red';
        if (text === '治疗中') color = 'orange';
        if (text === '亚健康') color = 'yellow';
        return <Tag color={color}>{text}</Tag>;
      },
      formItemProps: {
        render: () => <Input placeholder="请输入健康状态" />,
      },
    },
    {
      title: "体重(kg)",
      dataIndex: "weight",
      key: "weight",
      formItemProps: {
        render: () => <Input placeholder="请输入体重" />,
      },
    },
    {
      title: "入场时间",
      dataIndex: "inTime",
      key: "inTime",
      render: (text) => text ? dayjs(text).format('YYYY-MM-DD HH:mm:ss') : '-',
      formItemProps: {
        render: false,
      },
    },
    {
      title: "状态",
      dataIndex: "status",
      key: "status",
      render: (text) => {
        let color = 'green';
        let statusText = '在库';
        const statusValue = typeof text === 'string' ? parseInt(text) : text;
        if (statusValue === 2) {
          color = 'red';
          statusText = '已出库';
        }
        return <Tag color={color}>{statusText}</Tag>;
      },
    },
    {
      title: "操作",
      key: "action",
      render: (_, record) => {
        const statusValue = typeof record.status === 'string' ? parseInt(record.status) : record.status;
        return (
          <div>
            <Button
              type="link"
              icon={<EyeOutlined />}
              onClick={() => handleDetail(record)}
            >
              详情
            </Button>
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
            >
              编辑
            </Button>
            {statusValue === 1 && (
              <Button
                type="link"
                icon={<ExportOutlined />}
                onClick={() => handleOutStock(record)}
              >
                出库
              </Button>
            )}
          </div>
        );
      },
    },
  ];
};