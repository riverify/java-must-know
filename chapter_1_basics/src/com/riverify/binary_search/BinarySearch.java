package com.riverify.binary_search;

/**
 * FileName: BinarySearch.java
 * Date: 2023/3/10
 * Time: 10:57
 * Author: river
 */
public class BinarySearch {
    public static void main(String[] args) {
        int[] array = {1, 5, 7, 12, 15, 31, 35, 44, 46, 51, 53, 58, 62, 80, 100};
        int target = 58;
        int index = binarySearch(array, target);
        System.out.println(index);
    }

    /*
     * 二分查找原理
     * 前提：有序数组
     * 定义左边界L，右边界R，确定搜索范围，然后：
     *
     * - 循环执行二分查找
     *     1. 获取中间索引 M = Floor( ( L + R ) / 2 )
     *     2. 中间索引的值 A[M] 与待搜索的值 T 进行比较
     *         - A[M] == T 表示找到，返回中间索引的值，结束循环
     *         - A[M] > T，中间值右侧其他元素都大于 T，去中间索引左侧寻找，设置 R = M - 1
     *         - A[M] > T，中间值左侧其他元素都小于 T，去中间索引右侧寻找，设置 L = M + 1
     *
     * 当 L > R时，表示没有找到，循环结束，返回 -1
     */
    public static int binarySearch(int[] a, int t) {
        int l = 0, r = a.length - 1;
        int m;

        while (l <= r) {
            m = (l + r) / 2;
            if (a[m] == t) {
                return m;
            } else if (a[m] > t) {
                r = m - 1;
            } else if (a[m] < t) {
                l = m + 1;
            }
        }
        return -1;
    }

}

