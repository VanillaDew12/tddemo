package com.example.tddemo.controller;

import com.example.tddemo.Utils.TDengineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.Map;

@RestController
@RequestMapping("/api/tdengine")
public class TDengineController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TDengineUtils tDengineUtils;

    @PostMapping("/insert")
    public String insertData(@RequestBody Map<String, Object> payload) {
        // 直接使用 TDengineUtils 来执行插入数据逻辑
        long ts = System.currentTimeMillis();
        float temperature = ((Number) payload.get("temperature")).floatValue();
        float humidity = ((Number) payload.get("humidity")).floatValue();
        return tDengineUtils.insertData(ts, temperature, humidity);
    }

    // 新增的端点，用于手动触发数据采集,采集一次
    @GetMapping("/collect")
    public String collectDataManually() {
        try {
            tDengineUtils.collectData(); // 手动调用 collectData 方法
            return "Data collected successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error collecting data: " + e.getMessage();
        }
    }

    //开始自动采集
    @GetMapping("/start")
    public String startCollecting() {
        tDengineUtils.startCollecting();
        return "Started data collection.";
    }

    // 停止自动数据采集
    @GetMapping("/stop")
    public String stopCollecting() {
        tDengineUtils.stopCollecting();
        return "Stopped data collection.";
    }

    // 检查数据采集状态
    @GetMapping("/status")
    public String getStatus() {
        boolean isCollecting = tDengineUtils.isCollecting();
        return "TDengine API is running. Data collection is " + (isCollecting ? "enabled" : "disabled") + ".";
    }
}
