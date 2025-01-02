//@author MadeeshaSK

package me.madeeshask.budgetlog;

import me.madeeshask.budgetlog.views.Login;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true); 
            }
        });
    }
}
