package com.vsrka.readingAndWriting;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class workWithDatabase {

    final String url = "jdbc:mysql://localhost:3306/signals";
    final String user = "root";
    final String password = "1133Ov..";
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt=null;

    public workWithDatabase() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(url,user,password);
        stmt = conn.createStatement();
    }

    public void insertData(String deviceName,String signalName,int signalNumber,int intervalLevel,int intervalNumber,int characteristicNumber,double value) throws SQLException{

        String query ="INSERT INTO signals.devices(device_name,signal_name,signal_number,interval_level,interval_number,characteristic_number,characteristic_value) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1,deviceName);
        pstmt.setString(2,signalName);
        pstmt.setInt(3,signalNumber);
        pstmt.setInt(4,intervalLevel);
        pstmt.setInt(5,intervalNumber);
        pstmt.setInt(6,characteristicNumber);
        pstmt.setDouble(7,value);
        pstmt.executeUpdate();

    }

    //Пакетная вставка в базу данных с полной передачей что и куда вставить
    public void insertString() throws SQLException{
        String query ="INSERT INTO signals.devices2(device_name,signal_name,signal_number,interval_level,interval_number,characteristic_number,characteristic_value) VALUES(?,?,?,?,?,?,?)";
        pstmt = conn.prepareStatement(query);
        conn.setAutoCommit(false);
    }
    public void insertDataBatch(String deviceName,String signalName,int signalNumber,int intervalLevel,int intervalNumber,int characteristicNumber,double value) throws SQLException{


        try {
            pstmt.setString(1,deviceName);
            pstmt.setString(2,signalName);
            pstmt.setInt(3,signalNumber);
            pstmt.setInt(4,intervalLevel);
            pstmt.setInt(5,intervalNumber);
            pstmt.setInt(6,characteristicNumber);
            pstmt.setDouble(7,value);
            pstmt.addBatch();
        }catch (SQLException e){
            e.printStackTrace();
        }


    }
    public void insertBatch() throws SQLException{
        pstmt.executeBatch();
        conn.commit(); // Фиксация изменений
        conn.setAutoCommit(true);
    }

    public void close() throws SQLException{
        stmt.close();
        conn.close();
    }

    public List<Double> getAllParameter(String deviceName, int signalNumber, int intervalLevel, int characteristicNumber) throws Exception{

        String query = "SELECT characteristic_value FROM signals.devices1 WHERE device_name = ? AND signal_number = ? AND interval_level = ? AND characteristic_number = ? ORDER BY interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1,deviceName);
        pstmt.setInt(2,signalNumber);
        pstmt.setInt(3,intervalLevel);
        pstmt.setInt(4,characteristicNumber);

        List<Double> result = new ArrayList<Double>();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            result.add(rs.getDouble(1));
        }
        rs.close();
        return result;

    }
}
