package com.vsrka.readingAndWriting;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
        String query ="INSERT INTO signals.devices4_4(device_name,signal_name,signal_number,interval_level,interval_number,characteristic_number,characteristic_value) VALUES(?,?,?,?,?,?,?)";
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

        String query = "SELECT characteristic_value FROM signals.devices1_4 WHERE device_name = ? AND signal_number = ? AND interval_level = ? AND characteristic_number = ? ORDER BY interval_number";
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

    public HashMap<Integer,List<Double>> getAllParameterV2(String deviceName, int signalNumber, int intervalLevel, int characteristicNumber1) throws Exception{
        String query = "SELECT characteristic_number,characteristic_value FROM signals.devices3_4 WHERE signal_number = ? AND interval_level = ?  ORDER BY characteristic_number,interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1,signalNumber);
        pstmt.setInt(2,intervalLevel);
        HashMap<Integer,List<Double>> result = new HashMap<>();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            int characteristicNumber = rs.getInt(1);
            double characteristicValue = rs.getDouble(2);
            List<Double> curr = result.getOrDefault(characteristicNumber,new ArrayList<>());
            curr.add(characteristicValue);
            result.put(characteristicNumber,curr);
        }
        return result;
    }

    public List<Double> getParameter(String deviceName, int signalNumber, int characteristicNumber) throws Exception{
        List<Double> result = new ArrayList<>();
        String query = "SELECT characteristic_value FROM signals.devices5 WHERE device_name = ? AND signal_number = ? AND characteristic_number = ? ORDER BY interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1,deviceName);
        pstmt.setInt(2,signalNumber);
        pstmt.setInt(3,characteristicNumber);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            result.add(rs.getDouble(1));
        }
        return result;
    }

    public void insertPearson(String deviceName,String signalName,int signalNumber,int intervalLevel,int characteristicNumber,double pearsomValue) throws Exception{
        String query = "INSERT INTO signals.devicesParameter2(device_name,signal_name,signal_number,interval_level,characteristic_number,pearson_value) VALUES(?,?,?,?,?,?)";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1,deviceName);
        pstmt.setString(2,signalName);
        pstmt.setInt(3,signalNumber);
        pstmt.setInt(4,intervalLevel);
        pstmt.setInt(5,characteristicNumber);
        pstmt.setDouble(6,pearsomValue);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void insertPearsonString() throws SQLException{
        String query ="INSERT INTO signals.o303_4_level(device_name,signal_name,signal_number,interval_level,characteristic_number,pearson_value) VALUES(?,?,?,?,?,?)";
        pstmt = conn.prepareStatement(query);
        conn.setAutoCommit(false);
    }

    public void insertPearsonDataBatch(String deviceName,String signalName,int signalNumber,int intervalLevel,int characteristicNumber,double pearsomValue) throws SQLException{


        try {
            pstmt.setString(1,deviceName);
            pstmt.setString(2,signalName);
            pstmt.setInt(3,signalNumber);
            pstmt.setInt(4,intervalLevel);
            pstmt.setInt(5,characteristicNumber);
            pstmt.setDouble(6,pearsomValue);
            pstmt.addBatch();
        }catch (SQLException e){
            e.printStackTrace();
        }


    }

    public List<Double> getParameterV2(String deviceName, int signalNumber, int characteristicNumber) throws Exception{
        List<Double> result = new ArrayList<>();
        String query = "SELECT characteristic_value FROM signals.devices6 WHERE signal_number = ? AND characteristic_number = ? ORDER BY interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1,signalNumber);
        pstmt.setInt(2,characteristicNumber);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            result.add(rs.getDouble(1));
        }
        return result;
    }

    //Взятие последних характеристик
    public HashMap<Integer,List<Double>> getParameterV3(String deviceName, int signalNumber) throws Exception{
        HashMap<Integer,List<Double>> result = new HashMap<>(86000);
        String query = "SELECT characteristic_number,characteristic_value FROM signals.devices4_4 WHERE signal_number=? order by characteristic_number , interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1,signalNumber);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            int characteristicNumber = rs.getInt(1);
            double characteristicValue = rs.getDouble(2);
            List<Double> curr = result.getOrDefault(characteristicNumber,new ArrayList<>());
            curr.add(characteristicValue);
            result.put(characteristicNumber,curr);
        }
        return result;
    }
    //Взятие третьих характеристик
    public HashMap<Integer,List<Double>> getParameterV4(String deviceName, int signalNumber) throws Exception{
        HashMap<Integer,List<Double>> result = new HashMap<>(5000);
        String query = "SELECT characteristic_number,characteristic_value,interval_number FROM signals.devices3_4 WHERE signal_number=? order by characteristic_number , interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1,signalNumber);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            int characteristicNumber = rs.getInt(1);
            double characteristicValue = rs.getDouble(2);
            int intervalNumber = rs.getInt(3);
            if(intervalNumber % 10==9){
                List<Double> curr = result.getOrDefault(characteristicNumber,new ArrayList<>());
                curr.add(characteristicValue);
                result.put(characteristicNumber,curr);
            }
        }
        return result;
    }

    //Взятие вторых характеристик
    public HashMap<Integer,List<Double>> getParameterV5(String deviceName, int signalNumber) throws Exception{
        HashMap<Integer,List<Double>> result = new HashMap<>(300);
        String query = "SELECT characteristic_number,characteristic_value,interval_number FROM signals.devices2_4 WHERE signal_number=? order by characteristic_number , interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1,signalNumber);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            int characteristicNumber = rs.getInt(1);
            double characteristicValue = rs.getDouble(2);
            int intervalNumber = rs.getInt(3);
            if(intervalNumber % 100==99){
                List<Double> curr = result.getOrDefault(characteristicNumber,new ArrayList<>());
                curr.add(characteristicValue);
                result.put(characteristicNumber,curr);
            }
        }
        return result;
    }

    //Взятие первых характеристик
    public HashMap<Integer,List<Double>> getParameterV6(String deviceName, int signalNumber) throws Exception{
        HashMap<Integer,List<Double>> result = new HashMap<>(300);
        String query = "SELECT characteristic_number,characteristic_value,interval_number FROM signals.devices1_4 WHERE signal_number=? order by characteristic_number , interval_number";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1,signalNumber);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            int characteristicNumber = rs.getInt(1);
            double characteristicValue = rs.getDouble(2);
            int intervalNumber = rs.getInt(3);
            if(intervalNumber % 1000==999){
                List<Double> curr = result.getOrDefault(characteristicNumber,new ArrayList<>());
                curr.add(characteristicValue);
                result.put(characteristicNumber,curr);
            }
        }
        return result;
    }
}
