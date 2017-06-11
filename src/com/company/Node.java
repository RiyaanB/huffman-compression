package com.company;

public class Node{
    String character;
    int frequency;
    boolean isBranch;
    Node next;
    Node left;
    Node right;
    public Node(String c,int freq){
        character = c;
        frequency = freq;
    }
    public Node(Node l, Node r){
        left = l;
        right = r;
        isBranch = true;
    }
    public String toString(){
        return character;
    }
}