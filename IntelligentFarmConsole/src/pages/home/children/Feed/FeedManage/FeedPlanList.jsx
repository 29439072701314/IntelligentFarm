import React, { useState, useEffect } from "react";
import { Table, Button, Input, Modal, Form, message, Popconfirm, DatePicker, Select, Tag } from "antd";
import { EditOutlined, DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { apiGetPlanList, apiAddPlan, apiEditPlan, apiDeletePlan, apiUpdateStatus, apiExecutePlan, apiGetFormulaList } from "@/services/feedApi";
import dayjs from "dayjs";

const { Column } = Table;
const { Option } = Select;
const { RangePicker } = DatePicker;

export default function FeedPlanList() {
  const [form] = Form.useForm();
  const [dataSource, setDataSource] = useState([]);
  const [formulaOptions, setFormulaOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [currentPlan, setCurrentPlan] = useState(null);
  const [dateRange, setDateRange] = useState([]);

  // 加载配方列表用于下拉选择
  const loadFormulaOptions = async () => {
    try {
      const res = await apiGetFormulaList({ pageNumber: 1, pageSize: 100 });
      const list = res.data.content || res.data.list || [];
      setFormulaOptions(list.map(item => ({ label: item.name, value: item.id })));
    } catch (error) {
      message.error(error.response?.data?.message || "获取配方列表失败");
    }
  };

  // 加载计划列表
  const loadPlanList = async () => {
    setLoading(true);
    try {
      const params = { pageNumber: 1, pageSize: 100 };
      if (dateRange.length === 2) {
        params.startDate = dateRange[0].format("YYYY-MM-DD");
        params.endDate = dateRange[1].format("YYYY-MM-DD");
      }
      const res = await apiGetPlanList(params);
      setDataSource(res.data.content || res.data.list || []);
    } catch (error) {
      message.error(error.response?.data?.message || "获取计划列表失败");
    } finally {
      setLoading(false);
    }
  };

  // 初始化加载
  useEffect(() => {
    loadFormulaOptions();
    loadPlanList();
  }, []);

  // 日期范围变化时重新加载
  useEffect(() => {
    loadPlanList();
  }, [dateRange]);

  // 处理新增计划
  const handleAdd = () => {
    setCurrentPlan(null);
    form.resetFields();
    setModalVisible(true);
  };

  // 处理编辑计划
  const handleEdit = (record) => {
    setCurrentPlan(record);
    form.setFieldsValue({
      ...record,
      planDate: record.planDate ? dayjs(record.planDate) : null,
      planTime: record.planTime ? dayjs(`2023-01-01 ${record.planTime}`) : null
    });
    setModalVisible(true);
  };

  // 处理删除计划
  const handleDelete = async (record) => {
    try {
      await apiDeletePlan(record.id);
      message.success("删除成功");
      loadPlanList();
    } catch (error) {
      message.error(error.response?.data?.message || "删除失败");
    }
  };

  // 处理表单提交
  const handleSubmit = async (values) => {
    try {
      const planData = {
        ...values,
        planDate: values.planDate.format("YYYY-MM-DD"),
        planTime: values.planTime.format("HH:mm:ss")
      };
      if (currentPlan) {
        await apiEditPlan(currentPlan.id, planData);
        message.success("编辑成功");
      } else {
        await apiAddPlan(planData);
        message.success("新增成功");
      }
      setModalVisible(false);
      loadPlanList();
    } catch (error) {
      message.error(error.response?.data?.message || "操作失败");
    }
  };

  // 处理状态切换
  const handleStatusChange = async (record) => {
    try {
      let newStatus;
      switch (record.status) {
        case "待执行":
          newStatus = "已完成";
          // 执行计划
          await apiExecutePlan(record.id);
          break;
        case "已完成":
          newStatus = "取消";
          await apiUpdateStatus(record.id, newStatus);
          break;
        case "取消":
          newStatus = "待执行";
          await apiUpdateStatus(record.id, newStatus);
          break;
        default:
          newStatus = record.status;
      }
      message.success("状态更新成功");
      loadPlanList();
    } catch (error) {
      message.error(error.response?.data?.message || "状态更新失败");
    }
  };

  // 获取状态标签颜色
  const getStatusColor = (status) => {
    switch (status) {
      case "待执行":
        return "blue";
      case "已完成":
        return "green";
      case "取消":
        return "red";
      default:
        return "default";
    }
  };

  return (
    <div>
      <div style={{ marginBottom: 16, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <RangePicker onChange={(dates) => setDateRange(dates || [])} />
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          新增计划
        </Button>
      </div>
      <Table
        dataSource={dataSource}
        rowKey="id"
        loading={loading}
      >
        <Column title="日期" dataIndex="planDate" key="planDate" />
        <Column title="时间" dataIndex="planTime" key="planTime" />
        <Column title="区域" dataIndex="area" key="area" />
        <Column title="配方ID" dataIndex="formulaId" key="formulaId" />
        <Column title="数量" dataIndex="amount" key="amount" />
        <Column
          title="状态"
          dataIndex="status"
          key="status"
          render={(text, record) => (
            <Tag color={getStatusColor(text)} onClick={() => handleStatusChange(record)}>
              {text}
            </Tag>
          )}
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
              <Popconfirm
                title="确定删除该计划吗？"
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
        title={currentPlan ? "编辑计划" : "新增计划"}
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
            name="planDate"
            label="日期"
            rules={[{ required: true, message: "请选择日期" }]}
          >
            <DatePicker style={{ width: "100%" }} />
          </Form.Item>
          <Form.Item
            name="planTime"
            label="时间"
            rules={[{ required: true, message: "请选择时间" }]}
          >
            <DatePicker picker="time" style={{ width: "100%" }} />
          </Form.Item>
          <Form.Item
            name="area"
            label="区域"
            rules={[{ required: true, message: "请输入区域" }]}
          >
            <Input placeholder="请输入区域" />
          </Form.Item>
          <Form.Item
            name="formulaId"
            label="配方"
            rules={[{ required: true, message: "请选择配方" }]}
          >
            <Select placeholder="请选择配方">
              {formulaOptions.map(option => (
                <Option key={option.value} value={option.value}>
                  {option.label}
                </Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="amount"
            label="数量"
            rules={[{ required: true, message: "请输入数量" }]}
          >
            <Input type="number" placeholder="请输入数量" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
