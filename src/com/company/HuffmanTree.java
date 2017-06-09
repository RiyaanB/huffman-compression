package com.company;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class HuffmanTree {
    String target;
    SortedList sl;
    public HuffmanTree(String s){
        Hashtable<Character,Integer> map = new Hashtable<Character,Integer>();
        for(int i = 0; i < s.length(); i++){
            Integer val = map.get(new Character(s.charAt(i)));
            if(val != null){
                map.put(s.charAt(i),val + 1);
            }else{
                map.put(s.charAt(i),1);
            }
        }
        Enumeration<Character> keys = map.keys();
        sl = new SortedList();
        while(keys.hasMoreElements()){
            char c = keys.nextElement();
            sl.add(map.get(c),c);
        }

    }
}
