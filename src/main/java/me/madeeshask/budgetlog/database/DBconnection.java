//@author MadeeshaSK

package me.madeeshask.budgetlog.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    
    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:src/main/java/me/madeeshask/budgetlog/database/BudgetLog.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        connect();
    }
    
}

