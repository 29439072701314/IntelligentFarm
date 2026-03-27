import React, { useState, useEffect } from "react";
import { Select } from "antd";
import { apiGetFarmList } from "../../services/farmApi";

const { Option } = Select;

export default function FarmSelect({ value, onChange, placeholder = "请选择农场", allowClear = true, ...props }) {
  const [farmList, setFarmList] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchFarmList();
  }, []);

  const fetchFarmList = async () => {
    setLoading(true);
    try {
      const res = await apiGetFarmList({ pageNum: 1, pageSize: 1000 });
      if (res.code === 200) {
        setFarmList(res.data.content || []);
      }
    } catch (error) {
      console.error("获取农场列表失败", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Select
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      allowClear={allowClear}
      loading={loading}
      style={{ width: "100%" }}
      {...props}
    >
      {farmList.map((farm) => (
        <Option key={farm.farmId} value={farm.farmId}>
          {farm.farmName}
        </Option>
      ))}
    </Select>
  );
}
