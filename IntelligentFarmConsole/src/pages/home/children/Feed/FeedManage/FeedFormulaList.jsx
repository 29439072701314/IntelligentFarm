import React, { useState, useEffect } from "react";
import { Table, Button, Input, Modal, Form, message, Popconfirm, Space, Tag } from "antd";
import { EditOutlined, DeleteOutlined, PlusOutlined, WarningOutlined, InboxOutlined } from "@ant-design/icons";
import { apiGetFormulaList, apiAddFormula, apiEditFormula, apiDeleteFormula, apiAddStockRecord } from "@/services/feedApi";

const { Column } = Table;

export default function FeedFormulaList() {
  const [form] = Form.useForm();
  const [stockForm] = Form.useForm();
  const [dataSource, setDataSource] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [stockModalVisible, setStockModalVisible] = useState(false);
  const [currentFormula, setCurrentFormula] = useState(null);
  const [currentStockFormula, setCurrentStockFormula] = useState(null);

  // 加载配方列表
  const loadFormulaList = async () => {
    setLoading(true);
    try {
      const res = await apiGetFormulaList({ pageNumber: 1, pageSize: 100 });
      setDataSource(res.data.content || res.data.list || []);
    } catch (error) {
      message.error(error.response?.data?.message || "获取配方列表失败");
    } finally {
      setLoading(false);
    }
  };

  // 初始化加载
  useEffect(() => {
    loadFormulaList();
  }, []);

  // 处理新增配方
  const handleAdd = () => {
    setCurrentFormula(null);
    form.resetFields();
    setModalVisible(true);
  };

  // 处理编辑配方
  const handleEdit = (record) => {
    setCurrentFormula(record);
    form.setFieldsValue(record);
    setModalVisible(true);
  };

  // 处理删除配方
  const handleDelete = async (record) => {
    try {
      await apiDeleteFormula(record.id);
      message.success("删除成功");
      loadFormulaList();
    } catch (error) {
      message.error(error.response?.data?.message || "删除失败");
    }
  };

  // 处理表单提交
  const handleSubmit = async (values) => {
    try {
      if (currentFormula) {
        await apiEditFormula(currentFormula.id, values);
        message.success("编辑成功");
      } else {
        await apiAddFormula(values);
        message.success("新增成功");
      }
      setModalVisible(false);
      loadFormulaList();
    } catch (error) {
      message.error(error.response?.data?.message || "操作失败");
    }
  };

  // 处理入库
  const handleStockIn = (record) => {
    setCurrentStockFormula(record);
    stockForm.resetFields();
    setStockModalVisible(true);
  };

  // 处理入库提交
  const handleStockSubmit = async (values) => {
    try {
      await apiAddStockRecord({
        formulaId: currentStockFormula.id,
        quantity: values.quantity,
        remark: values.remark
      });
      message.success("入库成功");
      setStockModalVisible(false);
      loadFormulaList();
    } catch (error) {
      message.error(error.response?.data?.message || "入库失败");
    }
  };

  // 行样式
  const getRowClassName = (record) => {
    return record.stock < record.threshold ? "warning-row" : "";
  };

  // 库存单元格样式
  const getStockStyle = (record) => {
    return record.stock < record.threshold ? { color: 'red', fontWeight: 'bold' } : {};
  };

  return (
    <div>
      <div style={{ marginBottom: 16, display: "flex", justifyContent: "flex-end" }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          新增配方
        </Button>
      </div>
      <Table
        dataSource={dataSource}
        rowKey="id"
        loading={loading}
        rowClassName={getRowClassName}
      >
        <Column title="配方名称" dataIndex="name" key="name" />
        <Column title="适用阶段" dataIndex="stage" key="stage" />
        <Column 
          title="日建议量"
          dataIndex="dailyRecommendation" 
          key="dailyRecommendation"
          render={(text) => `${text} kg`}
        />
        <Column
          title="库存"
          dataIndex="stock"
          key="stock"
          render={(text, record) => (
            <Space>
              <span style={getStockStyle(record)}>{text}</span>
              <span>吨</span>
              {record.stock < record.threshold && (
                <Tag color="error" icon={<WarningOutlined />}>
                  库存不足
                </Tag>
              )}
            </Space>
          )}
        />
        <Column 
          title="阈值" 
          dataIndex="threshold" 
          key="threshold"
          render={(text) => `${text} 吨`}
        />
        <Column
          title="操作"
          key="action"
          render={(text, record) => (
            <div>
              <Button
                type="link"
                icon={<EditOutlined />}
                onClick={() => handleEdit(record)}
              >
                编辑
              </Button>
              <Button
                type="link"
                icon={<InboxOutlined />}
                onClick={() => handleStockIn(record)}
              >
                入库
              </Button>
              <Popconfirm
                title="确定删除该配方吗？"
                onConfirm={() => handleDelete(record)}
                okText="确定"
                cancelText="取消"
              >
                <Button type="link" danger icon={<DeleteOutlined />}>
                  删除
                </Button>
              </Popconfirm>
            </div>
          )}
        />
      </Table>

      <Modal
        title={currentFormula ? "编辑配方" : "新增配方"}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setModalVisible(false)}>
            取消
          </Button>,
          <Button key="submit" type="primary" onClick={() => form.submit()}>
            确定
          </Button>,
        ]}
      >
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Form.Item
            name="name"
            label="配方名称"
            rules={[{ required: true, message: "请输入配方名称" }]}
          >
            <Input placeholder="请输入配方名称" />
          </Form.Item>
          <Form.Item
            name="stage"
            label="适用阶段"
            rules={[{ required: true, message: "请输入适用阶段" }]}
          >
            <Input placeholder="请输入适用阶段" />
          </Form.Item>
          <Form.Item
            name="dailyRecommendation"
            label="日建议量"
            rules={[{ required: true, message: "请输入日建议量" }]}
          >
            <Input type="number" placeholder="请输入日建议量" />
          </Form.Item>
          <Form.Item
            name="stock"
            label="初始库存"
            rules={[{ required: true, message: "请输入初始库存" }]}
          >
            <Input type="number" placeholder="请输入初始库存" />
          </Form.Item>
          <Form.Item
            name="threshold"
            label="阈值"
            rules={[{ required: true, message: "请输入阈值" }]}
          >
            <Input type="number" placeholder="请输入阈值" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="饲料入库"
        open={stockModalVisible}
        onCancel={() => setStockModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setStockModalVisible(false)}>
            取消
          </Button>,
          <Button key="submit" type="primary" onClick={() => stockForm.submit()}>
            确定
          </Button>,
        ]}
      >
        <Form form={stockForm} layout="vertical" onFinish={handleStockSubmit}>
          <Form.Item
            label="配方名称"
            valuePropName="name"
          >
            <Input value={currentStockFormula?.name} disabled />
          </Form.Item>
          <Form.Item
            name="quantity"
            label="入库数量"
            rules={[{ required: true, message: "请输入入库数量" }]}
          >
            <Input type="number" placeholder="请输入入库数量" />
          </Form.Item>
          <Form.Item
            name="remark"
            label="备注"
          >
            <Input placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
