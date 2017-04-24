package com.hdu.team.hiwanan.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by JerryYin on 4/11/17.
 */

public class DBHelper {
    public static final String url = "jdbc:mysql://127.0.0.1/wanan";
    public static final String drive = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String pwd = "wanan@123";
    static String name;

    public static void main(String[] args) {
        try {
            Class.forName(drive);
            Connection conn = DriverManager.getConnection(url, user, pwd);
            if (conn.isClosed()) {
                System.out.println("Connetciton closed!");
            }
            System.out.println("Succeeded connecting to the Database!");

            Statement statement = conn.createStatement();
            String sql = "select * from hiuser";
            ResultSet set = statement.executeQuery(sql);
            while (set.next()){
                name = set.getString("username");
                System.out.println("username : "+name);
            }
            set.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

}
