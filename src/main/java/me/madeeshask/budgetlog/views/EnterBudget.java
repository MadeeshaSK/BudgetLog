//@author MadeeshaSK

package me.madeeshask.budgetlog.views;
import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import me.madeeshask.budgetlog.utils.Utils;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import me.madeeshask.budgetlog.database.DBconnection;

public class EnterBudget extends javax.swing.JFrame {

    public int sheetId;
    public int userId;
    public int recordId;
    private boolean isUpdate;

    public EnterBudget(int userId, int sheetId) {
        initComponents();
        this.sheetId = sheetId;
        this.userId = userId;
        conScaleImage();
        conSetWindowProperties(); 
        consetLabelFont();
        conFacus();
        fetchSheetAndCategoryDetails();
        this.isUpdate = false;
        
        
    }
    
    public EnterBudget(int userId, int sheetId, int recordId) {
        initComponents();
        this.sheetId = sheetId;
        this.userId = userId;
        this.recordId = recordId;
        conScaleImage();
        conSetWindowProperties(); 
        consetLabelFont();
        conFacusLoad();
        fetchSheetAndCategoryDetails();
        loadDataForAddSheet(recordId);
        this.isUpdate = true;
        
    }
    
    // scale image in constructor
    public void conScaleImage() {
        // scale image 
        SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
                Utils.scaleImage("images/logo-with-background.png", labelLogo);
                Utils.scaleImage("images/logo-image-blur.png", labelPhoto);
                // add images here
            }
        });
    }
    
    // window properties in constructor
    public void conSetWindowProperties() {
        Utils.setWindowProperties(this, "Login", "icons/icon.png", false);
    }
    
    // label fonts in constructor
    public void consetLabelFont() {
        try {
            Utils.setLabelFont(labelHeading, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            Utils.setLabelFont(labelUnit, "fonts/Roboto-Bold.ttf", Font.BOLD, 18);
            Utils.setLabelFont(labelHeading3, "fonts/Roboto-Bold.ttf", Font.BOLD, 18);
            Utils.setLabelFont(labelHeading1, "fonts/Roboto-Bold.ttf", Font.BOLD, 18);
            Utils.setLabelFont(labelUnit, "fonts/Roboto-Bold.ttf", Font.BOLD, 18);
            
            // add label fonts here
        } catch (IOException ex) {
            Logger.getLogger(EnterBudget.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // add focusing in constructer
    public void conFacus() {
        SwingUtilities.invokeLater(() -> {
            incomec1.requestFocus();
        });
        expensec1.setText("  Enter Categories Amounts");
        expensec1.setForeground(java.awt.Color.decode("#A9A9A9"));
        expensec1.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        incomec1.addActionListener(e -> incomec2.requestFocus());
        incomec2.addActionListener(e -> incomec3.requestFocus());
        incomec3.addActionListener(e -> incomeco.requestFocus());
        incomeco.addActionListener(e -> expensec1.requestFocus());
        expensec1.addActionListener(e -> expensec2.requestFocus());
        expensec2.addActionListener(e -> expensec3.requestFocus());
        expensec3.addActionListener(e -> expenseco.requestFocus());
        expenseco.addActionListener(e -> dateSelect.requestFocus());
        expenseco.addActionListener(e -> saveButton());

    }
    
    // add focusing in constructer
    public void conFacusLoad() {
        SwingUtilities.invokeLater(() -> {
            incomec1.requestFocus();
        });
        
        incomec1.addActionListener(e -> incomec2.requestFocus());
        incomec2.addActionListener(e -> incomec3.requestFocus());
        incomec3.addActionListener(e -> incomeco.requestFocus());
        incomeco.addActionListener(e -> expensec1.requestFocus());
        expensec1.addActionListener(e -> expensec2.requestFocus());
        expensec2.addActionListener(e -> expensec3.requestFocus());
        expensec3.addActionListener(e -> expenseco.requestFocus());
        expenseco.addActionListener(e -> dateSelect.requestFocus());
        expenseco.addActionListener(e -> saveButton());

    }
    
    //clear button action
    public void clearButton() {
        conFacus();
        dateSelect.setSelectedIndex(0);
        incomec1.setText("");
        expensec1.setText("");
        incomec2.setText("");
        expensec2.setText("");
        incomec3.setText("");
        expensec3.setText("");
        incomeco.setText("");
        expenseco.setText("");
    }
    
    // savebutton action
    public void saveButton() {
        String date = (String) dateSelect.getSelectedItem();
        String incomeC1Text = incomec1.getText().trim();
        String incomeC2Text = incomec2.getText().trim();
        String incomeC3Text = incomec3.getText().trim();
        String incomeCoText = incomeco.getText().trim();
        String expenseC1Text = expensec1.getText().trim();
        String expenseC2Text = expensec2.getText().trim();
        String expenseC3Text = expensec3.getText().trim();
        String expenseCoText = expenseco.getText().trim();

        Double incomeC1 = parseDouble(incomeC1Text);
        Double incomeC2 = parseDouble(incomeC2Text);
        Double incomeC3 = parseDouble(incomeC3Text);
        Double incomeCo = parseDouble(incomeCoText);
        Double expenseC1 = parseDouble(expenseC1Text);
        Double expenseC2 = parseDouble(expenseC2Text);
        Double expenseC3 = parseDouble(expenseC3Text);
        Double expenseCo = parseDouble(expenseCoText);   
        
        if (date == null || "Date".equals(date)) {
            JOptionPane.showMessageDialog(null, "Please select a valid date.");
            return;
        }

        boolean hasNonZeroValue = false;
        Double[] values = {incomeC1, incomeC2, incomeC3, incomeCo, 
                          expenseC1, expenseC2, expenseC3, expenseCo};

        for (Double value : values) {
            if (value != null && value > 0) {
                hasNonZeroValue = true;
                break;
            }
        }

        if (!hasNonZeroValue) {
            JOptionPane.showMessageDialog(null, "At least one field must have a value greater than zero.");
            return;
        }

        for (Double value : values) {
            if (value != null) {
                if (value < 0 || value > 999999999) {
                    JOptionPane.showMessageDialog(null, "Values must be between 0 and 999,999,999.");
                    return;
                }
            }
        }

        try (Connection conn = DBconnection.connect()) {
            conn.setAutoCommit(false); 

            int userId = getUserIdBySheetId(conn, sheetId);
            
            String recordQuery = "INSERT INTO Record (date, user_id, sheet_id) VALUES (?, ?, ?)";
            int recordId;
            try (PreparedStatement recordStmt = conn.prepareStatement(recordQuery, Statement.RETURN_GENERATED_KEYS)) {
                recordStmt.setString(1, date);
                recordStmt.setInt(2, userId); 
                recordStmt.setInt(3, sheetId);
                recordStmt.executeUpdate();

                try (ResultSet generatedKeys = recordStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        recordId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve record ID.");
                    }
                }
            }
            
            insertExpend(conn, recordId, expenseC1, "Expense 1");
            insertExpend(conn, recordId, expenseC2, "Expense 2");
            insertExpend(conn, recordId, expenseC3, "Expense 3");
            insertExpend(conn, recordId, expenseCo, "Expense Other");

            insertIncome(conn, recordId, incomeC1, "Income 1");
            insertIncome(conn, recordId, incomeC2, "Income 2");
            insertIncome(conn, recordId, incomeC3, "Income 3");
            insertIncome(conn, recordId, incomeCo, "Income Other");

            conn.commit(); 

            ViewSheet D1 = new ViewSheet(userId, sheetId);
            D1.setVisible(true);
            this.dispose();
            JOptionPane.showMessageDialog(null, "Data saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
        }
    }

    private void insertExpend(Connection conn, int recordId, Double amount, String name) {
        if (amount != null && amount >= 0) {
            String query = "INSERT INTO Expense (expense_name, amount, record_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setDouble(2, amount == null ? 0 : amount);
                stmt.setInt(3, recordId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error inserting expense '" + name + "': " + e.getMessage());
            }
        }
    }

    private void insertIncome(Connection conn, int recordId, Double amount, String name) {
        if (amount != null && amount >= 0) {
            String query = "INSERT INTO Income (income_name, amount, record_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setDouble(2, amount == null ? 0 : amount);
                stmt.setInt(3, recordId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error inserting income '" + name + "': " + e.getMessage());
            }
        }
    }

    private Double parseDouble(String text) {
        try {
            return text.isEmpty() ? 0.0 : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + text);
            return 0.0; 
        }
    }

    
    // update button action
    public void updateButton(int recordId) {
        String date = (String) dateSelect.getSelectedItem();
        String incomeC1Text = incomec1.getText().trim();
        String incomeC2Text = incomec2.getText().trim();
        String incomeC3Text = incomec3.getText().trim();
        String incomeCoText = incomeco.getText().trim();
        String expenseC1Text = expensec1.getText().trim();
        String expenseC2Text = expensec2.getText().trim();
        String expenseC3Text = expensec3.getText().trim();
        String expenseC0Text = expenseco.getText().trim();

        Double incomeC1 = parseDouble(incomeC1Text);
        Double incomeC2 = parseDouble(incomeC2Text);
        Double incomeC3 = parseDouble(incomeC3Text);
        Double incomeCo = parseDouble(incomeCoText);
        Double expenseC1 = parseDouble(expenseC1Text);
        Double expenseC2 = parseDouble(expenseC2Text);
        Double expenseC3 = parseDouble(expenseC3Text);
        Double expenseCo = parseDouble(expenseC0Text);

        if (date == null || "Date".equals(date)) {
            JOptionPane.showMessageDialog(null, "Please select a valid date.");
            return;
        }

        boolean hasNonZeroValue = false;
        Double[] values = {incomeC1, incomeC2, incomeC3, incomeCo, 
                          expenseC1, expenseC2, expenseC3, expenseCo};

        for (Double value : values) {
            if (value != null && value > 0) {
                hasNonZeroValue = true;
                break;
            }
        }

        if (!hasNonZeroValue) {
            JOptionPane.showMessageDialog(null, "At least one field must have a value greater than zero.");
            return;
        }

        for (Double value : values) {
            if (value != null) {
                if (value < 0 || value > 999999999) {
                    JOptionPane.showMessageDialog(null, "Values must be between 0 and 999,999,999.");
                    return;
                }
            }
        }

        try (Connection conn = DBconnection.connect()) {
            conn.setAutoCommit(false);

            String recordUpdateQuery = "UPDATE Record SET date = ? WHERE record_id = ?";
            try (PreparedStatement recordStmt = conn.prepareStatement(recordUpdateQuery)) {
                recordStmt.setString(1, date);
                recordStmt.setInt(2, recordId);
                recordStmt.executeUpdate();
            }

            updateExpend(conn, recordId, expenseC1, "Expense 1");
            updateExpend(conn, recordId, expenseC2, "Expense 2");
            updateExpend(conn, recordId, expenseC3, "Expense 3");
            updateExpend(conn, recordId, expenseCo, "Expense Other");

            updateIncome(conn, recordId, incomeC1, "Income 1");
            updateIncome(conn, recordId, incomeC2, "Income 2");
            updateIncome(conn, recordId, incomeC3, "Income 3");
            updateIncome(conn, recordId, incomeCo, "Income Other");

            conn.commit();

            ViewSheet D1 = new ViewSheet(getUserIdByRecordId(conn, recordId), sheetId);  
            D1.setVisible(true);
            this.dispose();
            JOptionPane.showMessageDialog(null, "Data updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating data: " + e.getMessage());
        }
    }

    private void updateExpend(Connection conn, int recordId, Double amount, String name) {
        if (amount != null && amount >= 0) {
            String query = "UPDATE Expense SET amount = ? WHERE record_id = ? AND expense_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, amount != null ? amount : 0);
                stmt.setInt(2, recordId);
                stmt.setString(3, name);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error updating expense '" + name + "': " + e.getMessage());
            }
        }
    }

    private void updateIncome(Connection conn, int recordId, Double amount, String name) {
        if (amount != null && amount >= 0) {
            String query = "UPDATE Income SET amount = ? WHERE record_id = ? AND income_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, amount != null ? amount : 0);
                stmt.setInt(2, recordId);
                stmt.setString(3, name);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error updating income '" + name + "': " + e.getMessage());
            }
        }
    }
    
    // Method to get user_id by record_id
    private int getUserIdByRecordId(Connection conn, int recordId) throws SQLException {
        String query = "SELECT user_id FROM Record WHERE record_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recordId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                } else {
                    throw new SQLException("Record not found with record_id: " + recordId);
                }
            }
        }
    }
    
    // get userid by sheetid
    private int getUserIdBySheetId(Connection conn, int sheetId) throws SQLException {
        String query = "SELECT user_id FROM Sheet WHERE sheet_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sheetId); 
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id"); 
                } else {
                    throw new SQLException("No user found for sheet_id " + sheetId);
                }
            }
        }
    }

    //view button
    public void viewButton() {
        ViewSheet D1 = new ViewSheet(userId,sheetId);
        D1.setVisible(true);
        this.dispose();
    }
    
    // fetchSheetAndCategoryDetails
    private void fetchSheetAndCategoryDetails() {
        try (Connection connection = DBconnection.connect()) {
            
            String sheetQuery = "SELECT sheet_name, unit FROM Sheet WHERE sheet_id = ?";
            try (PreparedStatement sheetStmt = connection.prepareStatement(sheetQuery)) {
                sheetStmt.setInt(1, sheetId);

                try (ResultSet sheetRs = sheetStmt.executeQuery()) {
                    if (sheetRs.next()) {
                        String sheetName = sheetRs.getString("sheet_name");
                        String unit = sheetRs.getString("unit");
                        labelSheetname.setText(sheetName);
                        labelUnit.setText(unit);
                    }
                }
            }
            
            fetchCategoryNames();
        
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
        }
    }

    // fetchCategoryNames
    private void fetchCategoryNames() {
        try (Connection connection = DBconnection.connect()) {
            String categoryQuery = "SELECT category_name, type FROM Category WHERE sheet_id = ?";

            try (PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery)) {
                categoryStmt.setInt(1, sheetId);

                try (ResultSet categoryRs = categoryStmt.executeQuery()) {
                    int incomeCount = 0;
                    int expenseCount = 0;

                    while (categoryRs.next()) {
                        String categoryName = categoryRs.getString("category_name");
                        String type = categoryRs.getString("type");

                        if ("Income".equalsIgnoreCase(type)) {
                            switch (incomeCount) {
                                case 0:
                                    labelic1.setText(categoryName);
                                    break;
                                case 1:
                                    labelic2.setText(categoryName);
                                    break;
                                case 2:
                                    labelic3.setText(categoryName);
                                    break;
                                case 3:
                                    labelico.setText(categoryName);
                                    break;
                                default:
                                    break; 
                            }
                            incomeCount++;
                        } else if ("Expense".equalsIgnoreCase(type)) {
                            switch (expenseCount) {
                                case 0:
                                    labelec1.setText(categoryName);
                                    break;
                                case 1:
                                    labelec2.setText(categoryName);
                                    break;
                                case 2:
                                    labelec3.setText(categoryName);
                                    break;
                                case 3:
                                    labeleco.setText(categoryName);
                                    break;
                                default:
                                    break; 
                            }
                            expenseCount++;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching category names: " + e.getMessage());
        }
    }
    
    // Method to load data for adding a sheet
    public void loadDataForAddSheet(final int recordId) {

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                Map<String, Object> results = new HashMap<>();
                Connection conn = null;

                try {
                    conn = DBconnection.connect();
                    
                    conn.setAutoCommit(false);

                    String date = loadDate(conn, recordId);
                    results.put("date", date);
                    
                    Map<String, Double> incomeData = loadIncomeData(conn, recordId);
                    results.put("income", incomeData);
                    
                    Map<String, Double> expenseData = loadExpenseData(conn, recordId);
                    results.put("expense", expenseData);

                    conn.commit();
                    return results;
                } catch (SQLException e) {
                    if (conn != null) {
                        try {
                            conn.rollback();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    throw e;
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> results = get();

                    String date = (String) results.get("date");
                    if (date != null) {
                        dateSelect.setSelectedItem(date);
                    }

                    Map<String, Double> incomeData = (Map<String, Double>) results.get("income");
                    if (incomeData != null) {
                        incomec1.setText(String.valueOf(incomeData.getOrDefault("Income 1", 0.0)));
                        incomec2.setText(String.valueOf(incomeData.getOrDefault("Income 2", 0.0)));
                        incomec3.setText(String.valueOf(incomeData.getOrDefault("Income 3", 0.0)));
                        incomeco.setText(String.valueOf(incomeData.getOrDefault("Income Other", 0.0)));
                    }

                    Map<String, Double> expenseData = (Map<String, Double>) results.get("expense");
                    if (expenseData != null) {
                        expensec1.setText(String.valueOf(expenseData.getOrDefault("Expense 1", 0.0)));
                        expensec2.setText(String.valueOf(expenseData.getOrDefault("Expense 2", 0.0)));
                        expensec3.setText(String.valueOf(expenseData.getOrDefault("Expense 3", 0.0)));
                        expenseco.setText(String.valueOf(expenseData.getOrDefault("Expense Other", 0.0)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                        "Error loading data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    // Helper method to load date
    private String loadDate(Connection conn, int recordId) throws SQLException {
        String date = null;
        String recordQuery = "SELECT date FROM Record WHERE record_id = ?";
        try (PreparedStatement recordStmt = conn.prepareStatement(recordQuery)) {
            recordStmt.setInt(1, recordId);
            try (ResultSet rs = recordStmt.executeQuery()) {
                if (rs.next()) {
                    date = rs.getString("date");
                }
            }
        }
        return date;
    }

    // Helper method to load income data
    private Map<String, Double> loadIncomeData(Connection conn, int recordId) throws SQLException {
        Map<String, Double> incomeData = new HashMap<>();
        String incomeQuery = "SELECT income_name, amount FROM Income WHERE record_id = ?";
        try (PreparedStatement incomeStmt = conn.prepareStatement(incomeQuery)) {
            incomeStmt.setInt(1, recordId);
            try (ResultSet rs = incomeStmt.executeQuery()) {
                while (rs.next()) {
                    incomeData.put(rs.getString("income_name"), rs.getDouble("amount"));
                }
            }
        }
        return incomeData;
    }

    // Helper method to load expense data
    private Map<String, Double> loadExpenseData(Connection conn, int recordId) throws SQLException {
        Map<String, Double> expenseData = new HashMap<>();
        String expenseQuery = "SELECT expense_name, amount FROM Expense WHERE record_id = ?";
        try (PreparedStatement expenseStmt = conn.prepareStatement(expenseQuery)) {
            expenseStmt.setInt(1, recordId);
            try (ResultSet rs = expenseStmt.executeQuery()) {
                while (rs.next()) {
                    expenseData.put(rs.getString("expense_name"), rs.getDouble("amount"));
                }
            }
        }
        return expenseData;
    }
    
    // Method to delete 
    private void deleteRecordAndRelatedData(int recordId) {
        Connection conn = null;
        try {
            conn = DBconnection.connect();
            conn.setAutoCommit(false);
            
            String deleteIncomeQuery = "DELETE FROM Income WHERE record_id = ?";
            try (PreparedStatement deleteIncomeStmt = conn.prepareStatement(deleteIncomeQuery)) {
                deleteIncomeStmt.setInt(1, recordId);
                deleteIncomeStmt.executeUpdate();
            }
            
            String deleteExpenseQuery = "DELETE FROM Expense WHERE record_id = ?";
            try (PreparedStatement deleteExpenseStmt = conn.prepareStatement(deleteExpenseQuery)) {
                deleteExpenseStmt.setInt(1, recordId);
                deleteExpenseStmt.executeUpdate();
            }
            
            String deleteRecordQuery = "DELETE FROM Record WHERE record_id = ?";
            try (PreparedStatement deleteRecordStmt = conn.prepareStatement(deleteRecordQuery)) {
                deleteRecordStmt.setInt(1, recordId);
                deleteRecordStmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting record: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        addNewSheetButton = new javax.swing.JButton();
        enterBudgetButton = new javax.swing.JButton();
        viewSummaryButton = new javax.swing.JButton();
        labelPhoto = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        labelic2 = new javax.swing.JLabel();
        incomec2 = new javax.swing.JTextField();
        expensec2 = new javax.swing.JTextField();
        labelec2 = new javax.swing.JLabel();
        clearButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        labelic3 = new javax.swing.JLabel();
        incomec3 = new javax.swing.JTextField();
        expensec3 = new javax.swing.JTextField();
        labelec3 = new javax.swing.JLabel();
        labelico = new javax.swing.JLabel();
        incomeco = new javax.swing.JTextField();
        expenseco = new javax.swing.JTextField();
        labeleco = new javax.swing.JLabel();
        incomec1 = new javax.swing.JTextField();
        expensec1 = new javax.swing.JTextField();
        labelec1 = new javax.swing.JLabel();
        labelic1 = new javax.swing.JLabel();
        labelHeading1 = new javax.swing.JLabel();
        labelUnit = new javax.swing.JLabel();
        labelHeading3 = new javax.swing.JLabel();
        dateSelect = new javax.swing.JComboBox<>();
        saveButton1 = new javax.swing.JButton();
        labelSheetname = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        labelHeading = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(6, 26, 45));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(6, 26, 45));
        jPanel2.setPreferredSize(new java.awt.Dimension(350, 500));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addNewSheetButton.setBackground(new java.awt.Color(7, 23, 90));
        addNewSheetButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        addNewSheetButton.setForeground(new java.awt.Color(255, 255, 255));
        addNewSheetButton.setText("Add New Sheet");
        addNewSheetButton.setBorder(null);
        addNewSheetButton.setOpaque(true);
        addNewSheetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewSheetButtonActionPerformed(evt);
            }
        });
        jPanel8.add(addNewSheetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 210, 60));

        enterBudgetButton.setBackground(new java.awt.Color(7, 23, 90));
        enterBudgetButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        enterBudgetButton.setForeground(new java.awt.Color(255, 255, 255));
        enterBudgetButton.setText("Enter Budget");
        enterBudgetButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        enterBudgetButton.setOpaque(true);
        enterBudgetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterBudgetButtonActionPerformed(evt);
            }
        });
        jPanel8.add(enterBudgetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 210, 60));

        viewSummaryButton.setBackground(new java.awt.Color(7, 23, 90));
        viewSummaryButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        viewSummaryButton.setForeground(new java.awt.Color(255, 255, 255));
        viewSummaryButton.setText("View Summary");
        viewSummaryButton.setOpaque(true);
        viewSummaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSummaryButtonActionPerformed(evt);
            }
        });
        jPanel8.add(viewSummaryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 210, 60));
        jPanel8.add(labelPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 470));

        jPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 470));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, -1, 490));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 850, 60));

        jPanel4.setBackground(new java.awt.Color(6, 26, 45));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(6, 26, 45));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelic2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelic2.setForeground(new java.awt.Color(255, 255, 255));
        labelic2.setText("Income category 2");
        jPanel9.add(labelic2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        incomec2.setBackground(new java.awt.Color(255, 255, 255));
        incomec2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        incomec2.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(incomec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 180, 40));

        expensec2.setBackground(new java.awt.Color(255, 255, 255));
        expensec2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expensec2.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(expensec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 180, 40));

        labelec2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelec2.setForeground(new java.awt.Color(255, 255, 255));
        labelec2.setText("Expense category 2");
        jPanel9.add(labelec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, -1, -1));

        clearButton.setBackground(new java.awt.Color(7, 23, 90));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        clearButton.setForeground(new java.awt.Color(255, 255, 255));
        clearButton.setText("Clear");
        clearButton.setAlignmentY(1.0F);
        clearButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        jPanel9.add(clearButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 410, 100, 40));

        viewButton.setBackground(new java.awt.Color(7, 23, 90));
        viewButton.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        viewButton.setForeground(new java.awt.Color(255, 255, 255));
        viewButton.setText("View");
        viewButton.setAlignmentY(1.0F);
        viewButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });
        jPanel9.add(viewButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 110, 40));

        deleteButton.setBackground(new java.awt.Color(7, 23, 90));
        deleteButton.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("Delete");
        deleteButton.setAlignmentY(1.0F);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel9.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 410, 100, 40));

        labelic3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelic3.setForeground(new java.awt.Color(255, 255, 255));
        labelic3.setText("Income category 3");
        jPanel9.add(labelic3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        incomec3.setBackground(new java.awt.Color(255, 255, 255));
        incomec3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        incomec3.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(incomec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 180, 40));

        expensec3.setBackground(new java.awt.Color(255, 255, 255));
        expensec3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expensec3.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(expensec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 180, 40));

        labelec3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelec3.setForeground(new java.awt.Color(255, 255, 255));
        labelec3.setText("Expense category 2");
        jPanel9.add(labelec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, -1, -1));

        labelico.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelico.setForeground(new java.awt.Color(255, 255, 255));
        labelico.setText("Other Incomes");
        jPanel9.add(labelico, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        incomeco.setBackground(new java.awt.Color(255, 255, 255));
        incomeco.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        incomeco.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(incomeco, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 180, 40));

        expenseco.setBackground(new java.awt.Color(255, 255, 255));
        expenseco.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expenseco.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(expenseco, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 180, 40));

        labeleco.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labeleco.setForeground(new java.awt.Color(255, 255, 255));
        labeleco.setText("Other Expenses");
        jPanel9.add(labeleco, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, -1, -1));

        incomec1.setBackground(new java.awt.Color(255, 255, 255));
        incomec1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        incomec1.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(incomec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 180, 40));

        expensec1.setBackground(new java.awt.Color(255, 255, 255));
        expensec1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expensec1.setForeground(new java.awt.Color(51, 51, 51));
        expensec1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                expensec1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                expensec1FocusLost(evt);
            }
        });
        jPanel9.add(expensec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, 180, 40));

        labelec1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelec1.setForeground(new java.awt.Color(255, 255, 255));
        labelec1.setText("Expense category 1");
        jPanel9.add(labelec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, -1, -1));

        labelic1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelic1.setForeground(new java.awt.Color(255, 255, 255));
        labelic1.setText("Income category 1");
        jPanel9.add(labelic1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, -1));

        labelHeading1.setBackground(new java.awt.Color(6, 26, 45));
        labelHeading1.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        labelHeading1.setForeground(new java.awt.Color(255, 0, 0));
        labelHeading1.setText("Expenses");
        jPanel9.add(labelHeading1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, 40));

        labelUnit.setBackground(new java.awt.Color(6, 26, 45));
        labelUnit.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        labelUnit.setForeground(new java.awt.Color(255, 255, 255));
        labelUnit.setText("Unit");
        jPanel9.add(labelUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 50, 40));

        labelHeading3.setBackground(new java.awt.Color(6, 26, 45));
        labelHeading3.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        labelHeading3.setForeground(new java.awt.Color(54, 205, 29));
        labelHeading3.setText("Incomes");
        labelHeading3.setOpaque(true);
        jPanel9.add(labelHeading3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, 40));

        dateSelect.setBackground(new java.awt.Color(255, 255, 255));
        dateSelect.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateSelect.setForeground(new java.awt.Color(51, 51, 51));
        dateSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jPanel9.add(dateSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 100, 40));

        saveButton1.setBackground(new java.awt.Color(7, 23, 90));
        saveButton1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        saveButton1.setForeground(new java.awt.Color(255, 255, 255));
        saveButton1.setText("Save");
        saveButton1.setAlignmentY(1.0F);
        saveButton1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        saveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButton1ActionPerformed(evt);
            }
        });
        jPanel9.add(saveButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 410, 100, 40));

        labelSheetname.setBackground(new java.awt.Color(6, 26, 45));
        labelSheetname.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        labelSheetname.setForeground(new java.awt.Color(255, 255, 255));
        labelSheetname.setText("Sheet Name");
        labelSheetname.setOpaque(true);
        jPanel9.add(labelSheetname, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 210, 40));

        jPanel4.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 470, 470));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 100, 490, 480));

        jPanel5.setBackground(new java.awt.Color(6, 26, 45));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 80));

        jPanel7.setBackground(new java.awt.Color(6, 26, 45));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelHeading.setBackground(new java.awt.Color(6, 26, 45));
        labelHeading.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelHeading.setForeground(new java.awt.Color(255, 255, 255));
        labelHeading.setText("Enter Data");
        labelHeading.setOpaque(true);
        jPanel7.add(labelHeading, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 270, 80));

        backButton.setBackground(new java.awt.Color(7, 23, 90));
        backButton.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("Back");
        backButton.setAlignmentY(1.0F);
        backButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        jPanel7.add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 110, 40));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 470, 80));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 100));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 640));

        setSize(new java.awt.Dimension(850, 620));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addNewSheetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewSheetButtonActionPerformed
        Connection conn = DBconnection.connect();
        int userId;
        try {
            userId = getUserIdBySheetId(conn, sheetId);
            AddNewSheet D1 = new AddNewSheet(userId, sheetId);
            D1.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(EnterBudget.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addNewSheetButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        Connection conn = DBconnection.connect();
        int userId;
        try {
            userId = getUserIdBySheetId(conn, sheetId);
            Dashboard D1 = new Dashboard(userId, sheetId);
            D1.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(EnterBudget.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_backButtonActionPerformed

    private void enterBudgetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterBudgetButtonActionPerformed
        EnterBudget D1 = new EnterBudget(userId,sheetId);
        D1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_enterBudgetButtonActionPerformed

    private void viewSummaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSummaryButtonActionPerformed
        ViewSummary D1 = new ViewSummary(userId, sheetId);
        D1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_viewSummaryButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearButton();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        viewButton();
    }//GEN-LAST:event_viewButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int choice = JOptionPane.showOptionDialog(
            null,
            "Do you want to delete the working Record?",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[] { "Yes", "No" },
            "No"
        );

        if (choice == JOptionPane.YES_OPTION) {
            deleteRecordAndRelatedData(recordId);
            ViewSheet D1 = new ViewSheet(userId, sheetId);
            D1.setVisible(true);
            this.dispose();
            JOptionPane.showMessageDialog(null, "The working Record has been successfully deleted.");
        } else if (choice == JOptionPane.NO_OPTION) {
            //
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void saveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButton1ActionPerformed
        if (isUpdate) {
            updateButton(recordId);  
        } else {
            saveButton();  
        }
    }//GEN-LAST:event_saveButton1ActionPerformed
    
    private void expensec1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_expensec1FocusGained
        if ("  Enter Categories Amounts".equals(expensec1.getText())) {
            expensec1.setText("");
            expensec1.setForeground(java.awt.Color.decode("#333333"));
            expensec1.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        }
    }//GEN-LAST:event_expensec1FocusGained

    private void expensec1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_expensec1FocusLost
        if ("".equals(expensec1.getText())) {
            expensec1.setText("  Enter Categories Amounts");
            expensec1.setForeground(java.awt.Color.decode("#A9A9A9"));
            expensec1.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        }
    }//GEN-LAST:event_expensec1FocusLost

    
    public static void main(String args[]) {
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EnterBudget.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EnterBudget.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EnterBudget.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EnterBudget.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EnterBudget(0,0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewSheetButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JComboBox<String> dateSelect;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton enterBudgetButton;
    private javax.swing.JTextField expensec1;
    private javax.swing.JTextField expensec2;
    private javax.swing.JTextField expensec3;
    private javax.swing.JTextField expenseco;
    private javax.swing.JTextField incomec1;
    private javax.swing.JTextField incomec2;
    private javax.swing.JTextField incomec3;
    private javax.swing.JTextField incomeco;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel labelHeading;
    private javax.swing.JLabel labelHeading1;
    private javax.swing.JLabel labelHeading3;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelPhoto;
    private javax.swing.JLabel labelSheetname;
    private javax.swing.JLabel labelUnit;
    private javax.swing.JLabel labelec1;
    private javax.swing.JLabel labelec2;
    private javax.swing.JLabel labelec3;
    private javax.swing.JLabel labeleco;
    private javax.swing.JLabel labelic1;
    private javax.swing.JLabel labelic2;
    private javax.swing.JLabel labelic3;
    private javax.swing.JLabel labelico;
    private javax.swing.JButton saveButton1;
    private javax.swing.JButton viewButton;
    private javax.swing.JButton viewSummaryButton;
    // End of variables declaration//GEN-END:variables
}
