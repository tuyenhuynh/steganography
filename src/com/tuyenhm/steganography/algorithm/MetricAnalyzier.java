/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.steganography.algorithm;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ObjectiveFidelity;

/**
 *
 * @author tuyenhuynh
 */
public class MetricAnalyzier {
    
    public static String calculateMetrics(String firstFile, String secondFile) {
        FastBitmap firstBitmap = new FastBitmap(firstFile); 
        FastBitmap secondBitmap = new FastBitmap(secondFile); 
        if(!firstBitmap.isGrayscale()) firstBitmap.toGrayscale();
        if(!secondBitmap.isGrayscale()) secondBitmap.toGrayscale();
        
        ObjectiveFidelity of = new ObjectiveFidelity(firstBitmap, secondBitmap); 
        // Error total
        
        StringBuilder builder = new StringBuilder(); 
        
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
        builder.append("Error total: " + error + ". ")
                .append("Mean Square Error: " + mse + ". ")
                .append("Signal Noise Ratio: " + snr  +". ")
                .append("Peak Signal Noise Ratio: " + psnr +".");
        return builder.toString();
    } 
    
}
