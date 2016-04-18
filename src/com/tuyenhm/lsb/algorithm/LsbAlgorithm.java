/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.lsb.algorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author tuyenhuynh
 */
public class LsbAlgorithm {
    
    
    public static void changeOneBit(BufferedImage image){
        Color pixel ; 
        boolean f = false ; 
        
        String message = "florence do is my love. Adeline has mercy. florence do is my love. Put the gun down. Steal my money. Stealflorence do is my love.  my car. Dont take florence do is my love. my man. Dont take my man. florence do is my love. Cuz you well florence do is my love. damn know what I would do. I got ten fingers to the sky, My back to the wall, my white flag high,\n Hair, lips, just like a gun,She's got silver bullets on her tongue He's deep under her spell,I'm screamin' out, but it just won't help"; 

        byte[] b = message.getBytes(); 
        int x = 0 ; 
        
        for(int i = 0 ; i < image.getWidth(); ++i) {
            
            for(int j = 0 ; j < image.getHeight() ; ++j) {
                if(x == b.length ) {
                    x = 0 ;
                }
                pixel = new Color(image.getRGB(i, j));
                
                int red = pixel.getRed();
                int green = pixel.getGreen() ; 
                int blue = pixel.getBlue(); 
                
                String m = convertByteTo8BitsString(b[x++]); 
                //byte twelve = 12 ; 
                //String m = convertByteTo8BitsString(twelve);  
                String redBits = convertIntTo8BitsString(red); 
                String greenBits = convertIntTo8BitsString(green); 
                String blueBits = convertIntTo8BitsString(blue); 
                
                redBits = new StringBuilder(redBits).replace(6, 8, m.substring(0, 2)).toString(); 
                
                greenBits = new StringBuilder(greenBits).replace(5, 8, m.substring(2, 5)).toString(); 
                blueBits = new StringBuilder(blueBits).replace(5, 8, m.substring(5, 8)).toString(); 
                
                Color modColor = new Color(Integer.parseInt(redBits, 2), 
                                        Integer.parseInt(greenBits, 2), 
                                        Integer.parseInt(blueBits, 2)
                                    ); 
                image.setRGB(x, j, modColor.getRGB());
                
            }
        }
        
    }
    
    private static String convertByteTo8BitsString(byte b) {
        String str = Integer.toBinaryString(b); 
        while(str.length() < 8) {
            str = new StringBuilder(str).insert(0, 0).toString(); 
        }
        return str ; 
    } 
    
    private static String convertIntTo8BitsString(int b) {
        String str = Integer.toBinaryString(b); 
        while(str.length() < 8) {
            str = new StringBuilder(str).insert(0, 0).toString(); 
        }
        return str ; 
    } 
}
