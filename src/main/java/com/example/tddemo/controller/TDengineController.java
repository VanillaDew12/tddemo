package com.example.tddemo.controller;

import com.example.tddemo.Utils.TDengineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
        long ts = System.currentTimeMillis();
        float temperature = ((Number) payload.get("temperature")).floatValue();
        float humidity = ((Number) payload.get("humidity")).floatValue();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // 插入数据到 TDengine
            String insertSQL = String.format(
                    "INSERT INTO d1 VALUES (%d, %.2f, %.2f)",
                    ts, temperature, humidity);
            stmt.executeUpdate(insertSQL);

            return String.format("Inserted data at %d - Temperature: %.2f, Humidity: %.2f",
                    ts, temperature, humidity);

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error inserting data: " + e.getMessage();
        }
    }

    // 新增的端点，用于手动触发数据采集
    @GetMapping("/collect")
    public String collectData() {
        try {
            tDengineUtils.collectData(); // 手动调用 collectData 方法
            return "Data collected successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error collecting data: " + e.getMessage();
        }
    }
}
