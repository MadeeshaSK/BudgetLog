//@author MadeeshaSK

package me.madeeshask.budgetlog.views;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import me.madeeshask.budgetlog.utils.Utils;
import javax.swing.SwingUtilities;
import me.madeeshask.budgetlog.database.DBconnection;
public class ViewSummary extends javax.swing.JFrame {

    public int sheetId;
    public int userId;

    public ViewSummary(int userId, int sheetId) {
        initComponents();
        this.sheetId = sheetId;
        this.userId = userId;
        conScaleImage();
        conSetWindowProperties(); 
        consetLabelFont();
        fetchSheetAndCategoryDetails();
        labelAlign();
        fetchSheetAndCategoryDetails();
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
            Utils.setLabelFont(labelBalance, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            Utils.setLabelFont(labelSheetname, "fonts/Roboto-Bold.ttf", Font.BOLD, 18);
            Utils.setLabelFont(labelHeading4, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            Utils.setLabelFont(labelHeading5, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            
            // add label fonts here
        } catch (IOException ex) {
            Logger.getLogger(ViewSummary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // labelAlign
    public void labelAlign() {
        licb1.setHorizontalAlignment(JLabel.RIGHT);
        licb2.setHorizontalAlignment(JLabel.RIGHT);
        licb3.setHorizontalAlignment(JLabel.RIGHT);
        licbo.setHorizontalAlignment(JLabel.RIGHT);
        lecb1.setHorizontalAlignment(JLabel.RIGHT);
        lecb2.setHorizontalAlignment(JLabel.RIGHT);
        lecb3.setHorizontalAlignment(JLabel.RIGHT);
        lecbo.setHorizontalAlignment(JLabel.RIGHT);
        labelTotalIncomes.setHorizontalAlignment(JLabel.RIGHT);
        labelTotalExpenses.setHorizontalAlignment(JLabel.RIGHT);
        labelBalance.setHorizontalAlignment(JLabel.RIGHT);
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
        ViewSheet D1 = new ViewSheet(userId, sheetId);
        D1.setVisible(true);
        this.dispose();
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
                                    labelec3.setText(categoryName);
                                    break;
                                case 1:
                                    labelec1.setText(categoryName);
                                    break;
                                case 2:
                                    labelec2.setText(categoryName);
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
    
    private void fetchSheetAndCategoryDetails() {
        try (Connection connection = DBconnection.connect()) {
            String sheetQuery = "SELECT sheet_name, unit FROM Sheet WHERE sheet_id = ?";
            try (PreparedStatement sheetStmt = connection.prepareStatement(sheetQuery)) {
                sheetStmt.setInt(1, sheetId);

                try (ResultSet sheetRs = sheetStmt.executeQuery()) {
                    if (sheetRs.next()) {
                        String sheetName = sheetRs.getString("sheet_name");
                        labelSheetname.setText(sheetName);
                        String sheetUnit = sheetRs.getString("unit");
                        labelUnit.setText(sheetUnit);
                    }
                }
            }

            // Fetch category names
            fetchCategoryNames();

            // fetch totals query
            String expenseQuery = """
                SELECT COALESCE(SUM(e.amount), 0) as category_total
                FROM Expense e
                WHERE e.expense_name = ?
                AND e.record_id IN (
                    SELECT r.record_id 
                    FROM Sheet s 
                    JOIN Record r ON r.sheet_id = s.sheet_id 
                    WHERE s.sheet_id = ?
                )
            """;
            
            String incomeQuery = """
                SELECT COALESCE(SUM(i.amount), 0) as category_total
                FROM Income i
                WHERE i.income_name = ?
                AND i.record_id IN (
                    SELECT r.record_id 
                    FROM Sheet s 
                    JOIN Record r ON r.sheet_id = s.sheet_id 
                    WHERE s.sheet_id = ?
                )
            """;

            // Fetch expense totals
            try (PreparedStatement expenseStmt = connection.prepareStatement(expenseQuery)) {
                
                expenseStmt.setString(1, "Expense 1");
                expenseStmt.setInt(2, sheetId);  
                ResultSet rs = expenseStmt.executeQuery();
                if (rs.next()) {
                    lecb1.setText(String.format("%.2f", rs.getDouble("category_total")));
                }

                
                expenseStmt.setString(1, "Expense 2");
                expenseStmt.setInt(2, sheetId);  
                rs = expenseStmt.executeQuery();
                if (rs.next()) {
                    lecb2.setText(String.format("%.2f", rs.getDouble("category_total")));
                }

                
                expenseStmt.setString(1, "Expense 3");
                expenseStmt.setInt(2, sheetId);  
                rs = expenseStmt.executeQuery();
                if (rs.next()) {
                    lecb3.setText(String.format("%.2f", rs.getDouble("category_total")));
                }

                
                expenseStmt.setString(1, "Expense Other");
                expenseStmt.setInt(2, sheetId);  
                rs = expenseStmt.executeQuery();
                if (rs.next()) {
                    lecbo.setText(String.format("%.2f", rs.getDouble("category_total")));
                }
            }

            
            try (PreparedStatement incomeStmt = connection.prepareStatement(incomeQuery)) {
                
                incomeStmt.setString(1, "Income 1");
                incomeStmt.setInt(2, sheetId);  
                ResultSet rs = incomeStmt.executeQuery();
                if (rs.next()) {
                    licb1.setText(String.format("%.2f", rs.getDouble("category_total")));
                }

                
                incomeStmt.setString(1, "Income 2");
                incomeStmt.setInt(2, sheetId);  
                rs = incomeStmt.executeQuery();
                if (rs.next()) {
                    licb2.setText(String.format("%.2f", rs.getDouble("category_total")));
                }

                
                incomeStmt.setString(1, "Income 3");
                incomeStmt.setInt(2, sheetId);  
                rs = incomeStmt.executeQuery();
                if (rs.next()) {
                    licb3.setText(String.format("%.2f", rs.getDouble("category_total")));
                }

                
                incomeStmt.setString(1, "Income Other");
                incomeStmt.setInt(2, sheetId);  
                rs = incomeStmt.executeQuery();
                if (rs.next()) {
                    licbo.setText(String.format("%.2f", rs.getDouble("category_total")));
                }
            }

            // Calculate and display totals
            double totalIncome = Double.parseDouble(licb1.getText()) +
                               Double.parseDouble(licb2.getText()) +
                               Double.parseDouble(licb3.getText()) +
                               Double.parseDouble(licbo.getText());

            double totalExpenses = Double.parseDouble(lecb1.getText()) +
                                 Double.parseDouble(lecb2.getText()) +
                                 Double.parseDouble(lecb3.getText()) +
                                 Double.parseDouble(lecbo.getText());
            
            double balance = totalIncome - totalExpenses;
            
            labelTotalIncomes.setText(String.format("%.2f", totalIncome));
            labelTotalExpenses.setText(String.format("%.2f", totalExpenses));
            labelBalance.setText(String.format("%.2f", balance));
            
            if (balance == 0) {
                labelBalance.setForeground(Color.WHITE); 
            } else if (balance > 0) {
                labelBalance.setForeground(Color.GREEN); 
            } else {
                labelBalance.setForeground(Color.RED);   
            }


        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
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
        viewButton = new javax.swing.JButton();
        labelSheetname = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        labeleco = new javax.swing.JLabel();
        labelico = new javax.swing.JLabel();
        labelec2 = new javax.swing.JLabel();
        labelic3 = new javax.swing.JLabel();
        labelec1 = new javax.swing.JLabel();
        labelic2 = new javax.swing.JLabel();
        labelic1 = new javax.swing.JLabel();
        labelec3 = new javax.swing.JLabel();
        labelBalance = new javax.swing.JLabel();
        labelHeading4 = new javax.swing.JLabel();
        labelHeading5 = new javax.swing.JLabel();
        labelUnit = new javax.swing.JLabel();
        labelHeading7 = new javax.swing.JLabel();
        licb1 = new javax.swing.JLabel();
        labelTotalIncomes = new javax.swing.JLabel();
        licb2 = new javax.swing.JLabel();
        licb3 = new javax.swing.JLabel();
        licbo = new javax.swing.JLabel();
        labelTotalExpenses = new javax.swing.JLabel();
        lecb1 = new javax.swing.JLabel();
        lecb2 = new javax.swing.JLabel();
        lecb3 = new javax.swing.JLabel();
        lecbo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        labelHeading2 = new javax.swing.JLabel();

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
        enterBudgetButton.setBorder(null);
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
        viewSummaryButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
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

        labelSheetname.setBackground(new java.awt.Color(6, 26, 45));
        labelSheetname.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        labelSheetname.setForeground(new java.awt.Color(255, 255, 255));
        labelSheetname.setText("Sheet Name");
        labelSheetname.setOpaque(true);
        jPanel9.add(labelSheetname, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 260, 40));

        jPanel10.setBackground(new java.awt.Color(6, 24, 67));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labeleco.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labeleco.setForeground(new java.awt.Color(255, 255, 255));
        labeleco.setText("Other Expenses");
        jPanel10.add(labeleco, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, -1, -1));

        labelico.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelico.setForeground(new java.awt.Color(255, 255, 255));
        labelico.setText("Other Incomes");
        jPanel10.add(labelico, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        labelec2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelec2.setForeground(new java.awt.Color(255, 255, 255));
        labelec2.setText("Expense category 2");
        jPanel10.add(labelec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, -1));

        labelic3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelic3.setForeground(new java.awt.Color(255, 255, 255));
        labelic3.setText("Income category 3");
        jPanel10.add(labelic3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));

        labelec1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelec1.setForeground(new java.awt.Color(255, 255, 255));
        labelec1.setText("Expense category 1");
        jPanel10.add(labelec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));

        labelic2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelic2.setForeground(new java.awt.Color(255, 255, 255));
        labelic2.setText("Income category 2");
        jPanel10.add(labelic2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        labelic1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelic1.setForeground(new java.awt.Color(255, 255, 255));
        labelic1.setText("Income category 1");
        jPanel10.add(labelic1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, -1, -1));

        labelec3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelec3.setForeground(new java.awt.Color(255, 255, 255));
        labelec3.setText("Expense category 3");
        jPanel10.add(labelec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        labelBalance.setBackground(new java.awt.Color(6, 26, 45));
        labelBalance.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelBalance.setForeground(new java.awt.Color(255, 255, 255));
        labelBalance.setText("0000000");
        jPanel10.add(labelBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 130, 40));

        labelHeading4.setBackground(new java.awt.Color(6, 26, 45));
        labelHeading4.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelHeading4.setForeground(new java.awt.Color(54, 205, 29));
        labelHeading4.setText("Incomes");
        jPanel10.add(labelHeading4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 40));

        labelHeading5.setBackground(new java.awt.Color(6, 26, 45));
        labelHeading5.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelHeading5.setForeground(new java.awt.Color(255, 0, 0));
        labelHeading5.setText("Expenses");
        jPanel10.add(labelHeading5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, 40));

        labelUnit.setBackground(new java.awt.Color(6, 26, 45));
        labelUnit.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelUnit.setForeground(new java.awt.Color(255, 255, 255));
        labelUnit.setText("LKR");
        jPanel10.add(labelUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 80, 40));

        labelHeading7.setBackground(new java.awt.Color(6, 26, 45));
        labelHeading7.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelHeading7.setForeground(new java.awt.Color(255, 255, 255));
        labelHeading7.setText("Balance");
        jPanel10.add(labelHeading7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 100, 40));

