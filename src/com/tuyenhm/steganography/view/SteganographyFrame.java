/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuyenhm.steganography.view;

import com.tuyenhm.steganography.algorithm.MetricAnalyzier;
import com.tuyenhm.steganography.algorithm.KDBAlgorithm;
import com.tuyenhm.steganography.algorithm.LSBAlgorithm;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author tuyenhuynh
 */
public class SteganographyFrame extends JFrame{
    
    private MetricAnalyzier controller ; 
    private JButton btnOpen;
    private JButton btnAnalyze; 
    private JButton btnHide;
    private JButton btnRetrieve; 
    private JRadioButton btnKDB; 
    private JRadioButton btnLSB; 
    private ImagePanel originalImagePanel; 
    private JLabel messageLabel = new JLabel("Message"); 
    private JTextField messageField = new JTextField(); 
    
    
    private BufferedImage image ; 
        
    public SteganographyFrame() {
        
        initComponents(); 
        
        setMinimumSize(new Dimension(800, 500) );
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        
        File defaultFile = new File("./src/lena_color.bmp"); 
        image = loadImage(defaultFile);
        //controller.writeImage("./src/hiddenImage.bmp", image);
        
        originalImagePanel.drawImage(image); 
        
        //modifiedImagePanel.drawImage(image); 
        
        btnOpen.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //open file with openfile dialog
                JFileChooser fc = new JFileChooser(); 
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    System.out.println(file.getAbsolutePath()); 
                    image = loadImage( file);
                }
            }
        });
        
        btnAnalyze.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(); 
                File file1 = null, file2 = null; 
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file1 = fc.getSelectedFile();                    
                }
                returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file2 = fc.getSelectedFile();                    
                }
                if(file1 != null && file2 != null) {
                    String analysticalResult = MetricAnalyzier.calculateMetrics(file1.getAbsolutePath(), file2.getAbsolutePath()); 
                    JOptionPane.showMessageDialog(null, analysticalResult);
                }else {
                    JOptionPane.showMessageDialog(null, "Please specify 2 files to compare metric");
                }
            }
        });
        
        btnHide.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText(); 
                //hide image using controller
                if(image == null) {
                    JOptionPane.showMessageDialog(null, "Image not selected");
                }else if(message == null) {
                    JOptionPane.showMessageDialog(null, "Message is null");
                }else {
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showSaveDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        
                        if(btnLSB.isSelected()) {
                            boolean result = LSBAlgorithm.hideImage(image, message);
                            if(!result ) {
                            JOptionPane.showMessageDialog(null, "Cannot hide message");
                            }else {
                                writeImage( file, image);
                                JOptionPane.showMessageDialog(null, "Message is successfully hidden");
                            }
                        }else {
                            List<Point> points = KDBAlgorithm.hideMessage(message, image);
                            if(points != null) {
                                writeImage( file, image);
                                JOptionPane.showMessageDialog(null, "Message is successfully hidden");
                                writeKeys(file.getAbsolutePath()+".key", points); 
                            }else {
                                JOptionPane.showMessageDialog(null, "Cannot hide message");
                            }
                        }
                    }
                }
            }
        }); 
        
        btnRetrieve.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String message= "";   
                JFileChooser fc = new JFileChooser(); 
                BufferedImage imageToRetrieve = null;
                int returnVal = fc.showOpenDialog(null);
                File file = null;
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    imageToRetrieve = loadImage( file);
                }
                
                if(imageToRetrieve != null) {
                    if(btnLSB.isSelected()) {
                        message = LSBAlgorithm.retrieveMessage(imageToRetrieve);
                    }else {
                        List<Point> keys = readKeys(file.getAbsolutePath() + ".key");
                        message  = KDBAlgorithm.extractMessage(imageToRetrieve, keys);
                    }
                    JOptionPane.showMessageDialog(null, message, "Retrieved Message", 0);
                }
            }
        });
    } 
    
    private List<Point> readKeys (String keyFilename) {
        List<Point> points = new ArrayList<>(); 
        Path path =  Paths.get(keyFilename);
        List<Integer> values = new ArrayList<>(); 
        try (
            InputStream in = Files.newInputStream(path);
                BufferedReader reader =
              new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                values.add(Integer.parseInt(line));
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        for(int i = 0 ; i < values.size(); i+=2) {
            Point p = new Point(values.get(i), values.get(i+1));
            points.add(p);
        }
        return points; 
    }
    
    private void writeKeys(String keyFilename, List<Point> keys) {
        Path path = Paths.get(keyFilename);
        Charset charset = Charset.forName("US-ASCII");

        StringBuilder builder = new StringBuilder(); 
        for(int i = 0 ; i < keys.size() ; ++i) {
            builder.append(keys.get(i).x +"\n" + keys.get(i).y +"\n");
        }
        String s = builder.toString(); 
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
    
    private void initComponents() {
        btnOpen = new JButton("Open");
        btnAnalyze = new JButton("Analyze Image");
        btnHide = new JButton("Hide");
        btnRetrieve  = new JButton("Retrieve");
        btnLSB = new JRadioButton("Least Significan Bits");
        btnKDB = new JRadioButton("Cutter-Jordan-Bossen");
        messageLabel = new JLabel("Message");
        messageField = new JTextField(30); 
        
        ButtonGroup group = new ButtonGroup(); 
        group.add(btnLSB);
        group.add(btnKDB);
        btnLSB.setSelected(true);
        Dimension dimension = new Dimension(400, 400); 
        originalImagePanel = new  ImagePanel(dimension);
        
        GridBagLayout layout = new GridBagLayout(); 
        GridBagConstraints gc = new GridBagConstraints(); 
        
        setLayout(layout);
        
        gc.gridwidth = 3 ; 
        
        gc.gridx = 0 ; 
        gc.gridy = 0 ; 
        add(originalImagePanel, gc);
         
        gc.gridx = 0; 
        gc.gridy = 1; 
        
        add(btnLSB, gc);
        
        gc.gridx = 0; 
        gc.gridy = 2;
        add(btnKDB, gc);
        
        gc.gridx  = 0; 
        gc.gridy = 3 ;
        add(messageLabel, gc);
        
        gc.gridx = 1; 
        gc.gridy = 3; 
        add(messageField, gc); 
        
        gc.gridx = 0; 
        gc.gridy = 4; 
        add(btnOpen);
        
        gc.gridx = 1; 
        add(btnHide); 
        
        gc.gridx = 2; 
        add(btnRetrieve); 
        
        gc.gridx  = 3; 
        add(btnAnalyze); 
        
    }
   
    private BufferedImage loadImage(File file) {
        BufferedImage img ;
        try {
           System.out.println(file.getAbsoluteFile()); 
           img = ImageIO.read(file);
       } catch (IOException e) {
           e.printStackTrace();
           img = null; 
       }
        return img ; 
    }
    
    private void writeImage(File file, BufferedImage img){
        try{ 
            ImageIO.write(img, "bmp", file ); 
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
