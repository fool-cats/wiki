package com.foolcats.wiki.req;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
//        System.out.println(list.get);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
        System.out.println("==========");
        list.remove(list.size() - 1);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
    }
}
