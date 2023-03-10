package com.riverify.binary_search;

/**
 * FileName: IntegerOverflow.java
 * Date: 2023/3/10
 * Time: 11:17
 * Author: river
 */
public class IntegerOverflow {
    public static void main(String[] args) {
        int l = 0;
        int r = Integer.MAX_VALUE - 1;

        int m = (l + r) / 2;
        System.out.println(m);      // 1073741823

        // 假若target在中间值右侧
//        l = m + 1;
//        m = (l + r) / 2;
//        System.out.println(m);      // -536870913  由于溢出导致的负数

//
//        // 改进 m 表达式
//        l = m + 1;
//        m = l + ( r - l ) / 2;
//        System.out.println(m);

        // 改进2: 无符号的右移运算
        l = m + 1;
        m = (l + r) >>> 1;
        System.out.println(m);
    }
}
