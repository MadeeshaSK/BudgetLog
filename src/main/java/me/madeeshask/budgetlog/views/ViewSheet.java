//@author MadeeshaSK

package me.madeeshask.budgetlog.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import me.madeeshask.budgetlog.database.DBconnection;
import me.madeeshask.budgetlog.utils.Utils;


public class ViewSheet extends javax.swing.JFrame {
    
    public int userId;
    public int sheetId;
    
    public ViewSheet(int userId, int sheetId) {
        initComponents();
        this.sheetId = sheetId;
        this.userId = userId;
        tablepropBalance();
        conScaleImage();
        conSetWindowProperties();
        consetLabelFont();
        tableLoad();
        tableprp();
        
    }
    
    // scale image in constructor
    public void conScaleImage() {
        // scale image 
        SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
                Utils.scaleImage("images/logo-with-background.png", labelLogo);
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
            Utils.setLabelFont(labelSheetname, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            // add label fonts here
            
        } catch (IOException ex) {
            Logger.getLogger(ViewSummary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // load table
    public void tableLoad() {
        try (Connection connection = DBconnection.connect();
             PreparedStatement sheetStmt = connection.prepareStatement(
                 "SELECT sheet_name, unit FROM Sheet WHERE sheet_id = ?")) {

            sheetStmt.setInt(1, sheetId);

            try (ResultSet sheetRs = sheetStmt.executeQuery()) {
                if (sheetRs.next()) {
                    labelSheetname.setText(sheetRs.getString("sheet_name"));
                    labelUnit.setText(sheetRs.getString("unit"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading table: " + e.getMessage(), 
                                      "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        loadDatesToTable();
        loadIncomeCategoriesAsHeaders();
        loadExpenseCategoriesAsHeaders();
        loadIncomeDataForAllRecords();
        loadExpenseDataForAllRecords();
        calculateBalanceForAllRows();
        populateEditColumn();
        alignTableColumns();
    }
    
    // load date
    public void loadDatesToTable() {
        String query = "SELECT date FROM Record WHERE sheet_id = ?";

        try (Connection connection = DBconnection.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, sheetId);

            try (ResultSet rs = stmt.executeQuery()) {
                
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Date");

                while (rs.next()) {
                    String date = rs.getString("date");
                    model.addRow(new Object[]{date});
                }

                DateTable.setModel(model);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading dates: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //load headers - income
    public void loadIncomeCategoriesAsHeaders() {
        String getCategoryNamesQuery = "SELECT category_name FROM Category WHERE type = 'Income' AND sheet_id = ?";

        try (Connection connection = DBconnection.connect();
             PreparedStatement categoryStmt = connection.prepareStatement(getCategoryNamesQuery)) {
            
            categoryStmt.setInt(1, sheetId);

            String category1 = "N/A", category2 = "N/A", category3 = "N/A", category4 = "N/A";
            try (ResultSet categoryRs = categoryStmt.executeQuery()) {
                if (categoryRs.next()) category1 = categoryRs.getString("category_name");
                if (categoryRs.next()) category2 = categoryRs.getString("category_name");
                if (categoryRs.next()) category3 = categoryRs.getString("category_name");
                if (categoryRs.next()) category4 = categoryRs.getString("category_name");
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{category1, category2, category3, category4, "Total Income"});

            IncomeTable.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading income categories: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // load headers - expense
    public void loadExpenseCategoriesAsHeaders() {
        String getCategoryNamesQuery = "SELECT category_name FROM Category WHERE type = 'Expense' AND sheet_id = ?";

        try (Connection connection = DBconnection.connect();
             PreparedStatement categoryStmt = connection.prepareStatement(getCategoryNamesQuery)) {

            categoryStmt.setInt(1, sheetId);

            String category1 = "N/A", category2 = "N/A", category3 = "N/A", category4 = "N/A";
            try (ResultSet categoryRs = categoryStmt.executeQuery()) {
                if (categoryRs.next()) category1 = categoryRs.getString("category_name");
                if (categoryRs.next()) category2 = categoryRs.getString("category_name");
                if (categoryRs.next()) category3 = categoryRs.getString("category_name");
                if (categoryRs.next()) category4 = categoryRs.getString("category_name");
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{category1, category2, category3, category4, "Total Expense"});

            ExpenseTable.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading expense categories: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //load data income
    public void loadIncomeDataForAllRecords() {
        String getRecordIdsQuery = "SELECT record_id FROM Record WHERE sheet_id = ?";
        String getIncomeDataQuery = "SELECT income_name, amount FROM Income WHERE record_id = ? ORDER BY income_name ASC";

        try (Connection connection = DBconnection.connect();
             PreparedStatement recordStmt = connection.prepareStatement(getRecordIdsQuery)) {

            recordStmt.setInt(1, sheetId);

            try (ResultSet recordRs = recordStmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) IncomeTable.getModel();
                while (recordRs.next()) {
                    int recordId = recordRs.getInt("record_id");

                    try (PreparedStatement incomeStmt = connection.prepareStatement(getIncomeDataQuery)) {
                        incomeStmt.setInt(1, recordId);

                        String income1 = "N/A", income2 = "N/A", income3 = "N/A", income4 = "N/A";
                        double amount1 = 0.0, amount2 = 0.0, amount3 = 0.0, amount4 = 0.0;
                        int count = 0;

                        try (ResultSet incomeRs = incomeStmt.executeQuery()) {
                            while (incomeRs.next() && count < 4) {
                                String incomeName = incomeRs.getString("income_name");
                                double amount = incomeRs.getDouble("amount");

                                if (count == 0) {
                                    income1 = incomeName;
                                    amount1 = amount;
                                } else if (count == 1) {
                                    income2 = incomeName;
                                    amount2 = amount;
                                } else if (count == 2) {
                                    income3 = incomeName;
                                    amount3 = amount;
                                } else if (count == 3) {
                                    income4 = incomeName;
                                    amount4 = amount;
                                }
                                count++;
                            }
                        }

                        double totalAmount = amount1 + amount2 + amount3 + amount4;
                        model.addRow(new Object[]{amount1, amount2, amount3, amount4, totalAmount});
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading income data: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // load data expense
    public void loadExpenseDataForAllRecords() {
        String getRecordIdsQuery = "SELECT record_id FROM Record WHERE sheet_id = ?";
        String getExpenseDataQuery = "SELECT expense_name, amount FROM Expense WHERE record_id = ? ORDER BY expense_name ASC";

        try (Connection connection = DBconnection.connect();
             PreparedStatement recordStmt = connection.prepareStatement(getRecordIdsQuery)) {

            recordStmt.setInt(1, sheetId);

            try (ResultSet recordRs = recordStmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) ExpenseTable.getModel();
                while (recordRs.next()) {
                    int recordId = recordRs.getInt("record_id");

                    try (PreparedStatement expenseStmt = connection.prepareStatement(getExpenseDataQuery)) {
                        expenseStmt.setInt(1, recordId);

                        String expense1 = "N/A", expense2 = "N/A", expense3 = "N/A", expense4 = "N/A";
                        double amount1 = 0.0, amount2 = 0.0, amount3 = 0.0, amount4 = 0.0;
                        int count = 0;

                        try (ResultSet expenseRs = expenseStmt.executeQuery()) {
                            while (expenseRs.next() && count < 4) {
                                String expenseName = expenseRs.getString("expense_name");
                                double amount = expenseRs.getDouble("amount");

                                if (count == 0) {
                                    expense1 = expenseName;
                                    amount1 = amount;
                                } else if (count == 1) {
                                    expense2 = expenseName;
                                    amount2 = amount;
                                } else if (count == 2) {
                                    expense3 = expenseName;
                                    amount3 = amount;
                                } else if (count == 3) {
                                    expense4 = expenseName;
                                    amount4 = amount;
                                }
                                count++;
                            }
                        }

                        double totalAmount = amount1 + amount2 + amount3 + amount4;
                        model.addRow(new Object[]{amount1, amount2, amount3, amount4, totalAmount});
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading expense data: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // calculate balance
    public void calculateBalanceForAllRows() {
        DefaultTableModel incomeModel = (DefaultTableModel) IncomeTable.getModel();
        DefaultTableModel expenseModel = (DefaultTableModel) ExpenseTable.getModel();
        DefaultTableModel balanceModel = (DefaultTableModel) BalanceTable.getModel();

        int rowCount = Math.min(incomeModel.getRowCount(), expenseModel.getRowCount());

        for (int i = 0; i < rowCount; i++) {
            double totalIncome = (double) incomeModel.getValueAt(i, 4);
            double totalExpense = (double) expenseModel.getValueAt(i, 4);
            double balance = totalIncome - totalExpense;
            balanceModel.addRow(new Object[]{balance});
        }

        BalanceTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                double balance = (value instanceof Number) ? ((Number) value).doubleValue() : 0.0;

                if (balance > 0) {
                    cell.setBackground(Color.GREEN);
                } else if (balance == 0) {
                    cell.setBackground(Color.WHITE);
                } else {
                    cell.setBackground(Color.RED);
                }

                return cell;
            }
        });
    }
    
    // table properties
    public void tableprp() {
        tablepropBalance();
        tablepropEdit();
        tablepropDate();
        tablepropIncome();
        tablepropExpense();
        
    }

    public void tablepropBalance() {
        setTableProperties(BalanceTable);
    }

    public void tablepropEdit() {
        setTableProperties(EditTable);
    }

    public void tablepropDate() {
        setTableProperties(DateTable);
    }

    public void tablepropIncome() {
        setTableProperties(IncomeTable);
    }

    public void tablepropExpense() {
        setTableProperties(ExpenseTable);
    }

    private void setTableProperties(javax.swing.JTable table) {
        table.setEnabled(false);
        table.getTableHeader().setBackground(java.awt.Color.decode("#07175A"));
        table.getTableHeader().setForeground(java.awt.Color.decode("#FFFFFF"));
        table.setFont(new Font("fonts/Roboto-Bold.ttf", Font.BOLD, 14));
        table.getTableHeader().setFont(new Font("fonts/Roboto-Bold.ttf", Font.BOLD, 14));

        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.getViewport().setBackground(java.awt.Color.decode("#061A2D"));
        scrollPane.setBackground(java.awt.Color.decode("#061A2D"));
        table.setBorder(BorderFactory.createLineBorder(java.awt.Color.decode("#061A2D"), 2));
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(java.awt.Color.decode("#061A2D"), 2));
        scrollPane.setBorder(BorderFactory.createLineBorder(java.awt.Color.decode("#061A2D"), 2));

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, 
                Object value, 
                boolean isSelected, 
                boolean hasFocus, 
                int row, 
                int column
            ) {
                java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row == 0) {
                    cell.setBackground(java.awt.Color.decode("#FFFFFF"));
                } else {
                    cell.setBackground(java.awt.Color.decode("#838D96"));
                }

                return cell;
            }
        });
    }
    
    // fill edit table
    public void populateEditColumn() {
        DefaultTableModel dateTableModel = (DefaultTableModel) DateTable.getModel();
        DefaultTableModel editTableModel = (DefaultTableModel) EditTable.getModel();

        editTableModel.setRowCount(0);

        int rowCount = dateTableModel.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            editTableModel.addRow(new Object[]{"Edit"});
        }
    }






    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        labelSheetname = new javax.swing.JLabel();
        labelUnit = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        editSheetButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ExpenseTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        EditTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        IncomeTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        DateTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        BalanceTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(6, 26, 45));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        labelSheetname.setBackground(new java.awt.Color(6, 26, 45));
        labelSheetname.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelSheetname.setForeground(new java.awt.Color(255, 255, 255));
        labelSheetname.setText("Sheet Name");
        labelSheetname.setOpaque(true);
        jPanel7.add(labelSheetname, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 410, 40));

        labelUnit.setBackground(new java.awt.Color(6, 26, 45));
        labelUnit.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        labelUnit.setForeground(new java.awt.Color(255, 255, 255));
        labelUnit.setText("Unit");
        labelUnit.setOpaque(true);
        jPanel7.add(labelUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 90, 40));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 600, 80));

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
        jPanel5.add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 30, 110, 40));

        editSheetButton.setBackground(new java.awt.Color(7, 23, 90));
        editSheetButton.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        editSheetButton.setForeground(new java.awt.Color(255, 255, 255));
        editSheetButton.setText("Edit Sheet");
        editSheetButton.setAlignmentY(1.0F);
        editSheetButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        editSheetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSheetButtonActionPerformed(evt);
            }
        });
        jPanel5.add(editSheetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 30, 130, 40));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 100));

        ExpenseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Expense 1", "Expense 2", "Expense 3", "Other Expense", "Total Expense"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(ExpenseTable);
        if (ExpenseTable.getColumnModel().getColumnCount() > 0) {
            ExpenseTable.getColumnModel().getColumn(0).setResizable(false);
            ExpenseTable.getColumnModel().getColumn(1).setResizable(false);
            ExpenseTable.getColumnModel().getColumn(2).setResizable(false);
            ExpenseTable.getColumnModel().getColumn(3).setResizable(false);
            ExpenseTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 440, 650));

        EditTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Edit Record"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(EditTable);
        if (EditTable.getColumnModel().getColumnCount() > 0) {
            EditTable.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 110, 100, 650));

        IncomeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Income 1", "Income 2", "Income 3", "Other Income", "Total Income"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(IncomeTable);
        if (IncomeTable.getColumnModel().getColumnCount() > 0) {
            IncomeTable.getColumnModel().getColumn(0).setResizable(false);
            IncomeTable.getColumnModel().getColumn(1).setResizable(false);
            IncomeTable.getColumnModel().getColumn(2).setResizable(false);
            IncomeTable.getColumnModel().getColumn(3).setResizable(false);
            IncomeTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 440, 650));

        DateTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(DateTable);
        if (DateTable.getColumnModel().getColumnCount() > 0) {
            DateTable.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 100, 650));

        BalanceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Balance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(BalanceTable);
        if (BalanceTable.getColumnModel().getColumnCount() > 0) {
            BalanceTable.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 110, 100, 650));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1294, 824));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void editSheetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSheetButtonActionPerformed
        AddNewSheet D1 = new AddNewSheet(userId, sheetId, 0);
        D1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_editSheetButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        Dashboard D1 = new Dashboard(userId, sheetId);
        D1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    
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
            java.util.logging.Logger.getLogger(ViewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewSheet(0,0).setVisible(true);
            }
        });
    }
    
    // Method to align columns in all tables
    private void alignTableColumns() {
       DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
       leftRenderer.setHorizontalAlignment(JLabel.RIGHT);

       DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
       centerRenderer.setHorizontalAlignment(JLabel.CENTER);

       for (int i = 0; i < IncomeTable.getColumnCount(); i++) {
           IncomeTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
       }
       for (int i = 0; i < ExpenseTable.getColumnCount(); i++) {
           ExpenseTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
       }

       DateTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
//       BalanceTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
       EditTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable BalanceTable;
    private javax.swing.JTable DateTable;
    private javax.swing.JTable EditTable;
    private javax.swing.JTable ExpenseTable;
    private javax.swing.JTable IncomeTable;
    private javax.swing.JButton backButton;
    private javax.swing.JButton editSheetButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelSheetname;
    private javax.swing.JLabel labelUnit;
    // End of variables declaration//GEN-END:variables
}
