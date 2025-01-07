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
            String dbPath = appFolder + File.separator + "BudgetLog.db";
            File appDir = new File(appFolder);
            
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                try (InputStream is = DBconnection.class.getClassLoader().getResourceAsStream("BudgetLog.db");
                     OutputStream os = new FileOutputStream(dbFile)) {
                    if (is == null) {
                        throw new IOException("Could not find BudgetLog.db in resources");
                    }
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }
            }
            
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Connection to SQLite has been established at: " + dbPath);
            
        } catch (SQLException | IOException e) {
            System.out.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        connect();
    }
}