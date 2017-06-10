package com.company;

public class CharacterSet {
    class Node{
        char c;
        int freq;
        Node next;
        public Node(){
            freq = 1;
        }
        public Node(char ch){
            c = ch;
            freq = 1;
        }
        public String toString(){
            return "(" + c + " : " + freq + ")";
        }
    }
    Node head;
    public void add(char c){
        if(head == null)
            head = new Node(c);
        else{
            Node node = head;
            while(node.next != null){
                if(node.c == c)
                    break;
                node = node.next;
            }
            if(node.c != c)
                node.next = new Node(c);
            else
                node.freq++;
        }
    }
    public String toString(){
        String s = "";
        Node node = head;
        while(node != null){
            s += node + ", ";
            node = node.next;
        }
        return s;
    }
}
