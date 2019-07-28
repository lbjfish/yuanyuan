package com.lee;

import java.util.*;

public class Testabc {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.push(11);
        list.push(22);
        list.push(33);
        System.out.println(list);
        list.pop();
        System.out.println(list);

        Stack st = new Stack();
        st.push(55);
        st.push(66);
        st.push(77);
        System.out.println(st);
        st.pop();
        System.out.println(st);

        int a = 1348;
        StringBuilder sb = new StringBuilder();
        ba(a,sb);
        System.out.println(sb.reverse().toString());

        noxh(a);

        int b = 75;
        StringBuilder sb2 = new StringBuilder();
        er(b,sb2);
        System.out.println(sb2.reverse().toString());
    }

    public static void ba(int a, StringBuilder sb){
        if(a >= 8){
            ba(a/8,sb.append(a%8));
        }else{
            sb.append(a);
        }
    }

    public static void er(int a, StringBuilder sb){
        if(a >= 2){
            er(a/2,sb.append(a%2));
        }else{
            sb.append(a);
        }
    }

    public static void noxh(int a){
        StringBuilder sb3 = new StringBuilder();
        int val = a;
        int ys = a;
        if(ys >= 8){
            ys = val % 8;
            sb3.append(ys);
        }
        while(val >= 8){
            val = val / 8;
            ys = val % 8;
            sb3.append(ys);
        }
       // sb3.append(val);
        System.out.println(sb3.toString());
    }
}
