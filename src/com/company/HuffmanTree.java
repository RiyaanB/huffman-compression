package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class HuffmanTree {
    String compressed;
    String target;
    Hashtable<Character,String> h;
    public HuffmanTree(String x){
        target = x;
        //Counting Frequencies
        Leaf head = null;
        for(int i = 0; i < x.length(); i++){
            if(head == null){
                head = new Leaf(x.charAt(i));
            }
            else {
                Leaf n = head;
                while(n.next != null){
                    if(n.c == x.charAt(i))
                        break;
                    n = n.next;
                }
                if(n.c == x.charAt(i)){
                    n.freq++;
                }
                else{
                    n.next = new Leaf(x.charAt(i));
                }
            }
        }
        //Sorting by Frequencies
        Leaf n = head;
        boolean changed = true;
        while(changed){
            n = head;
            while(n.next != null){
                changed = false;
                if(n.freq > n.next.freq){
                    char tc = n.c;
                    int tf = n.freq;
                    n.c = n.next.c;
                    n.freq = n.next.freq;
                    n.next.c = tc;
                    n.next.freq = tf;
                    changed = true;
                    break;
                }
                n = n.next;
            }
        }
        //Creating tree
        Branch c = new Branch();
        c.left = head;
        head = head.next;
        c.right = head;
        head = head.next;
        while(head != null){
            Branch t = c;
            c = new Branch();
            c.left = t;
            c.right = head;
            head = head.next;
        }
        h = new Hashtable<Character,String>();
        //Getting hashed character values
        traverse(h, c, "");
    }
    public void traverse(Hashtable<Character,String> h, Node parent, String s){
        if(parent.isLeaf){
            Leaf o = (Leaf) parent;
            h.put(o.c, s);
            return;
        }
        Branch b = (Branch) parent;
        traverse(h, b.left, s + "0");
        traverse(h, b.right, s + "1");
    }
    public static boolean[] charToBooleanArray(char c){
        int i = (int) c;
        boolean[] b = new boolean[8];
        for(int i2 = 0; i2 < 8; i2++){
            b[7-i2] = !(i%2 == 0);
            i = i >> 1;
        }
        return b;
    }
    public static boolean[] shortintToBooleanArray(int c){
        boolean[] b = new boolean[3];
        for(int i = 0; i < 3; i++){
            b[2-i] = !(c%2 == 0);
            c = c >> 1;
        }
        return b;
    }
    public static boolean[] stringToBooleanArray(String c){
        boolean[] result = new boolean[c.length()];
        for(int i = 0; i < c.length(); i++){
            result[i] = c.charAt(i) == '1';
        }
        return result;
    }
    public boolean[] dictionaryBinary(){
        boolean[] dictionary = new boolean[Short.MAX_VALUE];
        int current = 0;
        Enumeration<Character> e = h.keys();
        while(e.hasMoreElements()){
            char currentChar = e.nextElement();
            boolean[] binaryAscii = charToBooleanArray(currentChar);
            //Character
            for(int i = 0; i < 8; i++){
                dictionary[current++] = binaryAscii[i];
            }
            String charRepresentation = h.get(currentChar);
            boolean[] lengthOfRepresentation = shortintToBooleanArray(charRepresentation.length());
            //Length of character code
            for(int i = 0; i < 3; i++){
                dictionary[current++] = lengthOfRepresentation[i];
            }
            boolean[] representation = stringToBooleanArray(charRepresentation);
            //Character code
            for(int i = 0; i < charRepresentation.length(); i++){
                dictionary[current++] = representation[i];
            }
        }
        boolean[] result = new boolean[current];
        for(int i = 0; i < result.length; i++)
            result[i] = dictionary[i];
        return result;
    }
    public boolean[] fileBinary(){
        int count = 0;
        for(int i = 0; i < target.length(); i++)
            count += h.get(target.charAt(i)).length();
        boolean[] file = new boolean[count];
        count = 0;
        for(int i = 0; i < target.length(); i++){
            String encoded = h.get(target.charAt(i));
            for(int ch = 0; ch < encoded.length(); ch++){
                file[count++] = encoded.charAt(ch) == '1';
            }
        }
        return file;
    }
    public void write(String x){
        boolean[] fileToWrite = fileBinary();
        boolean[] size = new boolean[32];
        int c = fileToWrite.length;
        for(int i = 0; i < 32; i++){
            size[31-i] = !(c%2 == 0);
            c = c >> 1;
        }
        boolean[] dictionaryToWrite = dictionaryBinary();
        int total = size.length + fileToWrite.length + dictionaryToWrite.length;
        int pending = ((total) % 8);
        System.out.println("Total "+total);
        boolean[] result = new boolean[total + pending];

        for(int i = 0; i < size.length; i++)
            result[i] = size[i];
        for(int i = 0; i < fileToWrite.length; i++)
            result[i+size.length] = fileToWrite[i];
        for(int i = 0; i < dictionaryToWrite.length; i++)
            result[i + size.length + fileToWrite.length] = dictionaryToWrite[i];
        for(boolean b:result)
            System.out.print(b?'1':'0');
        System.out.println();
        char[] bytesToWrite = booleanArrayToByteArray(result);
        for(char b:bytesToWrite)
            System.out.print(b + " ");
        System.out.println("\n" + result.length);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(x));
            bw.write(bytesToWrite,0,bytesToWrite.length);
            bw.flush();
        } catch (Exception e){
        }
    }
    public char[] booleanArrayToByteArray(boolean[] bool){
        char[] bytes = new char[bool.length/8];
        for(int i = 0; i < bytes.length; i++){
            char temp = (char)((bool[(8*i)]?1<<7:0) + (bool[(8*i)+1]?1<<6:0) + (bool[(8*i)+2]?1<<5:0) + (bool[(8*i)+3]?1<<4:0) + (bool[(8*i)+4]?1<<3:0) + (bool[(8*i)+5]?1<<2:0) + (bool[(8*i)+6]?1<<1:0) + (bool[(8*i)+7]?1:0));
            bytes[i] = temp;
        }
        return bytes;
    }

    public void read(String x){
        System.out.println("Starting");
        ArrayList<Character> al = new ArrayList<Character>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(x));
            Character r = null;
            do{
                r = (char)br.read();
                al.add(r);
            }while(r != 65535);
        } catch (Exception e){
        }
        char[] chars = new char[al.size()];
        Iterator<Character> iterator = al.iterator();
        int i = 0;
        while(iterator.hasNext())
            chars[i++] = iterator.next();
        iterator = null;
        al = null;
        int size = ((int)chars[3]) + (((int)chars[2])<<8) + (((int)chars[1])<<16 + (((int)chars[0])<<24));
        boolean[] file = new boolean[size];
        for(i = 4; i < (size/8)+4; i++){
            int bits = (int)chars[i];
            for(int loc = 0; loc < 8; loc++){
                file[(7-loc)+(8*(i-4))] = !(bits%2 == 0);
                bits = bits >> 1;
            }
            System.out.println("loop");
        }
        int extra = (int)chars[4+(size/8)];
        boolean[] extraBits = new boolean[8];
        for(i = 0; i < 8; i++){
            extraBits[7-i] = !(extra%2 == 0);
            extra = extra >> 1;
        }
        for(i = 0; i < size%8; i++)
            file[((size/8)*8)+i] = extraBits[i];
        for(boolean b:file)
            System.out.print(b?'1':'0');
        System.out.println("\nOver " + size);
    }
}
