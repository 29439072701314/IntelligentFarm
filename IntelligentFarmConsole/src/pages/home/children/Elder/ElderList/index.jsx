import React, { useState } from "react";
import { apiGetUserList } from "@/services/userApi";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import UserSelectTable from "@/component/UserSelectTable";
import { Form, Modal } from "antd";
import { getColumns } from "./constant";
import { ROLE } from "@/constant";
import {
  apiGetUserListByIds,
  apiBindHealthDevice,
  apiBindFamily,
  apiBindCaregiver,
} from "@/services/userApi";
import HealthDeviceSelect from "@/component/HealthDeviceSelect";
import { BIND_OPTION } from "./constant";

export default function ElderList() {
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState();
  const [currentUserRecord, setCurrentUserRecord] = useState();
  const [form] = Form.useForm();
  const [visible, setVisible] = useState("");
  const onClose = () => {
    setVisible("");
  };
  // 搜索前处理，默认只查询老人角色
  const handleBeforeSearch = (values) => {
    const [minBirthday, maxBirthday] = values.birthday || [];
    values.birthday = undefined;
    return {
      ...values,
      role: ROLE.ELDER, // 默认只查询老人
      minBirthday: minBirthday?.format("YYYY-MM-DD"),
      maxBirthday: maxBirthday?.format("YYYY-MM-DD"),
    };
  };
  const handleConfirm = async (ids) => {
    switch (visible) {
      // 绑定健康设备
      case BIND_OPTION.deviceBind: {
        await apiBindHealthDevice({
          elderId: currentUserRecord.userId,
          healthDeviceIds: ids,
        });
        break;
      }
      // 绑定家属
      case BIND_OPTION.familyBind: {
        await apiBindFamily({
          elderId: currentUserRecord.userId,
          familyIds: ids,
        });
        break;
      }
      // 绑定护理人员
      case BIND_OPTION.caregiverBind: {
        await apiBindCaregiver({
          elderId: currentUserRecord.userId,
          caregiverIds: ids,
        });
        break;
      }
    }
    setCurrentUserRecord(null);
    onClose();
    form.getData();
  };
  // 点击操作按钮
  const handleOptionClick = async (option, record) => {
    setCurrentUserRecord(record);
    switch (option) {
      case BIND_OPTION.deviceBind: {
        setSelectedDevice(record.healthDevice?.deviceId || undefined);
        break;
      }
      case BIND_OPTION.familyBind: {
        const res = await apiGetUserListByIds(record.familyIds || []);
        setSelectedUsers(res?.data || []);
        break;
      }
      case BIND_OPTION.caregiverBind: {
        const res = await apiGetUserListByIds(record.caregiverIds || []);
        setSelectedUsers(res?.data || []);
        break;
      }
    }
    setVisible(option);
  };
  return (
    <Content title="老人列表">
      <ProTable
        rowKey="userId"
        form={form}
        api={apiGetUserList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns(handleOptionClick)}
      />
      {visible === BIND_OPTION.deviceBind && (
        <Modal
          title="绑定健康设备"
          open={visible === BIND_OPTION.deviceBind}
          onCancel={onClose}
          onOk={() => handleConfirm([selectedDevice])}
          centered
        >
          <HealthDeviceSelect
            value={selectedDevice}
            onChange={setSelectedDevice}
          />
        </Modal>
      )}
      {visible === BIND_OPTION.familyBind && (
        <UserSelectTable
          visible={visible === BIND_OPTION.familyBind}
          onClose={onClose}
          title={"绑定家属"}
          selectedUsers={selectedUsers}
          role={ROLE.FAMILY}
          onConfirm={handleConfirm}
        />
      )}
      {visible === BIND_OPTION.caregiverBind && (
        <UserSelectTable
          visible={visible === BIND_OPTION.caregiverBind}
          onClose={onClose}
          title={"绑定护理人员"}
          selectedUsers={selectedUsers}
          role={ROLE.CAREGIVER}
          onConfirm={handleConfirm}
          selectionType="radio"
        />
      )}
    </Content>
  );
}
