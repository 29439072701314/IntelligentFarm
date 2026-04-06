import React, { useState } from "react";
import { useNavigate } from "react-router";
import { apiGetFarmList, apiDeleteFarm } from "@/services/farmApi";
import { apiUnbindDeviceFromFarm } from "@/services/deviceApi";
import { apiGetPlanList, apiExecutePlan } from "@/services/feedApi";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Modal, Button, message, notification } from "antd";
import { getColumns } from "./constant";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import FarmEditModal from "./component/FarmEditModal";
import BindDeviceModal from "./component/BindDeviceModal";

const { confirm } = Modal;

export default function FarmList() {
  const navigate = useNavigate();
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [form] = Form.useForm();
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [currentFarm, setCurrentFarm] = useState(null);
  const [bindDeviceModalVisible, setBindDeviceModalVisible] = useState(false);
  const [currentBindFarmId, setCurrentBindFarmId] = useState(null);

  // 搜索前处理
  const handleBeforeSearch = (values) => {
    return {
      ...values,
    };
  };

  // 删除农场
  const handleDelete = (record) => {
    confirm({
      title: "提示",
      content: `确认删除农场 ${record.farmName} 吗？`,
      onOk: async () => {
        try {
          await apiDeleteFarm(record.farmId);
          form.getData();
        } catch (error) {
          message.error(error.response?.data?.message || "删除失败");
        }
      },
    });
  };

  // 添加农场
  const handleAdd = () => {
    setCurrentFarm(null);
    setEditModalVisible(true);
  };

  // 编辑农场
  const handleEdit = (record) => {
    setCurrentFarm(record);
    setEditModalVisible(true);
  };

  // 查看牲畜
  const handleViewLivestock = (record) => {
    navigate(`/home/livestock/livestockList?farmId=${record.farmId}`);
  };

  // 绑定设备
  const handleBindDevice = (record) => {
    setCurrentBindFarmId(record.farmId);
    setBindDeviceModalVisible(true);
  };

  // 绑定设备成功回调
  const handleBindDeviceSuccess = () => {
    setBindDeviceModalVisible(false);
    form.getData(); // 重新加载农场列表
  };

  // 解绑设备
  const handleUnbindDevice = (record) => {
    confirm({
      title: "提示",
      content: `确认解绑农场 ${record.farmName} 的设备吗？`,
      onOk: async () => {
        try {
          await apiUnbindDeviceFromFarm({ deviceId: record.deviceId });
          message.success("解绑设备成功");
          form.getData(); // 重新加载农场列表
        } catch (error) {
          message.error(error.response?.data?.message || "解绑设备失败");
        }
      },
    });
  };

  // 一键喂养
  const handleFeed = async (record) => {
    try {
      // 获取当前农场的待执行投喂计划
      const res = await apiGetPlanList({ pageNumber: 1, pageSize: 100 });
      const plans = res.data.content || res.data.list || [];
      const farmPlans = plans.filter(plan => 
        plan.area.toString() === record.farmId.toString() && plan.status === "待执行"
      );

      if (farmPlans.length === 0) {
        message.info(`农场 ${record.farmName} 没有待执行的投喂计划`);
        return;
      }

      // 执行所有待执行的计划
      let successCount = 0;
      let failCount = 0;

      for (const plan of farmPlans) {
        try {
          const result = await apiExecutePlan(plan.id);
          // 检查执行结果是否成功
          if (result.code === 200) {
            successCount++;
          } else {
            failCount++;
            console.error(`执行计划 ${plan.id} 失败:`, result.message);
          }
        } catch (error) {
          failCount++;
          console.error(`执行计划 ${plan.id} 失败:`, error);
        }
      }

      // 显示执行结果
      if (successCount > 0 || failCount > 0) {
        const type = failCount === 0 ? "success" : "warning";
        notification[type]({
          message: "喂养执行结果",
          description: `农场 ${record.farmName} 共执行 ${farmPlans.length} 个投喂计划，成功 ${successCount} 个，失败 ${failCount} 个`,
          duration: 5,
        });
      }

    } catch (error) {
      message.error(error.response?.data?.message || "获取投喂计划失败");
    }
  };

  // 批量删除农场
  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning("请选择要删除的农场");
      return;
    }
    confirm({
      title: "提示",
      content: `确认删除选中的 ${selectedRowKeys.length} 个农场吗？`,
      onOk: async () => {
        try {
          for (const farmId of selectedRowKeys) {
            await apiDeleteFarm(farmId);
          }
          setSelectedRowKeys([]);
          form.getData();
        } catch (error) {
          message.error(error.response?.data?.message || "批量删除失败");
        }
      },
    });
  };

  return (
    <Content title="农场列表">
      <ProTable
        rowKey="farmId"
        rowSelection={{
          type: "checkbox",
          selectedRowKeys: selectedRowKeys,
          preserveSelectedRowKeys: true,
          onChange: (selectedRowKeys, selectedRows) => {
            setSelectedRowKeys(selectedRowKeys);
          },
        }}
        form={form}
        api={apiGetFarmList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns(handleEdit, handleDelete, handleViewLivestock, handleBindDevice, handleUnbindDevice, handleFeed)}
        extraOptions={[
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            添加
          </Button>,
          <Button
            icon={<DeleteOutlined />}
            type="primary"
            danger
            onClick={handleBatchDelete}
          >
            批量删除
          </Button>,
        ]}
      />
      <FarmEditModal
        visible={editModalVisible}
        onClose={() => setEditModalVisible(false)}
        onSuccess={() => {
          setEditModalVisible(false);
          form.getData();
        }}
        farm={currentFarm}
      />
      <BindDeviceModal
        visible={bindDeviceModalVisible}
        onClose={() => setBindDeviceModalVisible(false)}
        onSuccess={handleBindDeviceSuccess}
        farmId={currentBindFarmId}
      />
    </Content>
  );
}