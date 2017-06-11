package com.company;

public class Main {
    public static void main(String[] args) {
        HuffmanTree h = new HuffmanTree("Hello.txt");
        h.write("Hello");
        h.read("Hello");
    }
}
