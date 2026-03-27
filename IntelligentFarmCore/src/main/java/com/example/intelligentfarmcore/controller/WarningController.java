package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.dto.WarningDTO;
import com.example.intelligentfarmcore.pojo.entity.Warning;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.WarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/warning")
public class WarningController {

    @Autowired
    private WarningService warningService;

    @GetMapping("/list")
    public ResponseMessage<Map<String, Object>> getWarningList(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Warning> warningPage = warningService.getWarningList(type, status, pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("total", warningPage.getTotalElements());
        result.put("list", warningPage.getContent().stream().map(WarningDTO::new).collect(Collectors.toList()));
        return ResponseMessage.success(result);
    }

    @GetMapping("/statistics")
    public ResponseMessage<Map<String, Long>> getStatistics() {
        return ResponseMessage.success(warningService.getStatistics());
    }

    @PutMapping("/{id}/handle")
    public ResponseMessage<String> handleWarning(@PathVariable Long id) {
        warningService.handleWarning(id);
        return ResponseMessage.success("处理成功");
    }

    @PutMapping("/batch-handle")
    public ResponseMessage<String> batchHandleWarning(@RequestBody List<Long> ids) {
        warningService.batchHandleWarning(ids);
        return ResponseMessage.success("批量处理成功");
    }

    @PostMapping("/scan")
    public ResponseMessage<String> scanForWarnings() {
        warningService.scanForWarnings();
        return ResponseMessage.success("扫描完成");
    }
}