        licb1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        licb1.setForeground(new java.awt.Color(255, 255, 255));
        licb1.setText("0000000");
        jPanel10.add(licb1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 100, -1));

        labelTotalIncomes.setBackground(new java.awt.Color(6, 26, 45));
        labelTotalIncomes.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelTotalIncomes.setForeground(new java.awt.Color(54, 205, 29));
        labelTotalIncomes.setText("0000000");
        jPanel10.add(labelTotalIncomes, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 150, 40));

        licb2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        licb2.setForeground(new java.awt.Color(255, 255, 255));
        licb2.setText("0000000");
        jPanel10.add(licb2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 90, 100, -1));

        licb3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        licb3.setForeground(new java.awt.Color(255, 255, 255));
        licb3.setText("0000000");
        jPanel10.add(licb3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, 100, -1));

        licbo.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        licbo.setForeground(new java.awt.Color(255, 255, 255));
        licbo.setText("0000000");
        jPanel10.add(licbo, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 100, -1));

        labelTotalExpenses.setBackground(new java.awt.Color(6, 26, 45));
        labelTotalExpenses.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelTotalExpenses.setForeground(new java.awt.Color(255, 0, 0));
        labelTotalExpenses.setText("0000000");
        jPanel10.add(labelTotalExpenses, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 150, 40));

        lecb1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lecb1.setForeground(new java.awt.Color(255, 255, 255));
        lecb1.setText("0000000");
        jPanel10.add(lecb1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, 100, -1));

        lecb2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lecb2.setForeground(new java.awt.Color(255, 255, 255));
        lecb2.setText("0000000");
        jPanel10.add(lecb2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, 100, -1));

        lecb3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lecb3.setForeground(new java.awt.Color(255, 255, 255));
        lecb3.setText("0000000");
        jPanel10.add(lecb3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, 100, -1));

        lecbo.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lecbo.setForeground(new java.awt.Color(255, 255, 255));
        lecbo.setText("0000000");
        jPanel10.add(lecbo, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 310, 100, -1));

        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 380, 400));

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

        labelHeading2.setBackground(new java.awt.Color(6, 26, 45));
        labelHeading2.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelHeading2.setForeground(new java.awt.Color(255, 255, 255));
        labelHeading2.setText("Summary");
        labelHeading2.setOpaque(true);
        jPanel7.add(labelHeading2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 270, 80));

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

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        viewButton();
    }//GEN-LAST:event_viewButtonActionPerformed

    
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
            java.util.logging.Logger.getLogger(ViewSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new ViewSummary(0,0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewSheetButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton enterBudgetButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel labelBalance;
    private javax.swing.JLabel labelHeading2;
    private javax.swing.JLabel labelHeading4;
    private javax.swing.JLabel labelHeading5;
    private javax.swing.JLabel labelHeading7;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelPhoto;
    private javax.swing.JLabel labelSheetname;
    private javax.swing.JLabel labelTotalExpenses;
    private javax.swing.JLabel labelTotalIncomes;
    private javax.swing.JLabel labelUnit;
    private javax.swing.JLabel labelec1;
    private javax.swing.JLabel labelec2;
    private javax.swing.JLabel labelec3;
    private javax.swing.JLabel labeleco;
    private javax.swing.JLabel labelic1;
    private javax.swing.JLabel labelic2;
    private javax.swing.JLabel labelic3;
    private javax.swing.JLabel labelico;
    private javax.swing.JLabel lecb1;
    private javax.swing.JLabel lecb2;
    private javax.swing.JLabel lecb3;
    private javax.swing.JLabel lecbo;
    private javax.swing.JLabel licb1;
    private javax.swing.JLabel licb2;
    private javax.swing.JLabel licb3;
    private javax.swing.JLabel licbo;
    private javax.swing.JButton viewButton;
    private javax.swing.JButton viewSummaryButton;
    // End of variables declaration//GEN-END:variables
}
