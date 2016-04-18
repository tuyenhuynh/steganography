/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.lsb.view;

import com.tuyenhm.lsb.algorithm.Controller;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author tuyenhuynh
 */
public class SteganographyFrame extends JFrame{
    private Controller controller ; 
    private JButton btnOpen;
    private JButton btnSave; 
    private JButton btnHide; 
    private ImagePanel originalImagePanel; 
    private ImagePanel modifiedImagePanel; 
    
    private BufferedImage image ; 
        
    public SteganographyFrame() {
        
        
        initComponents(); 
        
        setMinimumSize(new Dimension(800, 500) );
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        
        controller = new Controller(); 
        
        image = controller.loadImage("/lena_color.bmp");
        //LsbAlgorithm.changeOneBit(image);
        //controller.writeImage("./src/hiddenImage.bmp", image);
        
        //originalImagePanel.drawImage(image); 
        
        //modifiedImagePanel.drawImage(image); 
        
        controller.calculateMetrics("./src/lena_color.bmp","./src/hiddenImage.bmp"); 
        
        btnOpen.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //open file with openfile dialog
                image = controller.loadImage( "lena_color.bmp");
            }
        });
        
        btnSave.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //save file
            }
        });
        
        btnHide.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //hide image using controller
            }
        }); 
        
        
        
    } 
            
    private void initComponents() {
        btnOpen = new JButton("Open");
        btnSave = new JButton("Save");
        btnHide = new JButton("Hide");
        Dimension dimension = new Dimension(400, 400); 
        originalImagePanel = new  ImagePanel(dimension); 
        dimension = new Dimension(400, 400);
        modifiedImagePanel = new  ImagePanel(dimension); 
        GridBagLayout layout = new GridBagLayout(); 
        GridBagConstraints gc = new GridBagConstraints(); 
        
        setLayout(layout); 
        
        gc.gridwidth = 3 ; 
        gc.gridheight = 2 ; 
        
        gc.gridx = 0; 
        gc.gridy = 0 ; 
        add(originalImagePanel, gc);
        
        gc.gridx = 1; 
        gc.gridy = 0; 
        add(modifiedImagePanel, gc);
        
        gc.gridx = 0 ; 
        gc.gridy = 1; 
        add(btnOpen, gc);
        
        gc.gridx = 1 ; 
        add(btnHide, gc);
        
        gc.gridx = 2 ; 
        add(btnSave, gc); 
        
    }
    
}
