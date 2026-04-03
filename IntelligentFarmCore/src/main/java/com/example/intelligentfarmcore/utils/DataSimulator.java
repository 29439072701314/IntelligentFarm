package com.example.intelligentfarmcore.utils;

import com.example.intelligentfarmcore.dao.DeviceDao;
import com.example.intelligentfarmcore.dao.EnvironmentSensorDao;
import com.example.intelligentfarmcore.pojo.entity.Device;
import com.example.intelligentfarmcore.pojo.entity.EnvironmentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DataSimulator {

    private static final Logger logger = LoggerFactory.getLogger(DataSimulator.class);

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private EnvironmentSensorDao environmentSensorDao;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new Random();

    // 启动数据模拟
    public void start() {
        System.out.println("Starting data simulator...");
        logger.info("Starting data simulator...");
        // 初始化设备数据
        initializeDevices();
        // 每5秒生成一次数据
        executorService.scheduleAtFixedRate(this::generateData, 0, 5, TimeUnit.SECONDS);
        System.out.println("Data simulator started successfully");
        logger.info("Data simulator started successfully");
        // 睡眠2秒，以便能够看到日志输出
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 初始化设备数据
    private void initializeDevices() {
        try {
            System.out.println("Initializing device data...");
            logger.info("Initializing device data...");
            // 检查是否有设备数据
            List<Device> devices = deviceDao.findAll();
            System.out.println("Found " + devices.size() + " devices");
            logger.info("Found {} devices", devices.size());
            if (devices.isEmpty()) {
                System.out.println("No devices found, creating sample devices...");
                logger.info("No devices found, creating sample devices...");
                // 创建示例设备
                Device device1 = new Device();
                device1.setDeviceName("farm001");
                device1.setFarmId(1L);
                device1.setTime(System.currentTimeMillis());
                device1.setTemperature(25);
                device1.setHumidity(60);
                device1.setGasConcentration(200);
                deviceDao.save(device1);
                System.out.println("Created device: farm001");
                logger.info("Created device: farm001");

                Device device2 = new Device();
                device2.setDeviceName("farm002");
                device2.setFarmId(2L);
                device2.setTime(System.currentTimeMillis());
                device2.setTemperature(26);
                device2.setHumidity(65);
                device2.setGasConcentration(220);
                deviceDao.save(device2);
                System.out.println("Created device: farm002");
                logger.info("Created device: farm002");

                Device device3 = new Device();
                device3.setDeviceName("farm003");
                device3.setFarmId(3L);
                device3.setTime(System.currentTimeMillis());
                device3.setTemperature(24);
                device3.setHumidity(55);
                device3.setGasConcentration(180);
                deviceDao.save(device3);
                System.out.println("Created device: farm003");
                logger.info("Created device: farm003");

                System.out.println("Sample devices created successfully");
                logger.info("Sample devices created successfully");
            } else {
                System.out.println("Found " + devices.size() + " existing devices, skipping initialization");
                logger.info("Found {} existing devices, skipping initialization", devices.size());
            }
        } catch (Exception e) {
            System.out.println("Error initializing device data: " + e.getMessage());
            e.printStackTrace();
            logger.error("Error initializing device data", e);
        }
    }

    // 生成模拟数据
    private void generateData() {
        try {
            System.out.println("Generating simulated data...");
            logger.info("Generating simulated data...");
            // 获取所有设备
            List<Device> devices = deviceDao.findAll();
            System.out.println("Found " + devices.size() + " devices");
            logger.info("Found {} devices", devices.size());

            for (Device device : devices) {
                System.out.println("Generating data for device: " + device.getDeviceName() + " (ID: " + device.getDeviceId() + ")");
                logger.info("Generating data for device: {} (ID: {})", device.getDeviceName(), device.getDeviceId());
                // 为每个设备生成符合实际的模拟数据
                Device simulatedDevice = generateDeviceData(device);

                // 更新设备的实时数据
                device.setTime(simulatedDevice.getTime());
                device.setTemperature(simulatedDevice.getTemperature());
                device.setHumidity(simulatedDevice.getHumidity());
                device.setGasConcentration(simulatedDevice.getGasConcentration());
                deviceDao.save(device);
                System.out.println("Updated device data: temperature=" + simulatedDevice.getTemperature() + ", humidity=" + simulatedDevice.getHumidity() + ", gasConcentration=" + simulatedDevice.getGasConcentration());
                logger.info("Updated device data: temperature={}, humidity={}, gasConcentration={}", 
                    simulatedDevice.getTemperature(), simulatedDevice.getHumidity(), simulatedDevice.getGasConcentration());

                // 保存环境数据历史记录
                EnvironmentData environmentData = new EnvironmentData();
                environmentData.setDeviceId(device.getDeviceId());
                environmentData.setDeviceName(device.getDeviceName());
                environmentData.setTime(simulatedDevice.getTime());
                environmentData.setTemperature(simulatedDevice.getTemperature());
                environmentData.setHumidity(simulatedDevice.getHumidity());
                environmentData.setGasConcentration(simulatedDevice.getGasConcentration());
                environmentSensorDao.save(environmentData);
                System.out.println("Saved environment data record");
                logger.info("Saved environment data record");
            }
            System.out.println("Data generation completed successfully");
            logger.info("Data generation completed successfully");
        } catch (Exception e) {
            System.out.println("Error generating simulated data: " + e.getMessage());
            e.printStackTrace();
            logger.error("Error generating simulated data", e);
        }
    }

    // 为特定设备生成模拟数据
    private Device generateDeviceData(Device device) {
        long currentTime = System.currentTimeMillis();

        // 基于设备ID和农场ID生成不同的基础值，确保不同设备的数据不同
        int baseValue = (int) (device.getDeviceId() + (device.getFarmId() != null ? device.getFarmId() : 0));

        // 生成符合实际的温度数据 (12-20度)
        int temperature = 16 + random.nextInt(9) - 4 + (baseValue % 3);
        if (temperature < 12) temperature = 12;
        if (temperature > 20) temperature = 20;

        // 生成符合实际的湿度数据 (40-80%)
        int humidity = 60 + random.nextInt(21) - 10 + (baseValue % 7);
        if (humidity < 40) humidity = 40;
        if (humidity > 80) humidity = 80;

        // 生成符合实际的气体浓度数据 (0-500 ppm)
        int gasConcentration = 200 + random.nextInt(101) - 50 + (baseValue % 10);
        if (gasConcentration < 0) gasConcentration = 0;
        if (gasConcentration > 500) gasConcentration = 500;

        return new Device(device.getDeviceName(), currentTime, temperature, humidity, gasConcentration);
    }

    // 停止数据模拟
    public void stop() {
        System.out.println("Stopping data simulator...");
        logger.info("Stopping data simulator...");
        executorService.shutdown();
        System.out.println("Data simulator stopped");
        logger.info("Data simulator stopped");
    }
}