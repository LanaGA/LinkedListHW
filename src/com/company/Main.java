package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        MyLinkedList<Integer> mouse = new MyLinkedList();
        mouse.add(10);
        mouse.add(15);
        System.out.println(Arrays.toString(mouse.toArray()));
        System.out.print(mouse.toString());
        mouse.remove((Integer) 10);
        System.out.println(mouse.toString());
        mouse.add(10);
        mouse.add(15);
        mouse.set(1, 2);
        for(Object iter: mouse){
            System.out.println(iter);
        }
    }
}
