import React from 'react';
import { Button, Input } from 'antd';
import { EditOutlined, DeleteOutlined, FieldTimeOutlined } from '@ant-design/icons';

export const getColumns = (handleEdit, handleDelete, handleViewLivestock) => {
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
      formItemProps: {
        render: () => <Input placeholder="请输入地址" />,
      },
    },
    {
      title: '牲畜数',
      dataIndex: 'livestockCount',
      key: 'livestockCount',
      formItemProps: {
        render: () => <Input placeholder="请输入牲畜数" type="number" />,
      },
    },
    {
      title: '设备数',
      dataIndex: 'deviceCount',
      key: 'deviceCount',
      formItemProps: {
        render: () => <Input placeholder="请输入设备数" type="number" />,
      },
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <div>
          <Button
            type="link"
            icon={<FieldTimeOutlined />}
            onClick={() => handleViewLivestock(record)}
          >
            查看牲畜
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