package com.company;

import java.util.Hashtable;

public class HuffmanTree {
    public HuffmanTree(String x){
        //Counting Frequencies
        Leaf head = null;
        for(int i = 0; i < x.length(); i++){
            if(head == null){
                head = new Leaf(x.charAt(i));
            }
            else {
                Leaf n = head;
                while(n.next != null){
                    if(n.c == x.charAt(i))
                        break;
                    n = n.next;
                }
                if(n.c == x.charAt(i)){
                    n.freq++;
                }
                else{
                    n.next = new Leaf(x.charAt(i));
                }
            }
        }
        //Sorting by Frequencies
        Leaf n = head;
        boolean changed = true;
        while(changed){
            n = head;
            while(n.next != null){
                changed = false;
                if(n.freq > n.next.freq){
                    char tc = n.c;
                    int tf = n.freq;
                    n.c = n.next.c;
                    n.freq = n.next.freq;
                    n.next.c = tc;
                    n.next.freq = tf;
                    changed = true;
                    break;
                }
                n = n.next;
            }
        }
        //Creating tree
        Branch c = new Branch();
        c.left = head;
        head = head.next;
        c.right = head;
        head = head.next;
        while(head != null){
            Branch t = c;
            c = new Branch();
            c.left = t;
            c.right = head;
            head = head.next;
        }
        Hashtable<Character,String> h = new Hashtable<Character,String>();
        //Getting hashed character values
        traverse(h, c, "");
        String compressed = "";
        //Creating new String from character values
        for(int i = 0; i < x.length(); i++){
            compressed += h.get(x.charAt(i));
        }
        System.out.println(compressed);
    }
    public void traverse(Hashtable<Character,String> h, Node parent, String s){
        if(parent.isLeaf){
            Leaf o = (Leaf) parent;
            h.put(o.c, s);
            return;
        }
        Branch b = (Branch) parent;
        traverse(h, b.left, s + "0");
        traverse(h, b.right, s + "1");
    }
}
