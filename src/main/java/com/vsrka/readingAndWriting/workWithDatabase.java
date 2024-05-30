package com.vsrka.readingAndWriting;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class workWithDatabase {
    final String url = "jdbc:mysql://localhost:3306/signals";
    final String user = "root";
    final String password = "test";
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    public workWithDatabase() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, password);
        stmt = conn.createStatement();
    }

    public void insertBatch() throws SQLException {
        pstmt.executeBatch();
        conn.commit(); // Фиксация изменений
        conn.setAutoCommit(true);
    }

    public void close() throws SQLException {
        stmt.close();
        conn.close();
    }

    public void insertPearsonString() throws SQLException {
        String query = "INSERT INTO signals.devicesParameter_test(device_name,signal_name,signal_number,interval_level,characteristic_number,pearson_value) VALUES(?,?,?,?,?,?)";
        pstmt = conn.prepareStatement(query);
        conn.setAutoCommit(false);
    }

    public void insertPearsonDataBatch(String deviceName, String signalName, int signalNumber, int intervalLevel, int characteristicNumber, double pearsomValue) throws SQLException {
        try {
            pstmt.setString(1, deviceName);
            pstmt.setString(2, signalName);
            pstmt.setInt(3, signalNumber);
            pstmt.setInt(4, intervalLevel);
            pstmt.setInt(5, characteristicNumber);
            pstmt.setDouble(6, pearsomValue);
            pstmt.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
