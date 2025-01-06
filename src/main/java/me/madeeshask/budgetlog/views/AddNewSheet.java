//@author MadeeshaSK

package me.madeeshask.budgetlog.views;

import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import me.madeeshask.budgetlog.utils.Utils;
import javax.swing.SwingUtilities;
import me.madeeshask.budgetlog.database.DBconnection;
public class AddNewSheet extends javax.swing.JFrame {

    public int userId;
    public int sheetId;
    public int view;
    private boolean isUpdate;

    public AddNewSheet(int userId, int sheetId) {
        initComponents();
        this.userId = userId;
        this.sheetId = sheetId;
        conScaleImage();
        conSetWindowProperties(); 
        consetLabelFont();
        conFacus();
        this.isUpdate = false;
        
    }
    
    public AddNewSheet(int userId, int sheetId, int view) {
        initComponents();
        this.userId = userId;
        this.sheetId = sheetId;
        loadData(sheetId);
        conScaleImage();
        conSetWindowProperties(); 
        consetLabelFont();
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
            // add label fonts here
        } catch (IOException ex) {
            Logger.getLogger(AddNewSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // add focusing in constructer
    public void conFacus() {
    
        sheetName.setText("  Select a year and month first");
        sheetName.setForeground(java.awt.Color.decode("#A9A9A9"));
        sheetName.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        incomec1.setText("  Enter Categories Names");
        incomec1.setForeground(java.awt.Color.decode("#A9A9A9"));
        incomec1.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        yearSelect.addActionListener(e -> monthSelect.requestFocus());
        monthSelect.addActionListener(e -> sheetName.requestFocus());
        sheetName.addActionListener(e -> unitSelect.requestFocus());
        unitSelect.addActionListener(e -> incomec1.requestFocus());
        incomec1.addActionListener(e -> incomec2.requestFocus());
        incomec2.addActionListener(e -> incomec3.requestFocus());
        incomec3.addActionListener(e -> expensec1.requestFocus());
        expensec1.addActionListener(e -> expensec2.requestFocus());
        expensec2.addActionListener(e -> expensec3.requestFocus());
        expensec3.addActionListener(e -> saveButton());

    }
    // add focusing in constructer for load
    public void conFacusLoad() {
        
        sheetName.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        incomec1.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        yearSelect.addActionListener(e -> monthSelect.requestFocus());
        monthSelect.addActionListener(e -> sheetName.requestFocus());
        sheetName.addActionListener(e -> unitSelect.requestFocus());
        unitSelect.addActionListener(e -> incomec1.requestFocus());
        incomec1.addActionListener(e -> incomec2.requestFocus());
        incomec2.addActionListener(e -> incomec3.requestFocus());
        incomec3.addActionListener(e -> expensec1.requestFocus());
        expensec1.addActionListener(e -> expensec2.requestFocus());
        expensec2.addActionListener(e -> expensec3.requestFocus());
        expensec3.addActionListener(e -> saveButton());

    }
    
    // delete button action
    public void deleteButton() {
        int choice = JOptionPane.showOptionDialog(
            null,
            "Do you want to delete the working sheet?",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[] { "Yes", "No" },
            "No"
        );

        if (choice == JOptionPane.YES_OPTION) {
            Dashboard D1 = new Dashboard(userId,sheetId);
            D1.setVisible(true);
            this.dispose();
            JOptionPane.showMessageDialog(null, "The working sheet has been successfully deleted.");
        } else if (choice == JOptionPane.NO_OPTION) {
            //
        }
    }
    
    //clear button action
    public void clearButton() {
        yearSelect.setSelectedIndex(0);
        monthSelect.setSelectedIndex(0);
        unitSelect.setSelectedIndex(0);
        incomec1.setText("");
        expensec1.setText("");
        incomec2.setText("");
        expensec2.setText("");
        incomec3.setText("");
        expensec3.setText("");
        sheetName.setText("  Select a year and month first");
    }
    
    public void saveButton() {
        String year = (String) yearSelect.getSelectedItem();
        String month = (String) monthSelect.getSelectedItem();
        String unit = (String) unitSelect.getSelectedItem();
        String sheetname = sheetName.getText().trim();
        String incomeC1 = incomec1.getText().trim();
        String incomeC2 = incomec2.getText().trim();
        String incomeC3 = incomec3.getText().trim();
        String expenseC1 = expensec1.getText().trim();
        String expenseC2 = expensec2.getText().trim();
        String expenseC3 = expensec3.getText().trim();

        if (year == null || month == null || unit == null || 
            "Year".equals(year) || "Month".equals(month) || "Unit".equals(unit) || 
            sheetname.isEmpty() || 
            "Year Month".equals(sheetname) || 
            "  Select a year and month first".equals(sheetname)) {

            JOptionPane.showMessageDialog(null, 
                "Please select Year, Month, Unit, and provide a valid Sheet Name.");
            return;
        }
        
        if (!sheetname.matches("^[a-zA-Z0-9 ]{1,30}$")) {
            JOptionPane.showMessageDialog(null, "Sheet Name can only contain English letters numbers and spaces, and must be 1-30 characters long.");
            return;
        }
        
        if (incomeC1 != null && !incomeC1.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(null, "incomeC1 can only contain English letters numbers and spaces, and must be 0-30 characters long.");
            return;
        }

        if (incomeC2 != null && !incomeC2.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(null, "incomeC2 can only contain English letters numbers and spaces, and must be 0-30 characters long.");
            return;
        }

        if (incomeC3 != null && !incomeC3.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(null, "incomeC3 can only contain English letters numbers and spaces, and must be 0-30 characters long.");
            return;
        }

        if (expenseC1 != null && !expenseC1.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(null, "expenseC1 can only contain English letters numbers and spaces, and must be 0-30 characters long.");
            return;
        }

        if (expenseC2 != null && !expenseC2.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(null, "expenseC2 can only contain English letters numbers and spaces, and must be 0-30 characters long.");
            return;
        }

        if (expenseC3 != null && !expenseC3.matches("^[a-zA-Z0-9 ]{0,30}$")) {
            JOptionPane.showMessageDialog(null, "expenseC3 can only contain English letters numbers and spaces, and must be 0-30 characters long.");
            return;
        }

        if ("Enter Categories Names".equals(incomeC1)) incomeC1 = "";
        
        try (Connection conn = DBconnection.connect()) {
            conn.setAutoCommit(false);

            // Insert into Sheet table
            String sheetQuery = "INSERT INTO Sheet (sheet_name, unit, year, month, user_id) VALUES (?, ?, ?, ?, ?)";
            int sheetId;
            try (PreparedStatement sheetStmt = conn.prepareStatement(sheetQuery, Statement.RETURN_GENERATED_KEYS)) {
                sheetStmt.setString(1, sheetname);
                sheetStmt.setString(2, unit);
                sheetStmt.setString(3, year);
                sheetStmt.setString(4, month);
                sheetStmt.setInt(5, userId);
                sheetStmt.executeUpdate();

                // Retrieve the generated sheet_id
                try (ResultSet generatedKeys = sheetStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sheetId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve sheet ID.");
                    }
                }
            }

            // Insert categories into the Category table
            insertCategory(conn, sheetId, incomeC1.isEmpty() ? " income 1" : incomeC1, "Income");
            insertCategory(conn, sheetId, incomeC2.isEmpty() ? " income 2" : incomeC2, "Income");
            insertCategory(conn, sheetId, incomeC3.isEmpty() ? " income 3" : incomeC3, "Income");
            insertCategory(conn, sheetId, "Other", "Income");
            insertCategory(conn, sheetId, expenseC1.isEmpty() ? " expense 1" : expenseC1, "Expense");
            insertCategory(conn, sheetId, expenseC2.isEmpty() ? " expense 2" : expenseC2, "Expense");
            insertCategory(conn, sheetId, expenseC3.isEmpty() ? " expense 3" : expenseC3, "Expense");
            insertCategory(conn, sheetId, "Other", "Expense");
            

            conn.commit(); // Commit transaction
            ViewSheet D1 = new ViewSheet(userId, sheetId);
            D1.setVisible(true);
            this.dispose();
            JOptionPane.showMessageDialog(null, "Data saved successfully.");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
        }
    }
    
    private void insertCategory(Connection conn, int sheetId, String categoryName, String type) {
        String query = "INSERT INTO Category (sheet_id, category_name, type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sheetId);
            stmt.setString(2, categoryName);
            stmt.setString(3, type);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting category '" + categoryName + "': " + e.getMessage());
        }
    }

