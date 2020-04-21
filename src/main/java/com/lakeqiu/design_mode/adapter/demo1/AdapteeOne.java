package com.lakeqiu.design_mode.adapter.demo1;

import java.util.concurrent.atomic.LongAdder;

/**
 * 功能，但不兼容与ITarget接口
 * @author lakeqiu
 */
public class AdapteeOne {
    public static void main(String[] args) {
        String s1 = new String("1");
        String s3 = s1.intern();
        String s2 = "1";
        System.out.println(s1 == s2);
        System.out.println(s2 == s3);
        System.out.println(s1 == s3);
        /*//第二种情况
        String s3 = new String("1") + new String("1");
        String s5 = s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);*/
    }
}
