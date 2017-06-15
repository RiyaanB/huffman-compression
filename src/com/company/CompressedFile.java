package com.company;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class CompressedFile {
    String target;
    public CompressedFile(String x){
        try {
            Hashtable<Character, Integer> frequencyTable = new Hashtable<Character, Integer>();
            BufferedReader br = new BufferedReader(new FileReader(x));
            target = "";
            int readVal = br.read();
            while (readVal != -1) {
                if (frequencyTable.containsKey((char) readVal)) {
                    frequencyTable.put((char) readVal, frequencyTable.get((char) readVal) + 1);
                } else {
                    frequencyTable.put((char) readVal, 1);
                }
                target += (char) readVal;
                readVal = br.read();
            }
            x = x.substring(0, x.length() - 4) + ".tin";
            Node head = null;
            Enumeration<Character> e = frequencyTable.keys();
            while (e.hasMoreElements()) {
                char c = e.nextElement();
                Node insert = new Node(c, frequencyTable.get(c));
                head = insertSort(head, insert);
            }
            Node branch = null;
            while (head != null) {
                Node n1 = head;
                Node n2 = head.next;
                head = head.next.next;
                branch = new Node(n1, n2);
                insertSort(head, branch);
            }
            head = branch;
            Hashtable<Character, String> paths = new Hashtable<Character, String>();
            traverse(paths, head, "");
            Enumeration<Character> pathList = paths.keys();
            int dictionaryLength = 0;
            while (pathList.hasMoreElements()) {
                char c = pathList.nextElement();
                dictionaryLength += 16 + 4 + paths.get(c).length();
            }
            int encodedLength = 0;
            for (int i = 0; i < target.length(); i++) {
                encodedLength += paths.get(target.charAt(i)).length();
            }
            int extra = (16 - ((encodedLength + dictionaryLength) % 16))%16;
            boolean[] file = new boolean[32 + encodedLength + dictionaryLength + extra];
            boolean[] header = toBinary(encodedLength, 32);
            for (int i = 0; i < 32; i++) {
                file[i] = header[i];
            }
            int loc = 32;
            for(int i = 0; i < target.length(); i++){
                String path = paths.get(target.charAt(i));
                for(int ch = 0; ch < path.length(); ch++){
                    file[loc++] = path.charAt(ch) == '1';
                }
            }
            e = paths.keys();
            loc = 32 + encodedLength;
            while (e.hasMoreElements()){
                char c = e.nextElement();
                boolean[] character = toBinary(c, 16);
                for(int i = 0; i < 16; i++){
                    file[loc++] = character[i];
                }
                String rep = paths.get(c);
                boolean[] lengthOfRepresentation = toBinary(rep.length()-1,4);
                for(int i = 0; i < 4; i++){
                    file[loc++] = lengthOfRepresentation[i];
                }
                boolean[] representation = new boolean[rep.length()];
                for(int i = 0; i < representation.length; i++) {
                    file[loc++] = rep.charAt(i) == '1';
                }
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(x));
            for(int i = 0; i < file.length; i += 8)
                bufferedOutputStream.write(toDecimal(file,i,i+8));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("File not found");
        }
    }
    private Node insertSort(Node head, Node insert) {
        if (head == null)
            head = new Node(insert.character, insert.frequency);
        else {
            if (insert.frequency < head.frequency) {
                insert.next = head;
                return insert;
            } else {
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
    private void traverse(Hashtable<Character,String> paths, Node node, String path){
        if(node != null){
            if(node.isBranch){
                traverse(paths, node.left, path + "0");
                traverse(paths, node.right, path + "1");
            }
            else
                paths.put(node.character,path);
        }
    }
    public static boolean[] toBinary(int d, int bits) {
        boolean[] b = new boolean[bits];
        int c = (int) d;
        for (int i = 0; i < bits; i++) {
            b[bits - i - 1] = !(c % 2 == 0);
            c = c >> 1;
        }
        return b;
    }

    public static char toDecimal(boolean[] vals, int start, int end){
        char j = 0;
        for (int i = start; i < end; i++) {
            if (vals[i])
                j += 1 << (end) - 1 - i;
        }
        return j;
    }
}