/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.lsb.view;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author tuyenhuynh
 */
public class ImagePanel extends JPanel{
    
    private JLabel imageLabel = new JLabel(); 
    
    public ImagePanel(Dimension dimension){
        add(imageLabel); 
        
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension); 
    }
    
    public void drawImage(Image image){
        imageLabel.setIcon(new ImageIcon(image));
    }
}
