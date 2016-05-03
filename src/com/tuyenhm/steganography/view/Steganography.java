/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.steganography.view;

import com.tuyenhm.steganography.algorithm.KDBAlgorithm;
import com.tuyenhm.steganography.algorithm.LSBAlgorithm;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author tuyenhuynh
 */
public class Steganography {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame app = new SteganographyFrame(); 
                app.setVisible(true);
                app.setLocationRelativeTo(null);
                app.setTitle("STEGANOGRAPHY"); 
            }
        });
    }
}
