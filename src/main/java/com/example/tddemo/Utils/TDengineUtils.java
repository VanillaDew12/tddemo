package com.example.tddemo.Utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


@Component
public class TDengineUtils {

    /**
     * author hyz
     */
    private final DataSource dataSource;

    private final Random random = new Random();

    private volatile boolean isCollecting = false; // 标志变量

    private final String baseSQL = "use testdb";

    public TDengineUtils(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String insertData(long ts, float temperature, float humidity) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(baseSQL);
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

    // 定时任务，每10秒插入一次数据
    @Scheduled(fixedRateString = "${app.data-collection-interval}")
    public void collectData() {
        if (!isCollecting) {
            return; // 如果不需要采集数据，直接返回
        }

        long ts = System.currentTimeMillis();
        float temperature = 20.0f + random.nextFloat() * 10.0f;
        float humidity = 30.0f + random.nextFloat() * 40.0f;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            //选择数据库和表
            stmt.executeUpdate(baseSQL);
            // 插入数据到 TDengine
            String insertSQL = String.format(
                    "INSERT INTO d1 VALUES (%d, %.2f, %.2f)",
                    ts, temperature, humidity);
            stmt.executeUpdate(insertSQL);

            // 打印插入信息
            System.out.printf("Inserted data at %s - Temperature: %.2f, Humidity: %.2f%n",
                    new Timestamp(ts), temperature, humidity);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startCollecting() {
        isCollecting = true; // 开启数据采集
    }

    public void stopCollecting() {
        isCollecting = false; // 关闭数据采集
    }

    public boolean isCollecting() {
        return isCollecting; // 返回当前数据采集状态
    }

    // 查询 d1 表中的数据
    public List<Map<String, Object>> queryData(Integer limit) {
        List<Map<String, Object>> result = new ArrayList<>();

        String querySQL = "SELECT * FROM testdb.d1";
        if (limit != null) {
            querySQL += " LIMIT " + limit;
        }

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("timestamp", rs.getTimestamp("ts"));
                row.put("temperature", rs.getFloat("temperature"));
                row.put("humidity", rs.getFloat("humidity"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 可以返回一个更详细的错误信息
        }

        return result;
    }
}