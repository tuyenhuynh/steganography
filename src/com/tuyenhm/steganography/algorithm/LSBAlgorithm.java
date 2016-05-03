/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.steganography.algorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author tuyenhuynh
 */
public class LSBAlgorithm {
    
    public static boolean hideImage(BufferedImage image, String message){
        Color pixel ; 
        
        StringBuilder builder  = new StringBuilder(message); 
        builder.append("/"); 
        message = builder.toString(); 
        
        byte[] b = message.getBytes(); 
        int x = 0 ; 
        int width = image.getWidth(); 
        int height = image.getHeight(); 
        if(width * height < message.length()) {
            return false; 
        }
        
        boolean f = false ;
        
        for(int i = 0 ; !f && i < width; ++i) {
            
            for(int j = 0 ; !f && j < height ; ++j) {
                
                pixel = new Color(image.getRGB(i, j));
                
                int red = pixel.getRed();
                int green = pixel.getGreen() ; 
                int blue = pixel.getBlue(); 
                
                String m = StringBitConverter.convertByteTo8BitsString(b[x++]); 
                if(x == b.length ) {
                    f = true ;
                }
                
                String redBits = StringBitConverter.convertIntTo8BitsString(red); 
                String greenBits = StringBitConverter.convertIntTo8BitsString(green); 
                String blueBits = StringBitConverter.convertIntTo8BitsString(blue); 
                
                redBits = new StringBuilder(redBits).replace(6, 8, m.substring(0, 2)).toString(); 
                greenBits = new StringBuilder(greenBits).replace(5, 8, m.substring(2, 5)).toString(); 
                blueBits = new StringBuilder(blueBits).replace(5, 8, m.substring(5, 8)).toString(); 
                
                Color modColor = new Color(Integer.parseInt(redBits, 2), 
                                        Integer.parseInt(greenBits, 2), 
                                        Integer.parseInt(blueBits, 2)
                                    ); 
                image.setRGB(i, j, modColor.getRGB());
            }
        }
        return true; 
    }
    
    public static String retrieveMessage(BufferedImage image) {
        StringBuilder builder = new StringBuilder(); 
        int width = image.getWidth(); 
        int height = image.getHeight();
        boolean cont = true;  
        Color pixel; 
        for(int i = 0 ; cont && i < width ; ++i) {
            for(int j = 0 ; cont && j < height ; ++j) {
                pixel = new Color(image.getRGB(i, j));
                
                int red = pixel.getRed();
                int green = pixel.getGreen() ; 
                int blue = pixel.getBlue(); 
                
                String redBits = StringBitConverter.convertIntTo8BitsString(red); 
                String greenBits = StringBitConverter.convertIntTo8BitsString(green); 
                String blueBits = StringBitConverter.convertIntTo8BitsString(blue); 
                
                String hightBits = redBits.substring(6, 8); 
                String mediumBits = greenBits.substring(5, 8); 
                String lowBits = blueBits.substring(5, 8); 
                
                StringBuilder charBuilder = new StringBuilder(); 
                charBuilder.append(hightBits)
                        .append(mediumBits)
                        .append(lowBits); 
                
                int character = Integer.parseInt(charBuilder.toString(), 2); 
                if(character == 47){
                    cont = false; 
                }else {
                    builder.append((char)character);
                }
            }
        }
        return builder.toString();
    }
    
}
