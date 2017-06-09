package com.company;

public class Node {
    Node next;
    Node prev;
    int frequency;
    char c;
    public Node(){}
    public Node(int f, char ch) {
        frequency = f;
        c = ch;
    }
    public String toString(){
        return "(" + c + "," + frequency + ")";
    }
}