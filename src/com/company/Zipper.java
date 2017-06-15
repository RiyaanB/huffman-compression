package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Zipper {
    public static void CompressFile(String x){
        String target;
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
            System.out.println("Compressed file: " + x);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(x));
            for(int i = 0; i < file.length; i += 8)
                bufferedOutputStream.write(toDecimal(file,i,i+8));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch(IOException e){
            System.out.println("File not found");
        }
    }
    private static Node insertSort(Node head, Node insert) {
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
    public static void DecompressFile(String x){
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
            System.out.println("Decompressed file: " + x);
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
            System.out.println("Exception");
        }
    }
    private static void traverse(Hashtable<Character,String> paths, Node node, String path){
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