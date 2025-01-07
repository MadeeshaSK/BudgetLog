//@author MadeeshaSK

package me.madeeshask.budgetlog.views;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import me.madeeshask.budgetlog.utils.Utils;
import javax.swing.SwingUtilities;
import me.madeeshask.budgetlog.database.DBconnection;
public class Dashboard extends javax.swing.JFrame {

    private int userId;
    public int sheetId;
    private String loggedInUsername;

    public Dashboard(int userId, int sheetId) {
        initComponents();
        this.userId = userId;
        this.sheetId = sheetId;
        conScaleImage();
        conSetWindowProperties(); 
        consetLabelFont();
        fetchAndSetUsername();
        setupTableListener();
        loadUserSheets();
        tableprop();
        

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
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // usernamePass 
    private void fetchAndSetUsername() {
        try (Connection connection = DBconnection.connect()) {
            String query = "SELECT user_name FROM User WHERE user_id = ?";
            
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        loggedInUsername = rs.getString("user_name");
                        labelHeading.setText("Hi " + loggedInUsername);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching username: " + e.getMessage());
        }
    }
    
    // loadsheets 
    private Map<Integer, Integer> rowToSheetId = new HashMap<>();

    private void loadUserSheets() {
        try (Connection connection = DBconnection.connect()) {
            String query = "SELECT sheet_id, sheet_name FROM Sheet WHERE user_id = ? ORDER BY sheet_id DESC";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    javax.swing.table.DefaultTableModel model = 
                        (javax.swing.table.DefaultTableModel) sheetTable.getModel();

                    model.setRowCount(0);
                    rowToSheetId.clear();

                    int rowIndex = 0;
                    while (rs.next()) {
                        int sheetId = rs.getInt("sheet_id");
                        String sheetName = rs.getString("sheet_name");
                        model.addRow(new Object[]{sheetName});
                        rowToSheetId.put(rowIndex, sheetId);
                        rowIndex++;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading sheets: " + e.getMessage());
        }
    }

    // Table Row Click
    private void setupTableListener() {
        sheetTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = sheetTable.getSelectedRow();
                if (selectedRow != -1) {
                    int sheetId = rowToSheetId.get(selectedRow);
                    ViewSheet viewSheet = new ViewSheet(userId, sheetId);
                    viewSheet.setVisible(true);
                    dispose();
                }
            }
        });
    }
    
    // table properties
    public void tableprop() {
        sheetTable.getTableHeader().setBackground(java.awt.Color.decode("#07175A"));
        sheetTable.getTableHeader().setForeground(java.awt.Color.decode("#FFFFFF"));
        sheetTable.getTableHeader().setPreferredSize(new Dimension(100, 50));
        sheetTable.setFont(new Font("fonts/Roboto-Bold.ttf", Font.BOLD, 24));
        sheetTable.getTableHeader().setFont(new Font("fonts/Roboto-Bold.ttf", Font.BOLD, 24));
        
//        sheetTable.setRowMargin(5); 
        
        JScrollPane scrollPane = (JScrollPane) sheetTable.getParent().getParent();
        scrollPane.getViewport().setBackground(java.awt.Color.decode("#061A2D"));
        scrollPane.setBackground(java.awt.Color.decode("#061A2D"));  
        sheetTable.setBorder(BorderFactory.createLineBorder(java.awt.Color.decode("#061A2D"), 2));
        sheetTable.getTableHeader().setBorder(BorderFactory.createLineBorder(java.awt.Color.decode("#061A2D"), 2));
        scrollPane.setBorder(BorderFactory.createLineBorder(java.awt.Color.decode("#061A2D"), 2)); 
        
        sheetTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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
        jScrollPane1 = new javax.swing.JScrollPane();
        sheetTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        labelHeading = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();

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

        sheetTable.setBackground(new java.awt.Color(131, 141, 150));
        sheetTable.setForeground(new java.awt.Color(68, 68, 68));
        sheetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Sheet Name"
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
        sheetTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        sheetTable.setGridColor(new java.awt.Color(6, 26, 45));
        sheetTable.setRowHeight(50);
        sheetTable.setSelectionBackground(new java.awt.Color(255, 255, 255));
        sheetTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(sheetTable);

        jPanel9.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 400, 470));

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
        labelHeading.setText("Hi username");
        labelHeading.setOpaque(true);
        jPanel7.add(labelHeading, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 270, 80));

        logoutButton.setBackground(new java.awt.Color(7, 23, 90));
        logoutButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("Logout");
        logoutButton.setAlignmentY(1.0F);
        logoutButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        jPanel7.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 140, 60));

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

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        int choice = JOptionPane.showOptionDialog(
        null,
        "Do you want to logout?",
        "Logout Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        new Object[] { "Yes", "No" },
        "No"
        );

        if (choice == JOptionPane.YES_OPTION) {
            Login D1 = new Login();
            D1.setVisible(true);
            this.dispose();
        }

    }//GEN-LAST:event_logoutButtonActionPerformed

    private void enterBudgetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterBudgetButtonActionPerformed
        if (!rowToSheetId.isEmpty()) {
            int firstRowSheetId = rowToSheetId.get(0);
            EnterBudget D1 = new EnterBudget(userId,firstRowSheetId);
            D1.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No sheets available");
        }
    }//GEN-LAST:event_enterBudgetButtonActionPerformed

    private void viewSummaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSummaryButtonActionPerformed
        if (!rowToSheetId.isEmpty()) {
            int firstRowSheetId = rowToSheetId.get(0);
            ViewSummary D1 = new ViewSummary(userId, firstRowSheetId);
            D1.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No sheets available");
        }
    }//GEN-LAST:event_viewSummaryButtonActionPerformed

    
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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Dashboard(0,0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewSheetButton;
    private javax.swing.JButton enterBudgetButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelHeading;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelPhoto;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTable sheetTable;
    private javax.swing.JButton viewSummaryButton;
    // End of variables declaration//GEN-END:variables
}
