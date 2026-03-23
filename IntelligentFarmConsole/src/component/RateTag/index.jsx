import React from "react";
import { Tag } from "antd";

/**
 * 默认颜色阈值配置
 */
const DEFAULT_COLOR_THRESHOLDS = [
  { threshold: 80, color: "red" },
  { threshold: 50, color: "orange" },
  { threshold: 0, color: "green" },
];

/**
 * 百分率标签组件
 * @param {Object} props
 * @param {number} props.current - 当前值
 * @param {number} props.total - 总值
 * @param {string} [props.suffix] - 后缀，默认为空
 * @param {Object} [props.style] - 标签样式
 * @param {Array<{threshold: number, color: string}>} [props.colorThresholds] - 颜色阈值配置，按从高到低排序
 * @returns {JSX.Element}
 */
export default function RateTag({
  current,
  total,
  suffix,
  style,
  colorThresholds = DEFAULT_COLOR_THRESHOLDS,
  showPercentage = false,
  ...restProps
}) {
  const rate = total > 0 ? Math.round((current / total) * 100) : 0;

  const color =
    colorThresholds.find((item) => rate >= item.threshold)?.color || "default";

  return (
    <Tag color={color} style={style} {...restProps}>
      {showPercentage ? `${rate}%` : `${current} ${suffix}`}
    </Tag>
  );
}
