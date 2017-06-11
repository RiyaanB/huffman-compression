package com.company;

public class Main {
    public static void main(String[] args) {
        HuffmanTree h = new HuffmanTree("AAABBCDDDD");
        h.write("Hello");
        h.read("Hello");
    }
}
