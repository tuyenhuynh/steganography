/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.steganography.algorithm;

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
    
    public static List<Point> hideMessage(String message, BufferedImage image) {
   
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
                bStar = new int[width][height]; //modified blues 
        double[][] y = calculateBrightness(r, g, b, width, height); //brightness 
        //initial blue array
        for(int i = 0 ; i < width ; ++i) {
            for(int j = 0 ; j < height ;++j) {
                bStar[i][j] = b[i][j]; 
            }
        }
        
        String bitArray = messageToBitArray(message);
        
        List<Point> coords  = new ArrayList<>(); 
         
        for(int i = 0  ; i < bitArray.length() ; ++i) {
            Point p = randomPoint(coords, width, height); 
            bStar[p.x][p.y] = changeBlueValue(b[p.x][p.y], y[p.x][p.y], bitArray.charAt(i)); 
            coords.add(p); 
        } 
        
        //write blue 
        rgbExtractor.changeBlue(image, bStar);
        return coords; 
    }
    
    private static Point randomPoint(List<Point> currentPoints, int width, int height) {
        boolean f = true ; 
        Random random  = new Random (); 
        int x = 0, y = 0; 
        while (f) {
            x = Math.abs(random.nextInt())%(width - 4) +SIGMA;
            y = Math.abs(random.nextInt())%(height - 4) +SIGMA; 
            f = checkExisting(currentPoints, new Point(x, y)); 
        }
        Point p = new Point(x, y); 
        return p ;
    }
    
    private static boolean checkExisting(List<Point> points, Point point) {
        boolean f = false ; 
        for(int i = 0 ; !f && i < points.size() ;++i) {
            if(points.get(i).x == point.x && points.get(i).y == point.y ){
                return f = true ; 
            }
        }
        return f ; 
    }
    
    public static String extractMessage(BufferedImage image, List<Point> coords) {
        
        rgbExtractor.extractRGB(image);
        int[][] blues = rgbExtractor.getB(); 
        StringBuilder builder = new StringBuilder(); 
        builder.setLength(0);
        StringBuilder s = new StringBuilder(); 
    
        int count =0;
        int bit ; 
        
        for(int i = 0 ; i < coords.size() ; ++i) {
            Point p = coords.get(i); 
            bit = retrieveBit(blues, p.x, p.y); 
            s.append(bit); 
            count ++; 
            if(count == 8 ) {
                int character = Integer.parseInt(s.toString(), 2); 
                builder.append((char)character); 
                count = 0; 
                s.setLength(0);
            }
        }
        return builder.toString(); 
    }
    
    private static int retrieveBit(int[][]b, int x, int y) {
        //assumpt that SIGMA = 2 
        double value = 0; 
        value = b[x-2][y] +b[x-1][y] + b[x+1][y] +b[x+2][y]
                + b[x][y-2] +b[x][y-1] +b[x][y+1] +b[x][y+2];

        value = value/8;
        double delta = b[x][y] - value; 
        
        if(delta == 0 ){
            if(b[x][y] ==0){
                delta = -0.5; 
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
 
    private static String messageToBitArray(String message) {
        byte[] bytes = message.getBytes();
        int length = message.length() ; 
        StringBuilder builder = new StringBuilder(); 
        for(int i = 0 ;i < length; ++i) {
            String bitStr = StringBitConverter.convertByteTo8BitsString(bytes[i]); 
            builder.append(bitStr); 
        }
        String result = builder.toString(); 
        return result ; 
    }
    
    private static int changeBlueValue(int bxy, double yxy, char bit) {
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
    
    private static double[][] calculateBrightness(int[][] r, int[][]g, int[][]b, int width, int height){
        double[][] y = new double[width][height]; 
        for(int i = 0 ; i < width ; ++i) {
            for(int j = 0 ; j < height ; ++j) {
                y[i][j] = (int)(0.298*r[i][j] + 0.586*g[i][j] + 0.114*b[i][j]); 
            }
        }
        return y;
    }
    
}
