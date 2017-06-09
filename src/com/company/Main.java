package com.company;

public class Main {
    public static void main(String[] args) {
        SortedList sl = new SortedList();
        sl.add(10,'a');
        sl.add(20,'b');
        sl.add(30,'c');
        sl.add(40,'d');
        System.out.println(sl);
        System.out.println(sl.pop());
        System.out.println(sl);
        System.out.println(sl.pop());
        System.out.println(sl);
        System.out.println(sl.pop());
        System.out.println(sl);
        System.out.println(sl.pop());
        System.out.println(sl);
        System.out.println("Ended");
        System.out.println(sl);
        System.out.println("Ended");
    }
}
