package com.company;

public class HuffmanTree {
    CharacterSet l;
    public HuffmanTree(String s){
        l = new CharacterSet();
        for(int i = 0; i < s.length(); i++){
            l.add(s.charAt(i));
        }
    }
    public String toString(){
        return l.toString();
    }
}