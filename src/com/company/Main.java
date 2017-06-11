package com.company;

public class Main {
    public static void main(String[] args) {
        HuffmanTree h = new HuffmanTree("AAABBCDDDD");
        h.write("Hello.cpt");
        h.read("Hello.cpt");
        System.out.print(72<<8);
    }
}
