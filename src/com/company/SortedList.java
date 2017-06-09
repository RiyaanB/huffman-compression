package com.company;

public class SortedList {
    Node head;
    Node tail;
    public void add(int frequency, char c) {
        if(head == null){
            head = new Node(frequency,c);
            tail = head;
            return;
        }
        if(frequency > head.frequency){
            Node node = new Node(frequency,c);
            node.next = head;
            head.prev = node;
            head = node;
            return;
        }
        Node n = head;
        while(n.next != null){
            if(frequency > n.frequency){
                Node node = new Node(frequency,c);
                node.prev = n;
                node.next = n.next;
                n.next = node;
                if(node.next == null)
                    tail = node;
                else
                    node.next.prev = node;
                return;
            }
            n = n.next;
        }
        n.next = new Node(frequency,c);
        n.next.prev = n;
        tail = n.next;
    }

    public String toString() {
        Node n = head;
        String s = "";
        while (n != null) {
            s += n + ", ";
            n = n.next;
        }
        return s;
    }
    public Node pop(){
        if(head == null)
            return null;
        else if(head.next != null){
            Node n = tail;
            tail = tail.prev;
            tail.next = null;
            return n;
        }
        else{
            Node n = head;
            head = null;
            tail = null;
            return n;
        }
    }
}
