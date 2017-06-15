package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class DecompressedFile {
    String target;


    public DecompressedFile(String x){
        try{
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(x));
            int c = bufferedInputStream.read();
            ArrayList<Integer> bytes = new ArrayList<Integer>();
            while(c != -1){
                bytes.add(c);
                c = bufferedInputStream.read();
            }
            boolean[] file = new boolean[bytes.size()*8];
            for(int i = 0; i < file.length/8; i++){
                boolean[] b = toBinary(bytes.get(i),8);
                for(int j = 0; j < 8; j++)
                    file[(8*i)+j] = b[j];
            }

            int encodedSize = toDecimal(file,0,32);
            int dictionaryLocation = encodedSize + 32;
            Hashtable<Character,String> paths = new Hashtable<Character,String>();
            while (file.length - dictionaryLocation > 17) {
                char character = (char) toDecimal(file, dictionaryLocation, dictionaryLocation + 16);
                dictionaryLocation += 16;
                int sizeOfPath = toDecimal(file, dictionaryLocation, dictionaryLocation + 4) + 1;
                dictionaryLocation += 4;
                String path = "";
                for (int i = dictionaryLocation; i < dictionaryLocation + sizeOfPath; i++) {
                    path += file[i] ? '1' : '0';
                }
                dictionaryLocation += sizeOfPath;
                paths.put(character, path);
            }
            Node root = new Node();
            Enumeration<Character> e = paths.keys();
            while (e.hasMoreElements()) {
                char character = e.nextElement();
                String representation = paths.get(character);
                Node n = root;
                for (int i = 0; i < representation.length(); i++) {
                    if (i != representation.length() - 1) {
                        if (representation.charAt(i) == '0') {
                            if (n.left == null)
                                n.left = new Node();
                            n = n.left;
                        } else {
                            if (n.right == null)
                                n.right = new Node();
                            n = n.right;
                        }
                    } else {
                        if (representation.charAt(i) == '0')
                            n.left = new Node(character, 0);
                        else
                            n.right = new Node(character, 0);
                    }
                }
            }
            Node n = root;
            BufferedWriter bw = new BufferedWriter(new FileWriter(x.substring(0,x.length()-4) + ".txt"));
            for(int i = 32; i < 32 + encodedSize + 1; i++){
                if(!n.isBranch){
                    bw.write(n.character);
                    n = root;
                    i--;
                }
                else
                    n = file[i] ? n.right : n.left;
            }
            bw.flush();
            bw.close();
        } catch (Exception e){
        }
    }
    public static boolean[] toBinary(int d, int bits) {
        boolean[] b = new boolean[bits];
        int c = d;
        for (int i = bits-1; i >=0 ; i--) {
            b[i]= c%2==1;
            c = c >> 1;
        }
        return b;
    }
    public static int toDecimal(boolean[] vals, int start, int end){
        int j = 0;
        for (int i = start; i < end; i++) {
            if (vals[i])
                j += 1 << (end) - 1 - i;
        }
        return j;
    }
}