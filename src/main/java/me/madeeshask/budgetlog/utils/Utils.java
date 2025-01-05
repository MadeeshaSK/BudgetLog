//@author MadeeshaSk

package me.madeeshask.budgetlog.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Utils {
    
    // Scale image
    public static void scaleImage(String imagePath, JLabel label) {
        if (label.getWidth() > 0 && label.getHeight() > 0) {
            ImageIcon icon = new ImageIcon(Utils.class.getClassLoader().getResource(imagePath));
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = icon.getImage();
                Image imgScale = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(imgScale);
                label.setIcon(scaledIcon);
            } else {
                System.out.println("Image not found at path: " + imagePath);
            }
        } else {
            System.out.println("Invalid dimensions for label: Width = " + label.getWidth() + ", Height = " + label.getHeight());
        }
    }

    
     // set window properties
    public static void setWindowProperties(JFrame frame, String title, String iconPath, boolean resizable) {
        frame.setTitle(title);
        ImageIcon icon = new ImageIcon(frame.getClass().getClassLoader().getResource(iconPath));
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            frame.setIconImage(icon.getImage());
        } else {
            System.out.println("Icon not found at path: " + iconPath);
        }
        frame.setResizable(resizable);
    }
    
    // set fonts to labels
    public static void setLabelFont(JLabel label, String fontPath, int fontStyle, int fontSize) throws IOException {
        InputStream fontStream = Utils.class.getClassLoader().getResourceAsStream(fontPath);
        if (fontStream != null) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                font = font.deriveFont(fontStyle, fontSize); 
                label.setFont(font);
            } catch (FontFormatException e) {
                System.out.println("Error creating font: " + e.getMessage());
            }
        } else {
            System.out.println("Font not found at path: " + fontPath);
        }
    }

    
}
