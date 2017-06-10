package com.company;

public abstract class Node{
    int freq;
    boolean isLeaf;
}

class Branch extends Node{
    Node left;
    Node right;
    int freq;
    public Branch(){
        isLeaf = false;
    }
}

class Leaf extends Node{
    Leaf next;
    char c;
    int freq;
    public Leaf(char ch){
        freq = 1;
        c = ch;
        isLeaf = true;
    }
}
