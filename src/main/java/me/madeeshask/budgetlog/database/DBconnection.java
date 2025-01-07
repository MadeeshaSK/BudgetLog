//@author MadeeshaSK
package me.madeeshask.budgetlog.database;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    
    public static Connection connect() {
        Connection conn = null;
        try {
            String userHome = System.getProperty("user.home");
            String appFolder = userHome + File.separator + ".budgetlog";
            File appDir = new File(appFolder);
            
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            
            String dbPath = DBconnection.class.getClassLoader().getResource("BudgetLog.db").getPath();
            
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Connection to SQLite has been established at: " + dbPath);
            
                        
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        connect();
    }
}