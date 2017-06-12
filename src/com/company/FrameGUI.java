package com.company;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class FrameGUI extends JFrame implements ActionListener{
    JFileChooser fileChooser;
    JButton browse;
    JButton zip;
    Container con;
    JLabel selectFiles;
    JList<File> selectedFiles;
    DefaultListModel<File> selectedFileList;
    JButton remove;
    JButton unzip;
    FrameGUI(){
        this.setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        con = getContentPane();
        setSize(600, 600);

        selectFiles = new JLabel("Select files to compress: ");
        con.add(selectFiles);
        selectFiles.setSize(400,100);
        selectFiles.setLocation(100,20);

        browse = new JButton("Browse");
        con.add(browse);
        browse.addActionListener(this);
        browse.setSize(100,100);
        browse.setLocation(450,20);

        selectedFileList = new DefaultListModel<File>();

        selectedFiles = new JList<File>(selectedFileList);
        selectedFiles.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        selectedFiles.setLayoutOrientation(JList.VERTICAL);
        selectedFiles.setVisibleRowCount(-1);
        selectedFiles.setLocation(50,150);
        selectedFiles.setSize(500,300);
        JScrollPane listScroller = new JScrollPane(selectedFiles);
        con.add(selectedFiles);

        remove = new JButton("Remove");
        con.add(remove);
        remove.addActionListener(this);
        remove.setSize(500,20);
        remove.setLocation(50,450);

        zip = new JButton("Compress");
        con.add(zip);
        zip.addActionListener(this);
        zip.setSize(150,90);
        zip.setLocation(100,475);

        unzip = new JButton("Decompress");
        con.add(unzip);
        unzip.addActionListener(this);
        unzip.setLocation(350,475);
        unzip.setSize(150,90);
    }

    public static void main(String arg[]) {
        FrameGUI window = new FrameGUI();
        window.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == browse){
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files or Compressed Tin files","txt","tin"));
            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                selectedFileList.addElement(file);
            }
        }
        else if(e.getSource() == zip){
            Enumeration<File> en = selectedFileList.elements();
            while (en.hasMoreElements()){
                new CompressedFile(en.nextElement().getAbsolutePath());
            }
            selectedFileList.clear();
        }
        else if(e.getSource() == unzip){
            Enumeration<File> en = selectedFileList.elements();
            while (en.hasMoreElements()){
                new UncompressedFile(en.nextElement().getAbsolutePath());
            }
            selectedFileList.clear();
        }
        else if(e.getSource() == remove){
            int index = selectedFiles.getSelectedIndex();
            if(selectedFileList.getSize() != 0){
                selectedFileList.remove(index);
            }
        }
    }
}
