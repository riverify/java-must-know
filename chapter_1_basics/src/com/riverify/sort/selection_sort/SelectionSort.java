package com.riverify.sort.selection_sort;

import java.util.Arrays;

/**
 * FileName: SelectionSort.java
 * Date: 2023/3/10
 * Time: 13:49
 * Author: river
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] a = {5, 3, 7, 2, 1, 9, 8, 4};
        selection(a);
    }


    /*
     * 选择排序(Selection sort)是一种简单直观的排序算法。
     * 其基本思想是：首先在未排序的数列中找到最小(or最大)元素，然后将其存放到数列的起始位置；
     * 接着，再从剩余未排序的元素中继续寻找最小(or最大)元素，然后放到已排序序列的末尾。
     * 以此类推，直到所有元素均排序完毕。
     */
    public static void selection(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            // s代表最小的元素索引
            int s = i;
            for (int j = s + 1; j < a.length; j++) {
                if (a[s] > a[j]) {
                    s = j;
                }
            }
            if (s != i) {
                int temp = a[i];
                a[i] = a[s];
                a[s] = temp;
            }
            System.out.println("第" + (i + 1) + "轮：" + Arrays.toString(a));
        }
    }
}
