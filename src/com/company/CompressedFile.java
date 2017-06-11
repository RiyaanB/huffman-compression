package com.company;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class CompressedFile {
    String target;
    public CompressedFile(String x){
        try{
            BufferedReader br = new BufferedReader(new FileReader(x));
            target = "";
            int readVal = br.read();
            while(readVal != -1){
                target += (char)readVal;
                readVal = br.read();
            }
        } catch (Exception e){
            System.out.println("File doesn't exist");
        }
        if(target != null){
            Hashtable<Character,Integer> h = new Hashtable<Character,Integer>();
            for(int i = 0; i < target.length(); i++){
                if(h.containsKey(target.charAt(i)))
                    h.put(target.charAt(i),h.get(target.charAt(i)) + 1);
                else
                    h.put(target.charAt(i),1);
            }
            System.out.println(h);
            Node head = null;
            Enumeration<Character> e = h.keys();
            while (e.hasMoreElements()){
                char c = e.nextElement();
                Node insert = new Node(c+"",h.get(c));
                insert.isBranch = false;
                head = insertSort(head, insert);
            }
            while(head.next.next != null) {
                Node n1 = head;
                head = head.next;
                Node n2 = head;
                head = head.next;
                Node branch = new Node(n1,n2);
                branch.frequency = n1.frequency + n2.frequency;
                branch.character = n1.character + "" + n2.character;
                insertSort(head, branch);
            }
            Node fin = new Node(head,head.next);
            fin.isBranch = true;
            fin.character = fin.left.character + fin.right.character;
            Hashtable<Character,String> paths = new Hashtable<Character,String>();
            traverse(paths, fin, "");
            System.out.println(paths);
            int dictionarySize = 0;
            e = paths.keys();
            while(e.hasMoreElements()){
                dictionarySize += 11 + paths.get(e.nextElement()).length();
            }
            boolean[] dictionary = new boolean[dictionarySize];
            e = paths.keys();
            int count = 0;
            while(e.hasMoreElements()){
                char c = e.nextElement();
                boolean[] charBinary = toBinary((int)c,8);
                for(int i = 0; i < 8; i++)
                    dictionary[count++] = charBinary[i];
                boolean[] lengthBinary = toBinary(paths.get(c).length(), 3);
                for(int i = 0; i < 3; i++)
                    dictionary[count++] =  lengthBinary[i];
                String path = paths.get(c);
                for(int i = 0; i < path.length(); i++)
                    dictionary[count++] = path.charAt(i) == '1';
            }
            String encoded = "";
            for(int i = 0; i < target.length(); i++)
                encoded += paths.get(target.charAt(i));
            int dataSize = encoded.length();
            int total = 32 + dictionarySize + dataSize;
            boolean[] file = new boolean[total + (8 - total%8)];
            boolean[] size = toBinary(dataSize,32);
            for(int i = 0; i < size.length; i++)
                file[i] = size[i];
            for(int i = 0; i < dataSize; i++)
                file[i+32] = encoded.charAt(i) == '1';
            for(int i = 0; i < dictionarySize; i++)
                file[i+32+dataSize] = dictionary[i];
            for(boolean b:file)
                System.out.print(b?'1':'0');
            System.out.println();
        }
    }
    public static boolean[] toBinary(int d, int bits){
        boolean[] b = new boolean[bits];
        long c = (long)d;
        for(int i = 0; i < bits; i++){
            b[bits-i-1] = !(c%2 == 0);
            c = c >> 1;
        }
        return b;
    }
    private void traverse(Hashtable<Character,String> paths, Node node, String path){
        if(node == null)
            return;
        if(!node.isBranch){
            paths.put(node.character.charAt(0),path);
        }
        traverse(paths, node.left,path + '0');
        traverse(paths,node.right,path + '1');
    }
    private Node insertSort(Node head, Node insert){
        if(head == null)
            head = new Node(insert.character,insert.frequency);
        else {
            if(insert.frequency < head.frequency){
                insert.next = head;
                return insert;
            }
            else {
                Node node = head;
                while (node.next != null) {
                    if (insert.frequency < node.next.frequency) {
                        insert.next = node.next;
                        node.next = insert;
                        return head;
                    }
                    node = node.next;
                }
                node.next = insert;
            }
        }
        return head;
    }
}
