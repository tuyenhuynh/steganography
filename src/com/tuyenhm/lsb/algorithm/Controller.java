/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.lsb.algorithm;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ObjectiveFidelity;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tuyenhuynh
 */
public class Controller {
    
    public void calculateMetrics(String firstFile, String secondFile) {
        FastBitmap firstBitmap = new FastBitmap(firstFile); 
        FastBitmap secondBitmap = new FastBitmap(secondFile); 
        if(!firstBitmap.isGrayscale()) firstBitmap.toGrayscale();
        if(!secondBitmap.isGrayscale()) secondBitmap.toGrayscale();
        
        ObjectiveFidelity of = new ObjectiveFidelity(firstBitmap, secondBitmap); 
        // Error total
        int error = of.getTotalError();
        System.out.println("Error total: " + error ); 
      
        //Mean Square Error
        double mse = of.getMSE();
        System.out.println("Mean Square Error: " + mse ); 
  
        //Signal Noise Ratio
        double snr = of.getSNR();
        System.out.println("Signal Noise Ratio: " + snr ); 
        
        //Peak Signal Noise Ratio
        double psnr = of.getPSNR();
        System.out.println("Peak Signal Noise Ratio: " + psnr );
        
    } 
    
    public BufferedImage loadImage(String imagePath) {
        BufferedImage img ;
        try {
           System.out.println(imagePath); 
           img = ImageIO.read(getClass().getResourceAsStream(imagePath));
       } catch (IOException e) {
           e.printStackTrace();
           img = null; 
       }
        return img ; 
    }
    
    public void writeImage(String imagePath, BufferedImage img){
        try{
            File file = new File(imagePath); 
            ImageIO.write(img, "bmp", file ); 
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
