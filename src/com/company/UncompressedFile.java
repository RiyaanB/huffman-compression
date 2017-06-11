package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class UncompressedFile {
    public UncompressedFile(String x) {
        String s = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(x));
            s = "";
            int c = br.read();
            while (c != -1) {
                s += (char) c;
                c = br.read();
            }
            br.close();
            x = x.substring(0, x.length() - 4) + ".txt";
        } catch (Exception e) {
            System.out.println("Couldn't open file");
        }
        if (s != null) {
            char[] data = s.toCharArray();
            boolean[] raw = charsToBinary(data);
            for (boolean b : raw)
                System.out.print(b ? '1' : '0');
            System.out.println();
            int size = toInt(raw, 0, 32);
            boolean[] encoded = new boolean[size];
            for (int i = 0; i < size; i++)
                encoded[i] = raw[32 + i];
            int c = 32 + size;
            Hashtable<Character, String> paths = new Hashtable<Character, String>();
            while (raw.length - c > 11) {
                char character = (char) toInt(raw, c, c + 8);
                c += 8;
                int lengthOfRepresentation = toInt(raw, c, c + 3);
                c += 3;
                String representation = "";
                for (int i = 0; i < lengthOfRepresentation; i++)
                    representation += raw[c++] ? '1' : '0';
                paths.put(character, representation);
            }
            System.out.println(paths);
            Node root = new Node(null, null);
            Enumeration<Character> e = paths.keys();
            while (e.hasMoreElements()) {
                char character = e.nextElement();
                String representation = paths.get(character);
                Node n = root;
                for (int i = 0; i < representation.length(); i++) {
                    if (i != representation.length() - 1) {
                        if (representation.charAt(i) == '0') {
                            if (n.left == null)
                                n.left = new Node(null, null);
                            n = n.left;
                        } else {
                            if (n.right == null)
                                n.right = new Node(null, null);
                            n = n.right;
                        }
                    } else {
                        if (representation.charAt(i) == '0')
                            n.left = new Node("" + character, 0);
                        else
                            n.right = new Node("" + character, 0);
                    }
                }
            }
            Node n = root;
            String result = "";
            for (int i = 0; i < encoded.length; i++) {
                if (n.isBranch) {
                    if (encoded[i])
                        n = n.right;
                    else
                        n = n.left;
                } else {
                    i--;
                    result += n.character;
                    n = root;
                }
            }
            result += n.character;
            System.out.println(result);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(x));
                bw.write(result.toCharArray(), 0, result.length());
                bw.flush();
                bw.close();
                System.out.println("Uncompressed the File");
            } catch (Exception exception) {
                System.out.println("Couldn't create file");
            }
        }
    }

    public static int toInt(boolean[] booleans, int start, int end) {
        int j = 0;
        for (; start < end; start++) {
            j += (booleans[start] ? 1 : 0) << end - 1 - start;
        }
        return j;
    }

    public static boolean[] toBinary(int d, int bits) {
        boolean[] b = new boolean[bits];
        long c = (long) d;
        for (int i = 0; i < bits; i++) {
            b[bits - i - 1] = !(c % 2 == 0);
            c = c >> 1;
        }
        return b;
    }

    public boolean[] charsToBinary(char[] chars) {
        boolean[] binary = new boolean[chars.length * 8];
        int loc = 0;
        for (char c : chars) {
            boolean[] val = toBinary((int) c, 8);
            for (int i = 0; i < 8; i++)
                binary[loc++] = val[i];
        }
        return binary;
    }
}
