import React from "react";
import { Statistic, Flex, Card } from "antd";
import CountUp from "react-countup";
import { COLORS, GAP } from "../../../../../../constant";
const formatter = (value) => <CountUp end={value} />;

export default function DataStatistics(props) {
  const { statistics } = props;
  const dataList = [
    {
      title: "温度统计",
      value: statistics.temperature,
      suffix: "℃",
    },
    {
      title: "湿度统计",
      value: statistics.humidity,
      suffix: "%",
    },
    {
      title: "气体浓度统计",
      value: statistics.gasConcentration,
      suffix: "ppm",
    },
  ];
  return (
    <Flex justify="space-between" gap={GAP.MEDIUM}>
      {dataList.map((item) => (
        <Card
          hoverable
          key={item.title}
          title={item.title}
          style={{
            minWidth: `${90 / dataList.length}%`,
            backgroundColor: COLORS.BG_LAYOUT,
          }}
        >
          <Flex justify="space-between">
            <Statistic
              title="平均值"
              value={item.value.avg}
              suffix={item.suffix}
              formatter={formatter}
            />
            <Statistic
              title="最大值"
              value={item.value.max}
              suffix={item.suffix}
              formatter={formatter}
            />
            <Statistic
              title="最小值"
              value={item.value.min}
              suffix={item.suffix}
              formatter={formatter}
            />
          </Flex>
        </Card>
      ))}
    </Flex>
  );
}
