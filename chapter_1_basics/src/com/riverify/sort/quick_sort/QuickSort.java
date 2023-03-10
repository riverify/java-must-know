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
        int p = paratition2(a, l, h); // p基准点的索引
        quick(a, l, p - 1);  // 左边分区的范围确定
        quick(a, p + 1, h);   // 右边分区的范围确定
    }


    /**
     * Lomuto单边快排分区代码
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


    /**
     * 双边循环快排
     */
    public static int paratition2(int[] a, int l, int h) {
        int pv = a[l];
        int i = l;
        int j = h;
        // 只要i小于j，循环一直进行
        while (i < j) {
            // j从右找比pv小的，当j停下的时候，指向的数是比基准点pv小的值
            while (i < j && a[j] > pv) {
                j--;
            }
            // i从左找比pv大的，当i停下的时候，指向的数是比基准点pv大的值
            while (i < j && a[i] <= pv) {
                i++;
            }
            // 交换i，j所指向的数
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        // 退出while循环的时候，i和j已经相等了，此时基准点与i或者j交换位置
        int temp = a[i];
        a[i] = a[l];
        a[l] = temp;

        System.out.println(Arrays.toString(a) + ", i = " + i + ", j = " + j);

        return i;
    }
}
