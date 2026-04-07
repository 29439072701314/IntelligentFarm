package com.example.intelligentfarmcore.config;

import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.entity.LivestockRecord;
import com.example.intelligentfarmcore.service.LivestockRecordService;
import com.example.intelligentfarmcore.service.LivestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private LivestockService livestockService;

    @Autowired
    private LivestockRecordService livestockRecordService;

    @Override
    public void run(String... args) throws Exception {
        // 生成模拟的出入库记录
        generateMockLivestockRecords();
    }

    private void generateMockLivestockRecords() {
        // 获取所有牲畜
        List<Livestock> livestockList = (List<Livestock>) livestockService.getAllLivestock().getData();
        
        if (livestockList.isEmpty()) {
            System.out.println("没有牲畜数据，无法生成出入库记录");
            return;
        }

        // 检查是否已有记录
        List<LivestockRecord> existingRecords = livestockRecordService.getAllRecords();
        if (!existingRecords.isEmpty()) {
            System.out.println("已有出入库记录，跳过模拟数据生成");
            return;
        }

        // 操作人列表
        String[] operators = {"管理员", "张三", "李四", "王五", "赵六"};
        
        // 入库备注列表
        String[] inStockRemarks = {
            "初始入库",
            "新购牲畜",
            "繁殖幼崽",
            "从其他农场调入",
            "检疫合格入库"
        };
        
        // 出库备注列表
        String[] outStockRemarks = {
            "销售出库",
            "屠宰出库",
            "转移到其他农场",
            "因病淘汰",
            "繁殖配种"
        };

        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        // 为每个牲畜生成1-3条记录
        for (Livestock livestock : livestockList) {
            int recordCount = random.nextInt(3) + 1;
            
            for (int i = 0; i < recordCount; i++) {
                LivestockRecord record = new LivestockRecord();
                record.setFarmId(livestock.getFarmId());
                record.setLivestockId(livestock.getLivestockId());
                
                // 第一条记录一定是入库
                if (i == 0) {
                    record.setOperationType(1); // 入库
                    record.setOperationTime(now.minusDays(random.nextInt(30)));
                    record.setOperator(operators[random.nextInt(operators.length)]);
                    record.setRemark(inStockRemarks[random.nextInt(inStockRemarks.length)]);
                } else {
                    // 后续记录随机选择入库或出库
                    int operationType = random.nextInt(2) + 1;
                    record.setOperationType(operationType);
                    record.setOperationTime(now.minusDays(random.nextInt(20)));
                    record.setOperator(operators[random.nextInt(operators.length)]);
                    
                    if (operationType == 1) {
                        record.setRemark(inStockRemarks[random.nextInt(inStockRemarks.length)]);
                    } else {
                        record.setRemark(outStockRemarks[random.nextInt(outStockRemarks.length)]);
                    }
                }
                
                livestockRecordService.addRecord(record);
            }
        }

        System.out.println("模拟出入库记录生成完成");
    }
}