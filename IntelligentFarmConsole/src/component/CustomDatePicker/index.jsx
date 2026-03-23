import React, { useState, useEffect } from "react";
import { DatePicker, Select, Space } from "antd";
import dayjs from "dayjs";

const { RangePicker: AntdRangePicker } = DatePicker;

/**
 * 时间预设选项配置
 * @property {string} label - 显示文本
 * @property {string} value - 选项值
 * @property {number|null} hours - 对应的小时数，null表示自定义模式
 */
const TIME_PRESETS = [
  { label: "一小时内", value: "1hour", hours: 1 },
  { label: "一天内", value: "1day", hours: 24 },
  { label: "三天内", value: "3days", hours: 72 },
  { label: "一周内", value: "1week", hours: 168 },
  { label: "自定义", value: "custom", hours: null },
];

/**
 * 自定义时间范围选择器组件
 * @description 结合预设时间范围下拉框和时间范围选择器，支持快速选择常用时间范围
 * @param {Object} props - 组件属性
 * @param {[dayjs, dayjs]} props.value - 选中的时间范围（受控模式，由Form传入）
 * @param {Function} props.onChange - 时间变化回调函数（由Form传入）
 * @param {boolean} [props.showTime=true] - 是否显示时间选择器
 * @param {string} [props.format="YYYY-MM-DD HH:mm:ss"] - 时间显示格式
 * @returns {React.ReactElement} 时间范围选择器组件
 * @example
 * // 基础使用
 * <CustomDatePicker onChange={(dates) => console.log(dates)} />
 *
 * // 自定义格式
 * <CustomDatePicker format="YYYY-MM-DD" showTime={false} />
 */
export default function CustomDatePicker({
  value,
  onChange,
  showTime = true,
  format = "YYYY-MM-DD HH:mm:ss",
  ...props
}) {
  // 当前选中的预设时间类型
  const [preset, setPreset] = useState("1hour");

  // 内部维护的时间范围状态（用于非受控模式）
  const [internalDateRange, setInternalDateRange] = useState(() => {
    const end = dayjs();
    const start = end.subtract(1, "hour");
    return [start, end];
  });

  // 实际使用的时间范围：优先使用外部传入的value，否则使用内部状态
  const dateRange = value || internalDateRange;

  // 组件挂载时，触发一次onChange以初始化Form的值
  useEffect(() => {
    if (!value && onChange) {
      onChange(internalDateRange);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * 处理预设时间选择变化
   * @param {string} selectedPreset - 选中的预设时间类型
   */
  const handlePresetChange = (selectedPreset) => {
    setPreset(selectedPreset);

    // 选择"自定义"时不自动设置时间范围
    if (selectedPreset === "custom") {
      return;
    }

    // 根据预设配置计算时间范围
    const presetConfig = TIME_PRESETS.find((p) => p.value === selectedPreset);
    if (presetConfig && presetConfig.hours) {
      const end = dayjs();
      const start = end.subtract(presetConfig.hours, "hour");
      const newRange = [start, end];
      setInternalDateRange(newRange);
      onChange?.(newRange);
    }
  };

  /**
   * 处理时间范围手动选择变化
   * @param {[dayjs, dayjs]} dates - 选中的时间范围数组
   */
  const handleRangeChange = (dates) => {
    // 手动选择时自动切换到"自定义"模式
    setPreset("custom");
    setInternalDateRange(dates);
    onChange?.(dates);
  };

  return (
    <Space.Compact style={{ width: "100%" }}>
      {/* 预设时间范围下拉选择框 */}
      <Select
        value={preset}
        onChange={handlePresetChange}
        style={{ width: 120 }}
        options={TIME_PRESETS.map((p) => ({ label: p.label, value: p.value }))}
      />
      {/* 时间范围选择器 */}
      <AntdRangePicker
        {...props}
        value={dateRange}
        onChange={handleRangeChange}
        showTime={showTime}
        format={format}
        style={{ flex: 1 }}
        placeholder={["开始时间", "结束时间"]}
      />
    </Space.Compact>
  );
}
