import React from 'react';
import { Button, Input } from 'antd';
import { EditOutlined, DeleteOutlined, EyeOutlined } from '@ant-design/icons';

export const getColumns = (handleEdit, handleDelete, handleDetail) => {
  return [
    {
      title: '农场名称',
      dataIndex: 'farmName',
      key: 'farmName',
      formItemProps: {
        render: () => <Input placeholder="请输入农场名称" />,
      },
    },
    {
      title: '地址',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: '牲畜数',
      dataIndex: 'livestockCount',
      key: 'livestockCount',
    },
    {
      title: '设备数',
      dataIndex: 'deviceCount',
      key: 'deviceCount',
    },
    {
      title: '操作',
      key: 'action',
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
          <Button
            type="link"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record)}
          >
            删除
          </Button>
        </div>
      ),
    },
  ];
};