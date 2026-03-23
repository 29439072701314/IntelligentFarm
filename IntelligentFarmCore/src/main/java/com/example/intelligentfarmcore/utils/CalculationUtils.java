package com.example.intelligentfarmcore.utils;

/**
 * 计算工具类，存放各种计算方法
 */
public class CalculationUtils {

    /**
     * 计算房间入住率
     * @param currentCount 当前人数
     * @param maxCapacity 最大容量
     * @return 入住率百分比（保留两位小数）
     */
    public static Double calculateOccupancyRate(Integer currentCount, Integer maxCapacity) {
        if (maxCapacity == null || maxCapacity <= 0) {
            return 0.0;
        }
        if (currentCount == null) {
            return 0.0;
        }
        double rate = (currentCount.doubleValue() / maxCapacity.doubleValue()) * 100;
        return Math.round(rate * 100.0) / 100.0;
    }
}
