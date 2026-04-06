import React, { useState, useEffect, useRef } from "react";
import Content from "@/component/Content";
import { Select, Spin, Card, Radio, message, Row, Col, Statistic, Progress } from "antd";
import * as echarts from "echarts";
import { apiGetWeightRecordsByLivestockId, apiGetAverageWeightByFarmId, apiInitializeWeightRecords } from "@/services/livestockWeightApi";
import { apiGetLivestockList } from "@/services/livestockApi";
import { apiGetFarmList } from "@/services/farmApi";

const { Option } = Select;
const { Group, Button: RadioButton } = Radio;

export default function LivestockWeightChart() {
  // 图表引用
  const weightCurveRef = useRef(null);
  const healthPieRef = useRef(null);
  
  // 图表实例
  const weightCurveInstance = useRef(null);
  const healthPieInstance = useRef(null);
  
  // 状态管理
  const [loading, setLoading] = useState(false);
  const [livestockList, setLivestockList] = useState([]);
  const [farmList, setFarmList] = useState([]);
  const [selectedFarm, setSelectedFarm] = useState(null);
  const [selectedLivestockType, setSelectedLivestockType] = useState('all'); // all, 其他牲畜种类
  const [livestockTypes, setLivestockTypes] = useState([]);
  const [chartMode, setChartMode] = useState('farm'); // farm: 农场平均, individual: 单个牲畜
  const [selectedLivestock, setSelectedLivestock] = useState(null);
  const [weightData, setWeightData] = useState({
    averageWeight: 0,
    minWeight: 0,
    maxWeight: 0,
    dailyGain: 0,
    gainTrend: 'stable' // up, down, stable
  });
  const [healthData, setHealthData] = useState({
    healthRate: 0,
    statusDistribution: {
      healthy: 0,
      subHealthy: 0,
      sick: 0,
      dead: 0
    },
    abnormalCount: 0
  });
  const [weightTrendData, setWeightTrendData] = useState([]);
  const [initializing, setInitializing] = useState(false);

  // 加载农场列表
  const loadFarmList = async () => {
    try {
      const res = await apiGetFarmList({ pageNumber: 1, pageSize: 100 });
      let farms = [];
      if (res.data && res.data.content) {
        farms = res.data.content;
      } else if (res.data && res.data.list) {
        farms = res.data.list;
      } else if (Array.isArray(res.data)) {
        farms = res.data;
      }
      setFarmList(farms);
      if (farms.length > 0) {
        setSelectedFarm(farms[0].farmId);
      }
    } catch (error) {
      console.error("获取农场列表失败", error);
      message.error("获取农场列表失败");
    }
  };

  // 加载牲畜列表
  const loadLivestockList = async () => {
    if (!selectedFarm) return;

    try {
      console.log("开始加载牲畜列表，农场ID:", selectedFarm);
      const res = await apiGetLivestockList({ 
        pageNumber: 1, 
        pageSize: 100, 
        condition: { farmId: selectedFarm } 
      });
      console.log("获取牲畜列表响应:", res);
      let livestock = [];
      if (res.data && res.data.content) {
        livestock = res.data.content;
      } else if (res.data && res.data.list) {
        livestock = res.data.list;
      } else if (Array.isArray(res.data)) {
        livestock = res.data;
      } else if (res.code === 200 && Array.isArray(res.data)) {
        livestock = res.data;
      }
      console.log("处理后牲畜数据:", livestock);
      
      setLivestockList(livestock);
      
      // 提取所有不同的牲畜种类
      const types = [...new Set(livestock.map(item => item.livestockType).filter(Boolean))];
      console.log("提取的牲畜种类:", types);
      setLivestockTypes(types);
      
      calculateWeightData(livestock);
      calculateHealthData(livestock);
    } catch (error) {
      console.error("获取牲畜列表失败", error);
      message.error("获取牲畜列表失败");
    }
  };

  // 计算体重数据
  const calculateWeightData = (livestock, type = selectedLivestockType) => {
    if (livestock.length === 0) {
      setWeightData({
        averageWeight: 0,
        minWeight: 0,
        maxWeight: 0,
        dailyGain: 0,
        gainTrend: 'stable'
      });
      return;
    }

    // 过滤牲畜类型
    const filteredLivestock = type === 'all' 
      ? livestock 
      : livestock.filter(item => {
          // 忽略大小写和空格进行比较
          const itemType = item.livestockType ? String(item.livestockType).trim().toLowerCase() : '';
          const targetType = type ? String(type).trim().toLowerCase() : '';
          return itemType === targetType;
        });

    if (filteredLivestock.length === 0) {
      setWeightData({
        averageWeight: 0,
        minWeight: 0,
        maxWeight: 0,
        dailyGain: 0,
        gainTrend: 'stable'
      });
      return;
    }

    // 计算平均体重
    console.log("过滤后的牲畜数据:", filteredLivestock);
    const weights = filteredLivestock.map(item => {
      console.log("牲畜ID:", item.livestockId, "体重:", item.weight, "类型:", typeof item.weight);
      return item.weight;
    }).filter(w => w !== null && w !== undefined && !isNaN(w));
    console.log("提取的体重数据:", weights);
    if (weights.length === 0) {
      setWeightData({
        averageWeight: 0,
        minWeight: 0,
        maxWeight: 0,
        dailyGain: 0,
        gainTrend: 'stable'
      });
      return;
    }

    const averageWeight = weights.reduce((sum, w) => sum + w, 0) / weights.length;
    const minWeight = Math.min(...weights);
    const maxWeight = Math.max(...weights);

    // 模拟日增重（实际应该从体重记录中计算）
    const dailyGain = Math.round(Math.random() * 200 + 50);
    const gainTrend = Math.random() > 0.5 ? 'up' : 'down';

    const calculatedData = {
      averageWeight: Math.round(averageWeight * 100) / 100,
      minWeight: Math.round(minWeight * 100) / 100,
      maxWeight: Math.round(maxWeight * 100) / 100,
      dailyGain,
      gainTrend
    };
    console.log("计算出的体重数据:", calculatedData);
    setWeightData(calculatedData);
  };

  // 计算健康数据
  const calculateHealthData = (livestock, type = selectedLivestockType) => {
    if (livestock.length === 0) {
      setHealthData({
        healthRate: 0,
        statusDistribution: {
          healthy: 0,
          subHealthy: 0,
          sick: 0,
          dead: 0
        },
        abnormalCount: 0
      });
      return;
    }

    // 过滤牲畜类型
    const filteredLivestock = type === 'all' 
      ? livestock 
      : livestock.filter(item => {
          // 忽略大小写和空格进行比较
          const itemType = item.livestockType ? String(item.livestockType).trim().toLowerCase() : '';
          const targetType = type ? String(type).trim().toLowerCase() : '';
          return itemType === targetType;
        });

    if (filteredLivestock.length === 0) {
      setHealthData({
        healthRate: 0,
        statusDistribution: {
          healthy: 0,
          subHealthy: 0,
          sick: 0,
          dead: 0
        },
        abnormalCount: 0
      });
      return;
    }

    // 计算健康状态分布
    const statusDistribution = {
      healthy: 0,
      subHealthy: 0,
      sick: 0,
      dead: 0
    };

    filteredLivestock.forEach(item => {
      switch (item.healthStatus) {
        case '健康':
          statusDistribution.healthy++;
          break;
        case '亚健康':
          statusDistribution.subHealthy++;
          break;
        case '患病':
        case '治疗中':
          statusDistribution.sick++;
          break;
        case '死亡':
          statusDistribution.dead++;
          break;
        default:
          break;
      }
    });

    const healthRate = Math.round((statusDistribution.healthy / filteredLivestock.length) * 100);
    const abnormalCount = statusDistribution.subHealthy + statusDistribution.sick;

    setHealthData({
      healthRate,
      statusDistribution,
      abnormalCount
    });

    // 绘制健康状态饼图
    drawHealthPieChart(statusDistribution);
  };

  // 加载农场平均体重趋势数据
  const loadFarmAverageWeightData = async () => {
    if (!selectedFarm) return;

    try {
      const res = await apiGetAverageWeightByFarmId(selectedFarm);
      if (res.code === 200 && res.data) {
        setWeightTrendData(res.data);
        drawWeightCurveChart(res.data);
      }
    } catch (error) {
      console.error("获取农场平均体重数据失败", error);
    }
  };

  // 加载单个牲畜体重趋势数据
  const loadIndividualLivestockWeightData = async (livestockId) => {
    if (!livestockId) return;

    try {
      const res = await apiGetWeightRecordsByLivestockId(livestockId);
      if (res.code === 200 && res.data) {
        const data = res.data.map(record => ({
          date: record.recordTime?.substring(0, 10) || '',
          averageWeight: record.weight
        }));
        setWeightTrendData(data);
        drawWeightCurveChart(data);
      }
    } catch (error) {
      console.error("获取单个牲畜体重数据失败", error);
    }
  };

  // 绘制体重曲线图表
  const drawWeightCurveChart = (data) => {
    if (!weightCurveRef.current) return;

    if (!weightCurveInstance.current) {
      weightCurveInstance.current = echarts.init(weightCurveRef.current);
    }

    const option = {
      title: {
        text: chartMode === 'farm' ? '农场平均体重成长曲线' : '单个牲畜体重成长曲线',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        formatter: function(params) {
          return params[0].name + '<br/>' + params[0].seriesName + ': ' + params[0].value + ' kg';
        }
      },
      xAxis: {
        type: 'category',
        data: data.map(item => item.date),
        axisLabel: {
          rotate: 45
        }
      },
      yAxis: {
        type: 'value',
        name: '体重 (kg)',
        axisLabel: {
          formatter: '{value} kg'
        }
      },
      series: [
        {
          name: '平均体重',
          type: 'line',
          data: data.map(item => item.averageWeight),
          smooth: true,
          symbol: 'circle',
          symbolSize: 6,
          lineStyle: {
            width: 2,
            color: '#1890ff'
          },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: 'rgba(24, 144, 255, 0.3)'
                },
                {
                  offset: 1,
                  color: 'rgba(24, 144, 255, 0.1)'
                }
              ]
            }
          }
        }
      ]
    };

    weightCurveInstance.current.setOption(option);
  };

  // 绘制健康状态饼图
  const drawHealthPieChart = (data) => {
    if (!healthPieRef.current) return;

    if (!healthPieInstance.current) {
      healthPieInstance.current = echarts.init(healthPieRef.current);
    }

    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: ['健康', '亚健康', '生病', '死亡']
      },
      series: [
        {
          name: '健康状态',
          type: 'pie',
          radius: '50%',
          data: [
            { value: data.healthy, name: '健康', itemStyle: { color: '#52c41a' } },
            { value: data.subHealthy, name: '亚健康', itemStyle: { color: '#faad14' } },
            { value: data.sick, name: '生病', itemStyle: { color: '#f5222d' } },
            { value: data.dead, name: '死亡', itemStyle: { color: '#8c8c8c' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    healthPieInstance.current.setOption(option);
  };

  // 初始化体重记录
  const handleInitializeRecords = async () => {
    setInitializing(true);
    try {
      const res = await apiInitializeWeightRecords();
      if (res.code === 200) {
        message.success(res.message || "初始化成功");
        // 重新加载数据
        loadLivestockList();
        loadWeightTrendData();
      } else {
        message.error(res.message || "初始化失败");
      }
    } catch (error) {
      console.error("初始化体重记录失败", error);
      message.error("初始化体重记录失败");
    } finally {
      setInitializing(false);
    }
  };

  // 处理农场选择变化
  const handleFarmChange = (value) => {
    setSelectedFarm(value);
  };

  // 处理牲畜类型选择变化
  const handleLivestockTypeChange = (value) => {
    setSelectedLivestockType(value);
    // 重新计算数据，使用完整的牲畜列表，并传递新的类型值
    calculateWeightData(livestockList, value);
    calculateHealthData(livestockList, value);
    
    // 如果当前是单个牲畜模式，需要更新牲畜选择器
    if (chartMode === 'individual' && selectedLivestock) {
      // 检查当前选择的牲畜是否符合新的种类筛选条件
      const selectedLivestockObj = livestockList.find(item => item.livestockId === selectedLivestock);
      if (!selectedLivestockObj || (value !== 'all' && selectedLivestockObj.livestockType !== value)) {
        // 如果不符合，清空选择
        setSelectedLivestock(null);
      }
    }
  };

  // 监听农场变化
  useEffect(() => {
    if (selectedFarm) {
      loadLivestockList();
      if (chartMode === 'farm') {
        loadFarmAverageWeightData();
      }
    }
  }, [selectedFarm]);

  // 监听图表模式变化
  useEffect(() => {
    if (chartMode === 'farm') {
      setSelectedLivestock(null);
      if (selectedFarm) {
        loadFarmAverageWeightData();
      }
    } else if (chartMode === 'individual' && selectedLivestock) {
      loadIndividualLivestockWeightData(selectedLivestock);
    }
  }, [chartMode, selectedFarm, selectedLivestock]);

  // 初始化
  useEffect(() => {
    loadFarmList();
  }, []);

  // 响应式处理
  useEffect(() => {
    const handleResize = () => {
      if (weightCurveInstance.current) {
        weightCurveInstance.current.resize();
      }
      if (healthPieInstance.current) {
        healthPieInstance.current.resize();
      }
    };

    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  console.log("渲染时的 weightData:", weightData);
  return (
    <Content title="农场牲畜数据大屏">
      <div style={{ marginBottom: 20, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', gap: 20, flexWrap: 'wrap' }}>
          <div>
            <span style={{ marginRight: 8 }}>选择农场：</span>
            <Select
              placeholder="选择农场"
              style={{ width: 200 }}
              value={selectedFarm}
              onChange={handleFarmChange}
            >
              {farmList.map(farm => (
                <Option key={farm.farmId} value={farm.farmId}>
                  {farm.farmName}
                </Option>
              ))}
            </Select>
          </div>
          <div>
            <span style={{ marginRight: 8 }}>牲畜种类：</span>
            <Group onChange={handleLivestockTypeChange} value={selectedLivestockType}>
              <RadioButton value="all">全部</RadioButton>
              {livestockTypes.map(type => (
                <RadioButton key={type} value={type}>{type}</RadioButton>
              ))}
            </Group>
          </div>


        </div>
        <button
          style={{
            padding: '8px 16px',
            backgroundColor: '#1890ff',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
            fontSize: '14px'
          }}
          onClick={handleInitializeRecords}
          disabled={initializing}
        >
          {initializing ? '初始化中...' : '初始化体重记录'}
        </button>
      </div>

      <Row gutter={[16, 16]}>
        {/* 体重数据卡片 */}
        <Col span={8}>
          <Card title="体重数据" style={{ height: '100%' }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
              <Statistic 
                title="当前平均体重" 
                value={weightData.averageWeight} 
                suffix="kg" 
                valueStyle={{ color: '#1890ff' }}
              />
              <Statistic 
                title="体重范围" 
                value={`${weightData.minWeight} ~ ${weightData.maxWeight}`} 
                suffix="kg"
              />
              <div>
                <div style={{ marginBottom: 8, fontSize: '14px', color: '#666' }}>近7日日增重</div>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <Statistic 
                    value={weightData.dailyGain} 
                    suffix="g/天" 
                    valueStyle={{ color: weightData.gainTrend === 'up' ? '#52c41a' : weightData.gainTrend === 'down' ? '#f5222d' : '#faad14' }}
                  />
                  <span style={{ fontSize: '18px' }}>
                    {weightData.gainTrend === 'up' ? '↑' : weightData.gainTrend === 'down' ? '↓' : '→'}
                  </span>
                </div>
              </div>
            </div>
          </Card>
        </Col>

        {/* 健康数据卡片 */}
        <Col span={8}>
          <Card title="健康数据" style={{ height: '100%' }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
              <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div style={{ width: 150, height: 150 }} ref={healthPieRef} />
              </div>
              <div style={{ textAlign: 'center' }}>
                <Progress 
                  type="circle" 
                  percent={healthData.healthRate} 
                  format={percent => `${percent}%`} 
                  size={120}
                  strokeColor={{ from: '#108ee9', to: '#87d068' }}
                />
                <div style={{ marginTop: 8, fontSize: '14px' }}>整体健康率</div>
              </div>
              <Statistic 
                title="异常牲畜总数" 
                value={healthData.abnormalCount} 
                valueStyle={{ color: '#f5222d' }}
              />
              <div style={{ display: 'flex', justifyContent: 'space-around', fontSize: '14px' }}>
                <div>
                  <span style={{ display: 'inline-block', width: 12, height: 12, backgroundColor: '#52c41a', borderRadius: '50%', marginRight: 4 }}></span>
                  健康: {healthData.statusDistribution.healthy}
                </div>
                <div>
                  <span style={{ display: 'inline-block', width: 12, height: 12, backgroundColor: '#faad14', borderRadius: '50%', marginRight: 4 }}></span>
                  亚健康: {healthData.statusDistribution.subHealthy}
                </div>
                <div>
                  <span style={{ display: 'inline-block', width: 12, height: 12, backgroundColor: '#f5222d', borderRadius: '50%', marginRight: 4 }}></span>
                  生病: {healthData.statusDistribution.sick}
                </div>
                <div>
                  <span style={{ display: 'inline-block', width: 12, height: 12, backgroundColor: '#8c8c8c', borderRadius: '50%', marginRight: 4 }}></span>
                  死亡: {healthData.statusDistribution.dead}
                </div>
              </div>
            </div>
          </Card>
        </Col>

        {/* 体重成长曲线 */}
        <Col span={24}>
          <Card title="体重成长曲线">
            <div style={{ marginBottom: 20, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div style={{ display: 'flex', gap: 20, alignItems: 'center' }}>
                <div>
                  <span style={{ marginRight: 8 }}>图表模式：</span>
                  <Group onChange={(e) => setChartMode(e.target.value)} value={chartMode}>
                    <RadioButton value="farm">农场平均</RadioButton>
                    <RadioButton value="individual">单个牲畜</RadioButton>
                  </Group>
                </div>
                {chartMode === 'individual' && (
                  <div style={{ display: 'flex', alignItems: 'center' }}>
                    <span style={{ marginRight: 8 }}>选择牲畜：</span>
                    <Select
                      placeholder="选择牲畜"
                      style={{ width: 200 }}
                      value={selectedLivestock}
                      onChange={(value) => {
                        setSelectedLivestock(value);
                        loadIndividualLivestockWeightData(value);
                      }}
                    >
                      {livestockList
                        .filter(item => {
                          if (selectedLivestockType === 'all') return true;
                          // 忽略大小写和空格进行比较
                          const itemType = item.livestockType ? item.livestockType.trim().toLowerCase() : '';
                          const targetType = selectedLivestockType ? selectedLivestockType.trim().toLowerCase() : '';
                          return itemType === targetType;
                        })
                        .map(livestock => (
                          <Option key={livestock.livestockId} value={livestock.livestockId}>
                            {livestock.livestockName} ({livestock.livestockCode})
                          </Option>
                        ))}
                    </Select>
                  </div>
                )}
              </div>
            </div>
            <div style={{ height: 400, position: 'relative' }}>
              {loading ? (
                <div style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)' }}>
                  <Spin tip="加载中..." />
                </div>
              ) : null}
              <div ref={weightCurveRef} style={{ width: '100%', height: '100%' }} />
            </div>
          </Card>
        </Col>
      </Row>
    </Content>
  );
}
