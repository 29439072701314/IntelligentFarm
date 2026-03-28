package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.FeedFormulaDao;
import com.example.intelligentfarmcore.dao.LivestockDao;
import com.example.intelligentfarmcore.dao.WarningDao;
import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.entity.Warning;
import com.example.intelligentfarmcore.pojo.dto.WarningDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarningService {

    @Autowired
    private WarningDao warningDao;

    @Autowired
    private FeedFormulaDao feedFormulaDao;

    @Autowired
    private LivestockDao livestockDao;

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public Page<Warning> getWarningList(String type, String status, Pageable pageable) {
        if (type != null && status != null) {
            return warningDao.findByTypeAndStatus(type, status, pageable);
        } else if (type != null) {
            return warningDao.findByType(type, pageable);
        } else if (status != null) {
            return warningDao.findByStatus(status, pageable);
        } else {
            return warningDao.findAll(pageable);
        }
    }

    public Map<String, Long> getStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        statistics.put("total", warningDao.countTotal());
        statistics.put("unprocessed", warningDao.countUnprocessed());
        statistics.put("livestockAbnormal", warningDao.countLivestockAbnormal());
        statistics.put("environmentAbnormal", warningDao.countEnvironmentAbnormal());
        return statistics;
    }

    public void handleWarning(Long id) {
        Warning warning = warningDao.findById(id).orElse(null);
        if (warning != null) {
            warning.setStatus("处理中");
            warning.setHandledAt(LocalDateTime.now());
            warningDao.save(warning);
        }
    }

    public void batchHandleWarning(List<Long> ids) {
        for (Long id : ids) {
            handleWarning(id);
        }
    }

    public void generateWarning(String type, String source, String details, String level) {
        // 检查是否存在未处理的告警
        long count = warningDao.countByTypeAndSourceAndStatus(type, source);
        if (count == 0) {
            // 检查是否存在处理中的告警
            List<Warning> existingWarnings = warningDao.findAll();
            boolean hasProcessingWarning = false;
            
            for (Warning warning : existingWarnings) {
                if (warning.getType().equals(type) && warning.getSource().equals(source) && 
                    warning.getStatus().equals("处理中")) {
                    hasProcessingWarning = true;
                    break;
                }
            }
            
            if (!hasProcessingWarning) {
                Warning warning = new Warning();
                warning.setType(type);
                warning.setSource(source);
                warning.setDetail(details);
                warning.setDetails(details);
                warning.setLevel(level);
                warning.setStatus("未处理");
                warning.setCreatedAt(LocalDateTime.now());
                warningDao.save(warning);
                
                // 推送新告警通知
                if (messagingTemplate != null) {
                    messagingTemplate.convertAndSend("/topic/warnings", new WarningDTO(warning));
                }
            }
        }
    }

    public void eliminateWarning(String type, String source) {
        List<Warning> warnings = warningDao.findAll();
        for (Warning warning : warnings) {
            if (warning.getType().equals(type) && warning.getSource().equals(source) && 
                (warning.getStatus().equals("未处理") || warning.getStatus().equals("处理中"))) {
                warning.setStatus("已解决");
                warning.setHandledAt(LocalDateTime.now());
                warningDao.save(warning);
            }
        }
    }

    public void eliminateLivestockWarning(String livestockId) {
        eliminateWarning("牲畜", livestockId);
    }

    public void eliminateEnvironmentWarning(String deviceId) {
        eliminateWarning("环境", deviceId);
    }

    public void eliminateFeedWarning(String formulaId) {
        eliminateWarning("饲料", formulaId);
    }

    // 扫描所有需要告警的情况
    public void scanForWarnings() {
        // 扫描饲料库存
        scanFeedInventory();
        // 扫描牲畜健康状态
        scanLivestockHealth();
    }

    // 扫描饲料库存
    private void scanFeedInventory() {
        List<FeedFormula> formulas = feedFormulaDao.findAll();
        for (FeedFormula formula : formulas) {
            if (formula.getStock() < formula.getThreshold()) {
                generateWarning("饲料", formula.getId().toString(), "配方" + formula.getName() + "库存低于阈值", "低");
            } else {
                eliminateFeedWarning(formula.getId().toString());
            }
        }
    }

    // 扫描牲畜健康状态
    private void scanLivestockHealth() {
        List<Livestock> livestocks = livestockDao.findAll();
        for (Livestock livestock : livestocks) {
            String healthStatus = livestock.getHealthStatus();
            String livestockCode = livestock.getLivestockCode();
            if (livestockCode == null || livestockCode.isEmpty()) {
                continue;
            }
            
            switch (healthStatus) {
                case "患病":
                    generateWarning("牲畜", livestockCode, "牲畜健康状态为患病", "高");
                    break;
                case "治疗中":
                    generateWarning("牲畜", livestockCode, "牲畜正在治疗中", "中");
                    break;
                case "亚健康":
                    generateWarning("牲畜", livestockCode, "牲畜处于亚健康状态", "中");
                    break;
                case "健康":
                    eliminateLivestockWarning(livestockCode);
                    break;
                default:
                    break;
            }
        }
    }
}