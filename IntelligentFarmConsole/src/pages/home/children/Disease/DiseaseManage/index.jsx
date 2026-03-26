import React, { useState, useEffect } from "react";
import { Table, Button, Input, Modal, Form, message, Popconfirm, Card, Row, Col, Tag, Space, Spin, Select, DatePicker } from "antd";
import { EditOutlined, PlusOutlined, MedicineBoxOutlined, CheckCircleOutlined, SearchOutlined } from "@ant-design/icons";
import { apiGetDiseaseList, apiAddDiseaseRecord, apiEditDiseaseRecord, apiRecoverDiseaseRecord, apiGetDiseaseStatistics } from "@/services/diseaseApi";
import { apiGetLivestockList } from "@/services/livestockApi";
import { apiGetFarmList } from "@/services/farmApi";
import dayjs from "dayjs";
import Content from "@/component/Content";

const { Column } = Table;
const { TextArea } = Input;
const { Option } = Select;
const { RangePicker } = DatePicker;

export default function DiseaseManage() {
  const [form] = Form.useForm();
  const [dataSource, setDataSource] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState(null);
  const [statistics, setStatistics] = useState({ total: 0, treating: 0, recovered: 0 });
  const [searchParams, setSearchParams] = useState({ 
    livestockCode: "", 
    diseaseName: "", 
    farmId: null,
    status: null,
    dateRange: null
  });
  const [checking, setChecking] = useState(false);
  const [farmList, setFarmList] = useState([]);

  // 加载农场列表
  const loadFarmList = async () => {
    try {
      const res = await apiGetFarmList({ pageNumber: 1, pageSize: 100 });
      setFarmList(res.data.content || res.data.list || []);
    } catch (error) {
      console.error("获取农场列表失败", error);
    }
  };

  // 加载疾病记录列表
  const loadDiseaseList = async () => {
    setLoading(true);
    try {
      const condition = {};
      if (searchParams.livestockCode) {
        condition.livestockCode = searchParams.livestockCode;
      }
      if (searchParams.diseaseName) {
        condition.diseaseName = searchParams.diseaseName;
      }
      if (searchParams.farmId !== null && searchParams.farmId !== undefined) {
        condition.farmId = searchParams.farmId;
      }
      if (searchParams.status) {
        condition.status = searchParams.status;
      }
      if (searchParams.dateRange && searchParams.dateRange.length === 2) {
        condition.startDate = searchParams.dateRange[0].format("YYYY-MM-DD");
        condition.endDate = searchParams.dateRange[1].format("YYYY-MM-DD");
      }
      const params = {
        pageNumber: 1,
        pageSize: 100,
        condition: condition
      };
      const res = await apiGetDiseaseList(params);
      setDataSource(res.data.content || res.data.list || []);
    } catch (error) {
      message.error(error.response?.data?.message || "获取疾病记录列表失败");
    } finally {
      setLoading(false);
    }
  };

  // 加载统计数据
  const loadStatistics = async () => {
    try {
      const params = {};
      if (searchParams.farmId) {
        params.farmId = searchParams.farmId;
      }
      const res = await apiGetDiseaseStatistics(params);
      setStatistics(res.data);
    } catch (error) {
      console.error("获取统计数据失败", error);
    }
  };

  // 初始化加载
  useEffect(() => {
    loadFarmList();
    loadDiseaseList();
    loadStatistics();
  }, []);

  // 搜索条件变化时重新加载
  useEffect(() => {
    loadDiseaseList();
  }, [searchParams]);

  // 处理新增
  const handleAdd = () => {
    setCurrentRecord(null);
    form.resetFields();
    setModalVisible(true);
  };

  // 处理编辑
  const handleEdit = (record) => {
    setCurrentRecord(record);
    form.setFieldsValue({
      ...record,
      onsetDate: record.onsetDate ? dayjs(record.onsetDate) : null
    });
    setModalVisible(true);
  };

  // 处理标记康复
  const handleRecover = async (record) => {
    try {
      await apiRecoverDiseaseRecord(record.id);
      message.success("标记康复成功");
      loadDiseaseList();
      loadStatistics();
    } catch (error) {
      message.error(error.response?.data?.message || "标记康复失败");
    }
  };

  // 检查牲畜健康状态
  const checkLivestockHealth = async () => {
    setChecking(true);
    try {
      // 获取所有牲畜
      const livestockRes = await apiGetLivestockList({ pageNumber: 1, pageSize: 1000 });
      const livestockList = livestockRes.data.content || livestockRes.data.list || [];
      
      // 获取现有疾病记录，用于避免重复创建
      const diseaseRes = await apiGetDiseaseList({ 
        pageNumber: 1, 
        pageSize: 1000,
        condition: {} 
      });
      const existingDiseaseRecords = diseaseRes.data.content || diseaseRes.data.list || [];
      const existingLivestockCodes = existingDiseaseRecords
        .filter(record => record.status === "治疗中")
        .map(record => record.livestockCode);
      
      // 统计变量
      let checkedCount = 0;
      let unhealthyCount = 0;
      let addedCount = 0;
      
      // 检查每个牲畜的健康状态
      for (const livestock of livestockList) {
        checkedCount++;
        
        // 检查是否不健康且未记录
        if (livestock.healthStatus !== "健康" && !existingLivestockCodes.includes(livestock.livestockCode)) {
          unhealthyCount++;
          
          // 创建疾病记录
          try {
            await apiAddDiseaseRecord({
              diseaseName: "健康异常",
              livestockCode: livestock.livestockCode,
              onsetDate: dayjs().format("YYYY-MM-DD"),
              symptoms: `健康状态异常: ${livestock.healthStatus}`,
              treatment: "待诊断",
              status: "治疗中"
            });
            addedCount++;
          } catch (error) {
            console.error(`为牲畜 ${livestock.livestockCode} 创建疾病记录失败:`, error);
          }
        }
      }
      
      // 刷新数据
      await loadDiseaseList();
      await loadStatistics();
      
      // 显示结果
      message.success(`健康检查完成：共检查 ${checkedCount} 头牲畜，发现 ${unhealthyCount} 头不健康，新增 ${addedCount} 条疾病记录`);
    } catch (error) {
      message.error(error.response?.data?.message || "健康检查失败");
    } finally {
      setChecking(false);
    }
  };

  // 处理表单提交
  const handleSubmit = async (values) => {
    try {
      const recordData = {
        ...values,
        onsetDate: values.onsetDate.format("YYYY-MM-DD")
      };
      if (currentRecord) {
        await apiEditDiseaseRecord(currentRecord.id, recordData);
        message.success("编辑成功");
      } else {
        await apiAddDiseaseRecord(recordData);
        message.success("新增成功");
      }
      setModalVisible(false);
      loadDiseaseList();
      loadStatistics();
    } catch (error) {
      message.error(error.response?.data?.message || "操作失败");
    }
  };

  // 获取状态标签颜色
  const getStatusColor = (status) => {
    switch (status) {
      case "治疗中":
        return "orange";
      case "已康复":
        return "green";
      default:
        return "default";
    }
  };

  return (
    <Content title="疾病管理">
      {/* 统计卡片 */}
      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col span={8}>
          <Card>
            <div style={{ textAlign: "center" }}>
              <div style={{ fontSize: 24, fontWeight: "bold", color: "#1890ff" }}>{statistics.total}</div>
              <div>疾病记录总数</div>
            </div>
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <div style={{ textAlign: "center" }}>
              <div style={{ fontSize: 24, fontWeight: "bold", color: "#faad14" }}>{statistics.treating}</div>
              <div>治疗中</div>
            </div>
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <div style={{ textAlign: "center" }}>
              <div style={{ fontSize: 24, fontWeight: "bold", color: "#52c41a" }}>{statistics.recovered}</div>
              <div>已康复</div>
            </div>
          </Card>
        </Col>
      </Row>

      {/* 搜索栏 */}
      <div style={{ marginBottom: 16, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <Space>
          <Select
            placeholder="选择农场"
            value={searchParams.farmId}
            onChange={(value) => setSearchParams({ ...searchParams, farmId: value })}
            style={{ width: 150 }}
          >
            <Option value={null}>全部农场</Option>
            {farmList.map(farm => (
              <Option key={farm.id} value={farm.id}>{farm.farmName}</Option>
            ))}
          </Select>
          <Input
            placeholder="牲畜编号"
            value={searchParams.livestockCode}
            onChange={(e) => setSearchParams({ ...searchParams, livestockCode: e.target.value })}
            style={{ width: 150 }}
          />
          <Input
            placeholder="疾病名称"
            value={searchParams.diseaseName}
            onChange={(e) => setSearchParams({ ...searchParams, diseaseName: e.target.value })}
            style={{ width: 150 }}
          />
          <Select
            placeholder="状态"
            value={searchParams.status}
            onChange={(value) => setSearchParams({ ...searchParams, status: value })}
            style={{ width: 120 }}
          >
            <Option value={null}>全部状态</Option>
            <Option value="治疗中">治疗中</Option>
            <Option value="已康复">已康复</Option>
          </Select>
          <RangePicker
            placeholder={["开始日期", "结束日期"]}
            value={searchParams.dateRange}
            onChange={(dates) => setSearchParams({ ...searchParams, dateRange: dates })}
            style={{ width: 250 }}
          />
          <Button 
            type="default" 
            icon={<SearchOutlined />} 
            onClick={loadDiseaseList}
          >
            搜索
          </Button>
        </Space>
        <Space>
          <Button 
            type="default" 
            icon={<CheckCircleOutlined />} 
            onClick={checkLivestockHealth}
            loading={checking}
          >
            检查牲畜健康
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增疾病记录
          </Button>
        </Space>
      </div>

      {/* 表格 */}
      <Table
        dataSource={dataSource}
        rowKey="id"
        loading={loading}
      >
        <Column title="疾病名称" dataIndex="diseaseName" key="diseaseName" />
        <Column title="牲畜编号" dataIndex="livestockCode" key="livestockCode" />
        <Column title="发病日期" dataIndex="onsetDate" key="onsetDate" />
        <Column title="症状" dataIndex="symptoms" key="symptoms" ellipsis />
        <Column title="治疗措施" dataIndex="treatment" key="treatment" ellipsis />
        <Column
          title="状态"
          dataIndex="status"
          key="status"
          render={(text) => (
            <Tag color={getStatusColor(text)}>{text}</Tag>
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
                disabled={record.status === "已康复"}
              >
                编辑
              </Button>
              {record.status === "治疗中" && (
                <Button
                  type="link"
                  icon={<MedicineBoxOutlined />}
                  onClick={() => handleRecover(record)}
                >
                  标记康复
                </Button>
              )}
            </div>
          )}
        />
      </Table>

      {/* 新增/编辑弹窗 */}
      <Modal
        title={currentRecord ? "编辑疾病记录" : "新增疾病记录"}
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
            name="diseaseName"
            label="疾病名称"
            rules={[{ required: true, message: "请输入疾病名称" }]}
          >
            <Input placeholder="请输入疾病名称" />
          </Form.Item>
          <Form.Item
            name="livestockCode"
            label="牲畜编号"
            rules={[{ required: true, message: "请输入牲畜编号" }]}
          >
            <Input placeholder="请输入牲畜编号" />
          </Form.Item>
          <Form.Item
            name="onsetDate"
            label="发病日期"
            rules={[{ required: true, message: "请选择发病日期" }]}
          >
            <Input type="date" />
          </Form.Item>
          <Form.Item
            name="symptoms"
            label="症状"
            rules={[{ required: true, message: "请输入症状" }]}
          >
            <TextArea rows={3} placeholder="请输入症状" />
          </Form.Item>
          <Form.Item
            name="treatment"
            label="治疗措施"
            rules={[{ required: true, message: "请输入治疗措施" }]}
          >
            <TextArea rows={3} placeholder="请输入治疗措施" />
          </Form.Item>
        </Form>
      </Modal>
    </Content>
  );
}
