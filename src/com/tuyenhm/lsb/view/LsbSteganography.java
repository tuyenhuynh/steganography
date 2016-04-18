/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.lsb.view;

import com.tuyenhm.lsb.algorithm.KDBAlgorithm;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author tuyenhuynh
 */
public class LsbSteganography {
    public static void main(String[] args) {
        
        KDBAlgorithm kA = new KDBAlgorithm();
        String message = "/" ; 
        try {
            BufferedImage image = ImageIO.read(new File("./src/lena_color.bmp")); 
            List<Point> coords = kA.hideMessage(message, image);
            
            for(int i = 0 ; i < coords.size()  ; ++i) {
                System.out.println(coords.get(i).x +": " + coords.get(i).y ); 
            }
            
            kA.extractMessage(image, coords);
        
        }catch(IOException ex) {
            ex.printStackTrace();
        }
//        
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                JFrame app = new SteganographyFrame(); 
//                app.setVisible(true);
//                app.setLocationRelativeTo(null);
//                app.setTitle("STEGANOGRAPHY"); 
//            }
//        });
        
    }
}
