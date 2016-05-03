/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.steganography.algorithm;

/**
 *
 * @author tuyenhuynh
 */
public class StringBitConverter {
    
    public static String convertByteTo8BitsString(byte b) {
        String str = Integer.toBinaryString(b); 
        while(str.length() < 8) {
            str = new StringBuilder(str).insert(0, 0).toString(); 
        }
        return str ; 
    } 
    
    public static String convertIntTo8BitsString(int b) {
        String str = Integer.toBinaryString(b); 
        while(str.length() < 8) {
            str = new StringBuilder(str).insert(0, 0).toString(); 
        }
        return str ; 
    } 
}
