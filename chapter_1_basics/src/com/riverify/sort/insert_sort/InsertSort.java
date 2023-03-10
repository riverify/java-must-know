package com.riverify.sort.insert_sort;

import java.util.Arrays;

/**
 * FileName: InsertSort.java
 * Date: 2023/3/10
 * Time: 14:37
 * Author: river
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] a = {9, 3, 7, 2, 5, 6, 8, 1, 4};
        insertSort(a);
    }

    public static void insertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {    // 插入排序的第一个需要被插入的值是索引为1的数
            // 临时变量t，待插入的值
            int t = a[i];
            int j;
            for (j = i - 1; j >= 0; j--) {
                if (a[j] > t) {
                    a[j + 1] = a[j];
                } else {
                    // 出现小于t的数，跳出循环，减少比较的次数（优化）

                    break;
                }
            }
            a[j + 1] = t;   // 插入这个临时变量
            System.out.println("第" + (i) + "轮：" + Arrays.toString(a));
        }
    }
}
