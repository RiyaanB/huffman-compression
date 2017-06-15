package com.company;

public class Node {
    char character;
    int frequency;
    boolean isBranch;
    Node next;
    Node left;
    Node right;

    public Node(char c, int freq) {
        character = c;
        frequency = freq;
        isBranch = false;
    }

    public Node(Node l, Node r) {
        left = l;
        right = r;
        isBranch = true;
        frequency = l.frequency + (r == null ? 0 : r.frequency);
    }
    public Node(){
        isBranch = true;
    }
    public String toString() {
        return character+"";
    }
}