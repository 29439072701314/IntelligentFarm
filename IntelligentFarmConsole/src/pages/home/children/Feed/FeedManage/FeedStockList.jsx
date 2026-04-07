import React, { useState, useEffect } from "react";
import ProTable from "@/component/ProTable";
import { Form, Button, Modal, message, Select, Input, DatePicker } from "antd";
import { PlusOutlined, DeleteOutlined } from "@ant-design/icons";
import { apiGetStockRecordList, apiAddStockRecord, apiGetFormulaList } from "@/services/feedApi";

const { Option } = Select;

const FeedStockList = () => {
  const [form] = Form.useForm();
  const [modalVisible, setModalVisible] = useState(false);
  const [formulaList, setFormulaList] = useState([]);
  const [loading, setLoading] = useState(false);

  // 加载饲料配方列表
  const loadFormulaList = async () => {
    try {
      const res = await apiGetFormulaList({ pageNumber: 1, pageSize: 100 });
      const data = res.data.content || res.data.list || [];
      setFormulaList(data);
    } catch (error) {
      console.error("获取饲料配方列表失败", error);
    }
  };

  useEffect(() => {
    loadFormulaList();
  }, []);

  // 处理添加库存记录
  const handleAdd = () => {
    form.resetFields();
    setModalVisible(true);
  };

  // 处理表单提交
  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      const res = await apiAddStockRecord(values);
      if (res.code === 200) {
        message.success("添加成功");
        setModalVisible(false);
        form.submit();
      } else {
        message.error(res.message || "添加失败");
      }
    } catch (error) {
      message.error("添加失败");
    } finally {
      setLoading(false);
    }
  };

  // 表格列配置
  const columns = [
    {
      title: "记录ID",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      key: "createTime",
    },
    {
      title: "饲料配方",
      dataIndex: "formulaId",
      key: "formulaId",
      render: (formulaId) => {
        const formula = formulaList.find(item => item.id === formulaId);
        return formula ? formula.name : formulaId;
      },
    },
    {
      title: "数量",
      dataIndex: "quantity",
      key: "quantity",
    },
    {
      title: "备注",
      dataIndex: "remark",
      key: "remark",
      render: (remark) => remark || "无",
    },
  ];

  return (
    <div>
      <div style={{ marginBottom: 16, textAlign: "right" }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          添加库存记录
        </Button>
      </div>
      <ProTable
        rowKey="id"
        form={form}
        api={apiGetStockRecordList}
        columns={columns}
      />
      <Modal
        title="添加库存记录"
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
        confirmLoading={loading}
      >
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Form.Item
            name="formulaId"
            label="饲料配方"
            rules={[{ required: true, message: "请选择饲料配方" }]}
          >
            <Select placeholder="请选择饲料配方">
              {formulaList.map(formula => (
                <Option key={formula.id} value={formula.id}>
                  {formula.name}
                </Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="quantity"
            label="数量"
            rules={[{ required: true, message: "请输入数量" }]}
          >
            <Input placeholder="请输入数量" type="number" />
          </Form.Item>
          <Form.Item
            name="remark"
            label="备注"
          >
            <Input.TextArea rows={3} placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default FeedStockList;