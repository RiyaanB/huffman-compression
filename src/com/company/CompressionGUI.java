package com.company;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompressionGUI extends Applet implements ActionListener{
    Button compress;
    Button decompress;
    TextField path;
    public void init(){
        setSize(500,280);
        compress = new Button("Compress");
        decompress = new Button("Decompress");
        compress.setPreferredSize(new Dimension(245,245));
        decompress.setPreferredSize(new Dimension(245,245));
        add(compress);
        add(decompress);
        compress.addActionListener(this);
        decompress.addActionListener(this);
        path = new TextField("                                                            ");
        add(path);
        path.setText("");
    }
    public void start(){

    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == compress){
            System.out.println("Compress");
            String p = path.getText();
            System.out.println(p);
            try {
                new CompressedFile(p);
            }catch (Exception x){
                System.out.println("Failed");
            }
        }
        else if(e.getSource() == decompress){
            System.out.println("Decompress");
            String p = path.getText();
            System.out.println(p);
            try {
                new UncompressedFile(p);
            }catch (Exception x){
                System.out.println("Failed");
            }
        }
    }

}
