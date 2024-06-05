package com.example.tddemo.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.taosdata.jdbc.TSDBDriver;

public class JNIConnectExample {
    public static void main(String[] args) throws SQLException {
        String jdbcUrl = "jdbc:TAOS://weiwei-ThinkStation-P920:6030?user=root&password=taosdata";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
        
        // create database

        Statement stmt = conn.createStatement();

        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS power");

        // use database
        stmt.executeUpdate("USE power");

        // create table
        stmt.executeUpdate("CREATE STABLE IF NOT EXISTS meters (ts TIMESTAMP, current FLOAT, voltage INT, phase FLOAT) TAGS (groupId INT, location BINARY(24))");

        // insert data
        String insertQuery = "INSERT INTO " +
                "power.d1001 USING power.meters TAGS(2,'California.SanFrancisco') " +
                "VALUES " +
                "(NOW + 1a, 10.30000, 219, 0.31000) " +
                "(NOW + 2a, 12.60000, 218, 0.33000) " +
                "(NOW + 3a, 12.30000, 221, 0.31000) " +
                "power.d1002 USING power.meters TAGS(3, 'California.SanFrancisco') " +
                "VALUES " +
                "(NOW + 1a, 10.30000, 218, 0.25000) ";
        int affectedRows = stmt.executeUpdate(insertQuery);
        System.out.println("insert " + affectedRows + " rows.");

        System.out.println("success");
        conn.close();
    }
}

// use
// String jdbcUrl = "jdbc:TAOS://localhost:6030/dbName?user=root&password=taosdata";
// if you want to connect a specified database named "dbName".