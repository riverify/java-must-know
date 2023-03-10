package com.riverify.sort.quick_sort;

import java.util.Arrays;

/**
 * FileName: QuickSort.java
 * Date: 2023/3/10
 * Time: 16:24
 * Author: river
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] a = {9, 3, 7, 2, 5, 6, 8, 1, 4};
        quick(a, 0, a.length - 1);
    }


    // 递归调用
    public static void quick(int[] a, int l, int h) {
        if (l >= h) {
            return; // 递归结束点
        }
        int p = partition(a, l, h); // p基准点的索引
        quick(a, l, p - 1);  // 左边分区的范围确定
        quick(a, p + 1, h);   // 右边分区的范围确定
    }

    /**
     * Lomuto单边快排分区代码
     *
     * @param a array
     * @param l low
     * @param h high
     * @return 基准点所在的正确索引，以确定下一轮的分区边界
     */
    public static int partition(int[] a, int l, int h) {
        int pv = a[h];
        int i = l;
        int j;
        for (j = l; j < h; j++) {
            if (a[j] < pv) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
            }
        }
        // 基准点与i交换位置
        int temp = a[i];
        a[i] = a[h];
        a[h] = temp;
        System.out.println(Arrays.toString(a) + ", i = " + i + ", j = " + j);

        return i;
    }
}
