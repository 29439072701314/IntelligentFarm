import React from 'react';
import { Button, Input, Tag, DatePicker } from 'antd';
import { EditOutlined, EyeOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';

export const getColumns = (handleEdit, handleDelete, handleDetail) => {
  return [
    {
      title: "牲畜编码",
      dataIndex: "livestockCode",
      key: "livestockCode",
      formItemProps: {
        render: () => <Input placeholder="请输入牲畜编码" />,
      },
    },
    {
      title: "牲畜类型",
      dataIndex: "type",
      key: "type",
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
      render: (text) => text ? new Date(text).toLocaleString() : '-',
      formItemProps: {
        render: false,
      },
    },
    {
      title: "操作",
      key: "action",
      render: (_, record) => (
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
        </div>
      ),
    },
  ];
};