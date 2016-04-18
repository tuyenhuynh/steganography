/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.lsb.algorithm;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author tuyenhuynh
 */
public class KDBAlgorithm {
    
    private static final double LAMDA = 0.1 ; 
    
    private static final int SIGMA = 2; 
    
    private static final RGBExtractor rgbExtractor = new RGBExtractor(); 
    
    public List<Point> hideMessage(String message, BufferedImage image) {
   
        int width = image.getWidth() ; 
        int height = image.getHeight();
        //image is not large enough to hide message
        if(message.length() * 8 > width* height ) {
            return null; 
        }
        //extract rgb 
        rgbExtractor.extractRGB(image);
        //array of image's blue
        int[][] r = rgbExtractor.getG(), 
                g = rgbExtractor.getG(),
                b = rgbExtractor.getB(),  
                y = calculateBrightness(r, g, b, width, height), //brightness 
                bStar = new int[width][height]; //modified blues 
        
        int currentBit = 0; 
        String bitArray = messageToBitArray(message);
        
        List<Point> coords  = new ArrayList<>(); 
         
        for(int i = 0  ; i < bitArray.length() ; ++i) {
            Point p = randomPoint(coords, width, height); 
            bStar[p.x][p.y] = changeBlueValue(b[p.x][p.y], y[p.x][p.y], bitArray.charAt(currentBit)); 
            coords.add(p); 
        } 
        
        //write blue 
        rgbExtractor.changeBlue(image, bStar);
        return coords; 
    }
    
    private Point randomPoint(List<Point> currentPoints, int width, int height) {
        boolean f = true ; 
        Random random  = new Random (); 
        int x = 0, y = 0; 
        while (f) {
            x = Math.abs(random.nextInt())%(width-4) +SIGMA;
            y = Math.abs(random.nextInt())%(height - 4) +SIGMA; 
            f = checkExisting(currentPoints, new Point(x, y)); 
        }
        Point p = new Point(x, y); 
        return p ;
    }
    
    boolean checkExisting(List<Point> points, Point point) {
        boolean f = false ; 
        for(int i = 0 ; !f && i < points.size() ;++i) {
            if(points.get(i).x == point.x && points.get(i).y == point.y ){
                return f = true ; 
            }
        }
        return f ; 
    }
    
    public void extractMessage(BufferedImage image, List<Point> coords) {
        
        rgbExtractor.extractRGB(image);
        int[][] blues = rgbExtractor.getB(); 
        StringBuilder builder = new StringBuilder(); 
        builder.setLength(0);
        StringBuilder s = new StringBuilder(); 
        
    
        int count =0;
        int bit ; 
        
        for(int i = 0 ; i < coords.size() ; ++i) {
            Point p = coords.get(i); 
            bit = retrieveBit(blues, p.x, p.y, 2); 
            s.append(bit); 
            count ++; 
            if(count ==8 ) {
                int character = Integer.parseInt(s.toString(), 2); 
                builder.append((char)character); 
                if(character == 47){
                    //stop 
                    String str = builder.toString(); 
                    System.out.println("Damn cool");
                    System.out.println(str);
                    return ; 
                } 
                count = 0; 
                s.setLength(0);
            }
        }
    }
    
    private int retrieveBit(int[][]b, int x, int y, int sigma) {
        double value = 0; 
        for(int delta = 1 ; delta <= sigma ;++delta) {
            value += b[x][y+delta] + b[x][y-delta] +b[x+delta][y] + b[x-delta][y]; 
        }
        value = value/(4*sigma);
        double delta = b[x][y] - value; 
        if(delta == 0 ){
            if(b[x][y] ==0){
                delta = -0.5 ; 
            }
            if(b[x][y] == 255){
                delta = 0.5 ; 
            }
        } 
        if(delta > 0) {
            return 1 ; 
        }
        return 0;
    }
    
    public void extractMessage(BufferedImage image) {
        rgbExtractor.extractRGB(image);
        int[][] blues = rgbExtractor.getB(); 
        int width = image.getWidth(); 
        int height  = image.getHeight();
        StringBuilder builder = new StringBuilder(); 
        builder.setLength(0);
        StringBuilder s = new StringBuilder(); 
        for(int i = 2 ; i < width-2 ;++i) {
            for(int j = 2 ; j < height-2 ; ++j) {
                bit = (retrieveBit(blues, i, j, 2));
                s.append(bit); 
                ++count ; 
                if(count ==8) {
                    int character = Integer.parseInt(s.toString(), 2); 
                    builder.append((char)character); 
                    if(character == 47){
                        //stop 
                        String str = builder.toString(); 
                        System.out.println("Damn cool" );
                        System.out.println(str);
                        return ; 
                    } 
                    count = 0; 
                    s.setLength(0);
                }
            }
        }
    }
    
    
    private String messageToBitArray(String message) {
        byte[] bytes = message.getBytes();
        int length = message.length() ; 
        StringBuilder builder = new StringBuilder(); 
        for(int i = 0 ;i < length; ++i) {
            String bitStr = convertByteTo8BitsString(bytes[i]); 
            builder.append(bitStr); 
        }
        String result = builder.toString(); 
        return result ; 
    }
    
    private static String convertByteTo8BitsString(byte b) {
        String str = Integer.toBinaryString(b); 
        while(str.length() < 8) {
            str = new StringBuilder(str).insert(0, 0).toString(); 
        }
        return str ; 
    }
    
    private int changeBlueValue(int bxy, int yxy, char bit) {
        int result ; 
        if(bit == '1') {
            result  =  (int)(bxy + LAMDA* yxy);
            if(result > 255) {
                result = 255 ; 
            }
        }else {
            //m =0
            result = (int)(bxy - LAMDA* yxy);
            if(result < 0 ) {
                result = 0 ; 
            }
        }
        return result ; 
    }
    
    private int[][] calculateBrightness(int[][] r, int[][]g, int[][]b, int width, int height){
        int[][] y = new int[width][height]; 
        for(int i = 0 ; i < width ; ++i) {
            for(int j = 0 ; j < height ; ++j) {
                y[i][j] = (int)(0.298*r[i][j] + 0.586*g[i][j] + 0.114*b[i][j]); 
            }
        }
        return y;
    }
    
}
