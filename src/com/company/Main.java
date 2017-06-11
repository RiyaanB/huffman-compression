package com.company;

public class Main {
    public static void main(String[] args) {
//        HuffmanTree h = new HuffmanTree("Hello.txt");
//        h.write("Hello");
//        h.read("Hello");
        new CompressedFile("Hello.txt");
        boolean[] nums = CompressedFile.toBinary(64,8);
        for(boolean n:nums)
            System.out.print(n?'1':'0');
    }
}
