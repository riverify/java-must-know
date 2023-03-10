package com.riverify.sort.bubble_sort;

import java.util.Arrays;

/**
 * FileName: BubbleSort.java
 * Date: 2023/3/10
 * Time: 12:06
 * Author: river
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] array = {3, 54, 18, 23, 42, 27, 6, 58, 59};
//        int[] array = {1, 2, 3, 4, 5, 6, 7};
        // 读者可以自行切换调用的方法，对比发生比较的次数
//        bubble(array);
        bubble_final(array);
        System.out.println(Arrays.toString(array));
    }

    public static void bubble(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            boolean isSwapped = false;   // 第二步优化 是否发生了交换
            for (int j = 0; j < a.length - 1 - i; j++) {    // 第一步优化 j < a.length - 1 ==> j < a.length - 1 - i
                System.out.println("发生比较");
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    isSwapped = true;
                }
            }
            // 若没有发生交换，直接退出外层循环，结束排序
            if (!isSwapped) {
                break;
            }
            System.out.println("第" + (i + 1) + "轮：" + Arrays.toString(a));
        }
    }

    public static void bubble2(int[] a) {
        int n = a.length - 1;   // 内部第一次循环需要完全循环
        for (int i = 0; i < a.length - 1; i++) {
            int last = 0;   // 最后一次发生交换的索引位置
            for (int j = 0; j < n; j++) {
                System.out.println("发生比较");
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    last = j;   // 每次只要if判断之后，说明发生了交换，更新这个last
                }
            }
            n = last;   // 一轮循环结束，last之后的数已经确保是排序的了，故下次循环的条件为截至到last索引的位置
            if (n == 0) {   // 若没有发生交换，直接退出外层循环，结束排序
                break;
            }
            System.out.println("第" + (i + 1) + "轮：" + Arrays.toString(a));
        }
    }

    public static void bubble_final(int[] a) {
        int n = a.length - 1;
        while (n > 0) {
            int last = 0;
            for (int j = 0; j < n; j++) {
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    last = j; // update last swapped index
                }
            }
            n = last;
        }
    }

}
