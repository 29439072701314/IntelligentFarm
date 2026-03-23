import React from "react";
import { Modal, Input, Form, InputNumber, Tag } from "antd";
import ProForm from "../../../../../../../component/ProForm";
import DeviceSelect from "../../../../../../../component/DeviceSelect";
import { FieldNumberOutlined, UserOutlined } from "@ant-design/icons";
import { apiAddRoom, apiEditRoom } from "../../../../../../../services/roomApi";

export default function RoomEditModal(props) {
  const { open, onClose, form } = props;
  const isEdit = Boolean(form.getFieldValue("roomId"));

  const handleRoom = async () => {
    const values = await form.validateFields();
    if (isEdit) {
      const res = await apiEditRoom(values);
      if (res.code === 200) {
        form.resetFields();
        onClose();
      }
    } else {
      const res = await apiAddRoom(values);
      if (res.code === 200) {
        form.resetFields();
        onClose();
      }
    }
  };

  return (
    open && (
      <Modal
        title={isEdit ? "编辑房间" : "添加房间"}
        onOk={handleRoom}
        open={open}
        onCancel={() => onClose(false)}
        centered
      >
        <ProForm
          formConfig={{
            formProps: {
              form,
              layout: "vertical",
            },
            items: [
              {
                props: {
                  name: "roomId",
                  hidden: true,
                },
              },
              {
                props: {
                  name: "roomNumber",
                  label: "房间号",
                  rules: [{ required: true, message: "请输入房间号" }],
                },
                render: <Input prefix={<FieldNumberOutlined />} />,
              },
              {
                props: {
                  name: "maxCapacity",
                  label: "房间容量",
                  rules: [{ required: true, message: "请输入房间容量" }],
                },
                render: (
                  <InputNumber
                    min={1}
                    prefix={<UserOutlined />}
                    placeholder="请输入房间容量"
                    style={{ width: "100%" }}
                  />
                ),
              },
              {
                props: {
                  name: "deviceId",
                  label: "设备",
                },
                render: <DeviceSelect style={{ width: "100%" }} />,
              },
            ],
          }}
        />
      </Modal>
    )
  );
}
