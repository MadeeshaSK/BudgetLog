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
public class Register extends javax.swing.JFrame {
    
    public Register() {
        initComponents();
        conScaleImage();
        conSetWindowProperties(); 
        consetLabelFont();
        conplaceholderdef();
        conFacus();

        
    }
    
    // scale image in constructor
    public void conScaleImage() {
        // scale image 
        SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
                Utils.scaleImage("images/logo-with-background.png", labelLogo);
                Utils.scaleImage("images/login-image.jpg", labelPhoto);
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
            Utils.setLabelFont(labelHeading, "fonts/Roboto-Bold.ttf", Font.BOLD, 48);
            Utils.setLabelFont(labelUsername, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            Utils.setLabelFont(labelPassword, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            Utils.setLabelFont(labelPasswordre, "fonts/Roboto-Bold.ttf", Font.BOLD, 24);
            // add label fonts here
        } catch (IOException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // add focusing in constructer
    public void conFacus() {
        username.addActionListener(e -> password.requestFocus());
        password.addActionListener(e -> rePasswordField.requestFocus());
        rePasswordField.addActionListener(e -> register());
    }
    
    public void register() {
        String userName;
        String passWord;
        String rePassword;
        boolean isCheckboxSelected;  

        userName = username.getText();
        passWord = new String(password.getPassword()); 
        rePassword = new String(rePasswordField.getPassword());
        isCheckboxSelected = checkbox.isSelected();
        
        if (userName.isEmpty() || passWord.isEmpty() || rePassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fields cannot be empty!");
            return;
        }
        
        if (!userName.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]{4,15}$")) {
            username.setText("");
            JOptionPane.showMessageDialog(null, "Username must be 4 to 15 characters long and contain only English letters and numbers, and common special characters!");
            return;
        }
        
        if (!passWord.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]{4,15}$")) {
            password.setText("");
            JOptionPane.showMessageDialog(null, "Password must be 4 to 15 characters long and can include English letters, numbers, and common special characters!");
            return;
        }

        if (!passWord.equals(rePassword)) {
            password.setText("");
            rePasswordField.setText("");
            JOptionPane.showMessageDialog(null, "Passwords do not match!");
            return;
        }

        if (!isCheckboxSelected) {
            JOptionPane.showMessageDialog(null, "You must agree to the terms and conditions!");
            return;
        }

        try (Connection connection = DBconnection.connect()) {
            
            String createTableQuery = "CREATE TABLE IF NOT EXISTS User (" +
                                      "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                      "user_name TEXT UNIQUE NOT NULL, " +
                                      "password TEXT NOT NULL)";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableQuery);
            }
            
            String checkUserQuery = "SELECT * FROM User WHERE user_name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery)) {
                checkStmt.setString(1, userName);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        username.setText("");
                        JOptionPane.showMessageDialog(null, "Username already exists. Please choose another!");
                        return;
                    }
                }
            }
            
            String insertQuery = "INSERT INTO User (user_name, password) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, userName);
                insertStmt.setString(2, passWord);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    Login D1 = new Login();
                    D1.setVisible(true);
                    this.dispose();
                    JOptionPane.showMessageDialog(null, "Registration successful! Log in with your username and password.");   
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed. Please try again!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    
    public void exitFromApplication() {
        int choice = JOptionPane.showOptionDialog(
            null,
            "Do you want to exit the application?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[] { "Yes", "No" },
            "No"
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            //
        }
    }
    
    // placeholder in constructor
    public void conplaceholderdef() {
        if ("".equals(username.getText())) {
            username.setText("  4 to 15 characters each for username and password");
            username.setForeground(java.awt.Color.decode("#A9A9A9"));
            username.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        labelPhoto = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        labelPassword = new javax.swing.JLabel();
        labelUsername = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        registerButton = new javax.swing.JButton();
        password = new javax.swing.JPasswordField();
        labelPasswordre = new javax.swing.JLabel();
        rePasswordField = new javax.swing.JPasswordField();
        checkbox = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        labelHeading = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(6, 26, 45));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(6, 26, 45));
        jPanel2.setPreferredSize(new java.awt.Dimension(350, 500));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel8.add(labelPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 470));

        exitButton.setBackground(new java.awt.Color(7, 23, 90));
        exitButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        exitButton.setForeground(new java.awt.Color(255, 255, 255));
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        jPanel8.add(exitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, 110, 40));

        jPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 470));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, -1, 490));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 850, 60));

        jPanel4.setBackground(new java.awt.Color(6, 26, 45));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(6, 26, 45));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelPassword.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        labelPassword.setForeground(new java.awt.Color(255, 255, 255));
        labelPassword.setText("Password");
        jPanel9.add(labelPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        labelUsername.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        labelUsername.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername.setText("Username");
        jPanel9.add(labelUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        username.setBackground(new java.awt.Color(255, 255, 255));
        username.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        username.setForeground(new java.awt.Color(68, 68, 68));
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameFocusLost(evt);
            }
        });
        jPanel9.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 370, 50));

        registerButton.setBackground(new java.awt.Color(7, 23, 90));
        registerButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        registerButton.setForeground(new java.awt.Color(255, 255, 255));
        registerButton.setText("Register");
        registerButton.setAlignmentY(1.0F);
        registerButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });
        jPanel9.add(registerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 370, 60));

        password.setBackground(new java.awt.Color(255, 255, 255));
        password.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        password.setForeground(new java.awt.Color(68, 68, 68));
        jPanel9.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 370, 50));

        labelPasswordre.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        labelPasswordre.setForeground(new java.awt.Color(255, 255, 255));
        labelPasswordre.setText("Re-enter Password");
        jPanel9.add(labelPasswordre, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, -1, -1));

        rePasswordField.setBackground(new java.awt.Color(255, 255, 255));
        rePasswordField.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        rePasswordField.setForeground(new java.awt.Color(68, 68, 68));
        jPanel9.add(rePasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 370, 50));

        checkbox.setBackground(new java.awt.Color(6, 26, 45));
        checkbox.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        checkbox.setForeground(new java.awt.Color(255, 255, 255));
        checkbox.setText("I agree, password recovery is unavailable.");
        jPanel9.add(checkbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, -1, -1));

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
        labelHeading.setFont(new java.awt.Font("Roboto Black", 1, 48)); // NOI18N
        labelHeading.setForeground(new java.awt.Color(255, 255, 255));
        labelHeading.setText("Register");
        labelHeading.setOpaque(true);
        jPanel7.add(labelHeading, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 220, 80));

        loginButton.setBackground(new java.awt.Color(7, 23, 90));
        loginButton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Login");
        loginButton.setAlignmentY(1.0F);
        loginButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        jPanel7.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 140, 60));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 470, 80));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 100));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 640));

        setSize(new java.awt.Dimension(850, 620));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        register();
    }//GEN-LAST:event_registerButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
       exitFromApplication();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        Login D1 = new Login();
        D1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusGained
        if ("  4 to 15 characters each for username and password".equals(username.getText())) {
            username.setText("");
            username.setForeground(java.awt.Color.decode("#061A2D"));
            username.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 36));
        }
    }//GEN-LAST:event_usernameFocusGained

    private void usernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusLost
        if ("".equals(username.getText())) {
            username.setText("  4 to 15 characters each for username and password");
            username.setForeground(java.awt.Color.decode("#A9A9A9"));  
            username.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        }
    }//GEN-LAST:event_usernameFocusLost

    
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
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Register().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkbox;
    private javax.swing.JButton exitButton;
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
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelPasswordre;
    private javax.swing.JLabel labelPhoto;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField password;
    private javax.swing.JPasswordField rePasswordField;
    private javax.swing.JButton registerButton;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