    // load data    
    public void loadData(int sheetId) {
        try (Connection conn = DBconnection.connect()) {
            // Query to fetch sheet details
            String sheetQuery = "SELECT sheet_name, unit, year, month FROM Sheet WHERE sheet_id = ?";
            try (PreparedStatement sheetStmt = conn.prepareStatement(sheetQuery)) {
                sheetStmt.setInt(1, sheetId);

                try (ResultSet rs = sheetStmt.executeQuery()) {
                    if (rs.next()) {
                        // Populate sheet details
                        sheetName.setText(rs.getString("sheet_name"));
                        unitSelect.setSelectedItem(rs.getString("unit"));
                        yearSelect.setSelectedItem(rs.getString("year"));
                        monthSelect.setSelectedItem(rs.getString("month"));
                    } else {
                        JOptionPane.showMessageDialog(null, "Sheet not found.");
                        return;
                    }
                }
            }

            // Query to fetch categories
            String categoryQuery = "SELECT category_name, type FROM Category WHERE sheet_id = ?";
            try (PreparedStatement categoryStmt = conn.prepareStatement(categoryQuery)) {
                categoryStmt.setInt(1, sheetId);

                try (ResultSet rs = categoryStmt.executeQuery()) {
                    // Reset fields
                    incomec1.setText(" ");
                    incomec2.setText("");
                    incomec3.setText("");
                    expensec1.setText("");
                    expensec2.setText("");
                    expensec3.setText("");

                    // Load categories into respective fields
                    int incomeCount = 0;
                    int expenseCount = 0;

                    while (rs.next()) {
                        String categoryName = rs.getString("category_name");
                        String type = rs.getString("type");

                        if ("Income".equalsIgnoreCase(type)) {
                            if (incomeCount == 0) incomec1.setText(categoryName);
                            else if (incomeCount == 1) incomec2.setText(categoryName);
                            else if (incomeCount == 2) incomec3.setText(categoryName);
                            incomeCount++;
                        } else if ("Expense".equalsIgnoreCase(type)) {
                            if (expenseCount == 0) expensec1.setText(categoryName);
                            else if (expenseCount == 1) expensec2.setText(categoryName);
                            else if (expenseCount == 2) expensec3.setText(categoryName);
                            expenseCount++;
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }

    //update 
    public void updateButton() {
        String year = (String) yearSelect.getSelectedItem();
        String month = (String) monthSelect.getSelectedItem();
        String unit = (String) unitSelect.getSelectedItem();
        String sheetname = sheetName.getText().trim();
        String incomeC1 = incomec1.getText().trim();
        String incomeC2 = incomec2.getText().trim();
        String incomeC3 = incomec3.getText().trim();
        String expenseC1 = expensec1.getText().trim();
        String expenseC2 = expensec2.getText().trim();
        String expenseC3 = expensec3.getText().trim();

        if (year == null || month == null || unit == null || 
            "Year".equals(year) || "Month".equals(month) || "Unit".equals(unit) || 
            sheetname.isEmpty() || 
            "Year Month".equals(sheetname) || 
            "  Select a year and month first".equals(sheetname)) {

            JOptionPane.showMessageDialog(null, 
                "Please select Year, Month, Unit, and provide a valid Sheet Name.");
            return;
        }

        if (!sheetname.matches("^[a-zA-Z0-9 ]{1,30}$")) {
            JOptionPane.showMessageDialog(null, "Sheet Name can only contain English letters, numbers, and spaces, and must be 1-30 characters long.");
            return;
        }

        try (Connection conn = DBconnection.connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Update Sheet table
            String sheetUpdateQuery = "UPDATE Sheet SET sheet_name = ?, unit = ?, year = ?, month = ? WHERE sheet_id = ? AND user_id = ?";
            try (PreparedStatement sheetStmt = conn.prepareStatement(sheetUpdateQuery)) {
                sheetStmt.setString(1, sheetname);
                sheetStmt.setString(2, unit);
                sheetStmt.setString(3, year);
                sheetStmt.setString(4, month);
                sheetStmt.setInt(5, sheetId);
                sheetStmt.setInt(6, userId);

                if (sheetStmt.executeUpdate() == 0) {
                    throw new SQLException("No rows updated. Sheet may not exist or belong to this user.");
                }
            }
            
            // Get the current max category_id for the given sheetId
            String getMaxCategoryIdQuery = "SELECT MAX(category_id) FROM Category WHERE sheet_id = ?";
            int maxCategoryId = 0;

            try (PreparedStatement getMaxStmt = conn.prepareStatement(getMaxCategoryIdQuery)) {
                getMaxStmt.setInt(1, sheetId);

                try (ResultSet rs = getMaxStmt.executeQuery()) {
                    if (rs.next()) {
                        maxCategoryId = rs.getInt(1);
                    }
                }
            }
            int categoryId = maxCategoryId - 7;

            // Update or insert categories in the Category table
            updateCategory(conn, sheetId, incomeC1.isEmpty() ? " income 1" : incomeC1, "Income", categoryId++);
            updateCategory(conn, sheetId, incomeC2.isEmpty() ? " income 2" : incomeC2, "Income", categoryId++);
            updateCategory(conn, sheetId, incomeC3.isEmpty() ? " income 3" : incomeC3, "Income", categoryId++);
            updateCategory(conn, sheetId, "Other", "Income", categoryId++);
            updateCategory(conn, sheetId, expenseC1.isEmpty() ? " expense 1" : expenseC1, "Expense", categoryId++);
            updateCategory(conn, sheetId, expenseC2.isEmpty() ? " expense 2" : expenseC2, "Expense", categoryId++);
            updateCategory(conn, sheetId, expenseC3.isEmpty() ? " expense 3" : expenseC3, "Expense", categoryId++);
            updateCategory(conn, sheetId, "Other", "Expense", categoryId++);

            conn.commit(); // Commit transaction
            JOptionPane.showMessageDialog(null, "Data updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating data: " + e.getMessage());
        }
    }

    private void updateCategory(Connection conn, int sheetId, String categoryName, String type, int categoryId) throws SQLException {
    // Update the existing record
    String updateQuery = "UPDATE Category SET category_name = ?, type = ? WHERE sheet_id = ? AND category_id = ?";
    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
        updateStmt.setString(1, categoryName);
        updateStmt.setString(2, type);
        updateStmt.setInt(3, sheetId);
        updateStmt.setInt(4, categoryId);

        // Execute the update query
        int rowsAffected = updateStmt.executeUpdate();
        
        
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
        yearSelect = new javax.swing.JComboBox<>();
        unitSelect = new javax.swing.JComboBox<>();
        labelUsername = new javax.swing.JLabel();
        sheetName = new javax.swing.JTextField();
        monthSelect = new javax.swing.JComboBox<>();
        labelUsername1 = new javax.swing.JLabel();
        incomec1 = new javax.swing.JTextField();
        expensec1 = new javax.swing.JTextField();
        labelUsername2 = new javax.swing.JLabel();
        clearButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        labelUsername3 = new javax.swing.JLabel();
        incomec2 = new javax.swing.JTextField();
        expensec2 = new javax.swing.JTextField();
        labelUsername4 = new javax.swing.JLabel();
        labelUsername5 = new javax.swing.JLabel();
        incomec3 = new javax.swing.JTextField();
        expensec3 = new javax.swing.JTextField();
        labelUsername6 = new javax.swing.JLabel();
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
        addNewSheetButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
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

        yearSelect.setBackground(new java.awt.Color(255, 255, 255));
        yearSelect.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        yearSelect.setForeground(new java.awt.Color(51, 51, 51));
        yearSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Year", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050", "2051", "2052", "2053", "2054", "2055", "2056", "2057", "2058", "2059", "2060", "2061", "2062", "2063", "2064", "2065", "2066", "2067", "2068", "2069", "2070", "2071", "2072", "2073", "2074", "2075", "2076", "2077", "2078", "2079", "2080", "2081", "2082", "2083", "2084", "2085", "2086", "2087", "2088", "2089", "2090", "2091", "2092", "2093", "2094", "2095", "2096", "2097", "2098", "2099" }));
        jPanel9.add(yearSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 180, 50));

        unitSelect.setBackground(new java.awt.Color(255, 255, 255));
        unitSelect.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        unitSelect.setForeground(new java.awt.Color(51, 51, 51));
        unitSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Unit", "AED", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK", "HUF", "INR", "ISK", "JPY", "KES", "KRW", "KWD", "LKR", "MXN", "MYR", "NOK", "NZD", "PKR", "PLN", "QAR", "RON", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TWD", "USD", "ZAR" }));
        jPanel9.add(unitSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 100, 50));

        labelUsername.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelUsername.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername.setText("Sheet Name");
        jPanel9.add(labelUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        sheetName.setBackground(new java.awt.Color(255, 255, 255));
        sheetName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        sheetName.setForeground(new java.awt.Color(51, 51, 51));
        sheetName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sheetNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sheetNameFocusLost(evt);
            }
        });
        jPanel9.add(sheetName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 250, 50));

        monthSelect.setBackground(new java.awt.Color(255, 255, 255));
        monthSelect.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        monthSelect.setForeground(new java.awt.Color(51, 51, 51));
        monthSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        jPanel9.add(monthSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 180, 50));

        labelUsername1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelUsername1.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername1.setText("Income category 1");
        jPanel9.add(labelUsername1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        incomec1.setBackground(new java.awt.Color(255, 255, 255));
        incomec1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        incomec1.setForeground(new java.awt.Color(51, 51, 51));
        incomec1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                incomec1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                incomec1FocusLost(evt);
            }
        });
        jPanel9.add(incomec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 180, 40));

        expensec1.setBackground(new java.awt.Color(255, 255, 255));
        expensec1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expensec1.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(expensec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 180, 40));

        labelUsername2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelUsername2.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername2.setText("Expense category 1");
        jPanel9.add(labelUsername2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, -1, -1));

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
        jPanel9.add(clearButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 110, 40));

        saveButton.setBackground(new java.awt.Color(7, 23, 90));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Save");
        saveButton.setAlignmentY(1.0F);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel9.add(saveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, 110, 40));

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
        jPanel9.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 410, 110, 40));

        labelUsername3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelUsername3.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername3.setText("Income category 2");
        jPanel9.add(labelUsername3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        incomec2.setBackground(new java.awt.Color(255, 255, 255));
        incomec2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        incomec2.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(incomec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 180, 40));

        expensec2.setBackground(new java.awt.Color(255, 255, 255));
        expensec2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expensec2.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(expensec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 180, 40));

        labelUsername4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelUsername4.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername4.setText("Expense category 2");
        jPanel9.add(labelUsername4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, -1, -1));

        labelUsername5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelUsername5.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername5.setText("Income category 3");
        jPanel9.add(labelUsername5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        incomec3.setBackground(new java.awt.Color(255, 255, 255));
        incomec3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        incomec3.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(incomec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 180, 40));

        expensec3.setBackground(new java.awt.Color(255, 255, 255));
        expensec3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expensec3.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.add(expensec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 180, 40));

        labelUsername6.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelUsername6.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername6.setText("Expense category 3");
        jPanel9.add(labelUsername6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, -1, -1));

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
        labelHeading.setText("New Sheet");
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
        AddNewSheet D1 = new AddNewSheet(userId, sheetId);
        D1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_addNewSheetButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        Dashboard D1 = new Dashboard(userId, sheetId);
        D1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void enterBudgetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterBudgetButtonActionPerformed
        if (sheetId > 0) {
            EnterBudget D1 = new EnterBudget(userId, sheetId); // Pass both userId and sheetId
            D1.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Please save the sheet first before entering budget.");
        }
    }//GEN-LAST:event_enterBudgetButtonActionPerformed

    private void viewSummaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSummaryButtonActionPerformed
        if (sheetId > 0) {
            ViewSummary D1 = new ViewSummary(userId, sheetId); 
            D1.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Please save the sheet first before viewing summary.");
        }
    }//GEN-LAST:event_viewSummaryButtonActionPerformed

    private void sheetNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sheetNameFocusGained
        if ("  Select a year and month first".equals(sheetName.getText())) {
            sheetName.setText("");
            sheetName.setForeground(java.awt.Color.decode("#333333"));
            sheetName.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            
            String selectedYear = (String) yearSelect.getSelectedItem();
            String selectedMonth = (String) monthSelect.getSelectedItem();
            if (selectedYear != null && selectedMonth != null) {
                sheetName.setText(selectedYear + " " + selectedMonth);
            }
        }
    }//GEN-LAST:event_sheetNameFocusGained

    private void sheetNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sheetNameFocusLost
        if ("".equals(sheetName.getText())) {
            sheetName.setText("  Select a year and month first");
            sheetName.setForeground(java.awt.Color.decode("#A9A9A9"));
            sheetName.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        }
    }//GEN-LAST:event_sheetNameFocusLost

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearButton();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (isUpdate) {
            updateButton();  
        } else {
            saveButton();  
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        deleteButton();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void incomec1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_incomec1FocusGained
        if ("  Enter Categories Names".equals(incomec1.getText())) {
            incomec1.setText("");
            incomec1.setForeground(java.awt.Color.decode("#333333"));
            incomec1.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        }
    }//GEN-LAST:event_incomec1FocusGained

    private void incomec1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_incomec1FocusLost
        if ("".equals(incomec1.getText())) {
            incomec1.setText("  Enter Categories Names");
            incomec1.setForeground(java.awt.Color.decode("#A9A9A9"));
            incomec1.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        }
    }//GEN-LAST:event_incomec1FocusLost

    
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
            java.util.logging.Logger.getLogger(AddNewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddNewSheet(0,0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewSheetButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton enterBudgetButton;
    private javax.swing.JTextField expensec1;
    private javax.swing.JTextField expensec2;
    private javax.swing.JTextField expensec3;
    private javax.swing.JTextField incomec1;
    private javax.swing.JTextField incomec2;
    private javax.swing.JTextField incomec3;
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
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelPhoto;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JLabel labelUsername1;
    private javax.swing.JLabel labelUsername2;
    private javax.swing.JLabel labelUsername3;
    private javax.swing.JLabel labelUsername4;
    private javax.swing.JLabel labelUsername5;
    private javax.swing.JLabel labelUsername6;
    private javax.swing.JComboBox<String> monthSelect;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField sheetName;
    private javax.swing.JComboBox<String> unitSelect;
    private javax.swing.JButton viewSummaryButton;
    private javax.swing.JComboBox<String> yearSelect;
    // End of variables declaration//GEN-END:variables
}
