import { COLORS } from "@/constant";

export const initOptions = {
  backgroundColor: "rgba(255, 255, 255, 0)",
  tooltip: {
    trigger: "axis",
    backgroundColor: "rgba(255, 255, 255, 0.95)",
    borderColor: COLORS.BORDER_SECONDARY,
    borderWidth: 1,
    textStyle: {
      color: COLORS.TEXT_BASE,
    },
    axisPointer: {
      type: "line",
      lineStyle: {
        color: COLORS.PRIMARY,
        type: "dashed",
      },
    },
  },
  grid: {
    left: "3%",
    right: "4%",
    bottom: "15%",
    containLabel: true,
  },
  toolbox: {
    feature: {
      saveAsImage: {},
    },
  },
  dataZoom: [
    {
      type: "inside",
      start: 0,
      end: 100,
    },
    {
      start: 0,
      end: 100,
      bottom: 10,
      height: 20,
      handleStyle: {
        color: COLORS.PRIMARY,
        shadowBlur: 3,
        shadowColor: "rgba(0, 0, 0, 0.2)",
        shadowOffsetX: 1,
        shadowOffsetY: 1,
      },
    },
  ],
  legend: {
    data: [],
    textStyle: {
      color: COLORS.TEXT_SECONDARY,
    },
  },
  xAxis: {
    data: [],
    axisLine: {
      lineStyle: {
        color: COLORS.BORDER_SECONDARY,
      },
    },
    axisLabel: {
      color: COLORS.TEXT_TERTIARY,
    },
    axisTick: {
      show: false,
    },
  },
  yAxis: {
    axisLine: {
      show: false,
    },
    axisLabel: {
      color: COLORS.TEXT_TERTIARY,
    },
    splitLine: {
      lineStyle: {
        color: COLORS.FILL_QUATERNARY,
        type: "dashed",
      },
    },
  },
  series: [],
};

const calcStats = (values) => {
  const validValues = values.filter((v) => v != null);
  if (validValues.length === 0) return { avg: "0", max: "0", min: "0" };
  const sum = validValues.reduce((acc, val) => acc + val, 0);
  return {
    avg: (sum / validValues.length).toFixed(1),
    max: Math.max(...validValues).toFixed(1),
    min: Math.min(...validValues).toFixed(1),
  };
};

const createSeries = (name, colorConfig) => ({
  sampling: "lttb",
  name,
  type: "line",
  data: [],
  smooth: true,
  symbol: "circle",
  symbolSize: 6,
  showSymbol: false,
  lineStyle: {
    width: 3,
    color: {
      type: "linear",
      x: 0,
      y: 0,
      x2: 1,
      y2: 0,
      colorStops: [
        { offset: 0, color: colorConfig.lineStart },
        { offset: 1, color: colorConfig.lineEnd },
      ],
    },
  },
  itemStyle: {
    color: colorConfig.lineStart,
    borderColor: COLORS.BG_BASE,
    borderWidth: 2,
  },
  areaStyle: {
    color: {
      type: "linear",
      x: 0,
      y: 0,
      x2: 0,
      y2: 1,
      colorStops: [
        { offset: 0, color: colorConfig.areaStart },
        { offset: 1, color: colorConfig.areaEnd },
      ],
    },
  },
});

const createChartOptions = (title, legendData, series) => ({
  ...initOptions,
  title: {
    left: "left",
    text: title,
    textStyle: {
      color: COLORS.TEXT_BASE,
      fontSize: 16,
      fontWeight: "bold",
    },
  },
  legend: {
    data: legendData,
    top: 20,
    textStyle: {
      color: COLORS.TEXT_SECONDARY,
    },
  },
  xAxis: { data: [] },
  series,
});

const SERIES_COLORS = {
  temperature: {
    lineStart: COLORS.ERROR,
    lineEnd: "#ff888a",
    areaStart: "rgba(255, 77, 79, 0.25)",
    areaEnd: "rgba(255, 77, 79, 0.02)",
  },
  humidity: {
    lineStart: COLORS.PRIMARY,
    lineEnd: "#9a8ee8",
    areaStart: "rgba(114, 101, 227, 0.25)",
    areaEnd: "rgba(114, 101, 227, 0.02)",
  },
  gasConcentration: {
    lineStart: COLORS.SUCCESS,
    lineEnd: "#7dd952",
    areaStart: "rgba(82, 196, 26, 0.25)",
    areaEnd: "rgba(82, 196, 26, 0.02)",
  },
};

export const buildOptions = (content) => {
  const temperatures = [];
  const humidities = [];
  const gasConcentrations = [];

  const temperatureHumidityOptions = createChartOptions(
    "温湿度组合图",
    ["温度", "湿度"],
    [
      createSeries("温度", SERIES_COLORS.temperature),
      createSeries("湿度", SERIES_COLORS.humidity),
    ]
  );

  const gasConcentrationOptions = createChartOptions(
    "气体浓度",
    ["气体浓度"],
    [createSeries("气体浓度", SERIES_COLORS.gasConcentration)]
  );

  content.forEach((item) => {
    const time = new Date(item.time).toLocaleString();
    temperatureHumidityOptions.xAxis.data.push(time);
    temperatureHumidityOptions.series[0].data.push(item.temperature);
    temperatureHumidityOptions.series[1].data.push(item.humidity);
    gasConcentrationOptions.xAxis.data.push(time);
    gasConcentrationOptions.series[0].data.push(item.gasConcentration);

    temperatures.push(item.temperature);
    humidities.push(item.humidity);
    gasConcentrations.push(item.gasConcentration);
  });

  return {
    temperatureHumidityOptions,
    gasConcentrationOptions,
    statistics: {
      temperature: calcStats(temperatures),
      humidity: calcStats(humidities),
      gasConcentration: calcStats(gasConcentrations),
    },
  };
};
