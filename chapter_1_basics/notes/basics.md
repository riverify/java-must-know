# Java程序员必知系列

# 基础篇

> 基础篇要点：算法、数据结构、基础设计模式

## 二分查找

### 二分查找思路

前提：有序数组

- 循环执行二分查找
    1. 获取中间索引 `M = Floor( ( L + R ) / 2 )`；
    2. 中间索引的值 A[M] 与待搜索的值 T 进行比较
        - `A[M] == T` 表示找到，返回中间索引的值，结束循环
        - `A[M] > T`，中间值右侧其他元素都大于 T，去中间索引左侧寻找，设置 `R = M - 1`
        - `A[M] > T`，中间值左侧其他元素都小于 T，去中间索引右侧寻找，设置 `L = M + 1`

当 L > R时，表示没有找到，循环结束。
> *更形象的描述请参考：[animation/binary_search.html](https://htmlpreview.github.io/?https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/animation/binary_search.html)*

### 二分查找代码

```java
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
```

### 解决整数溢出问题

当 `L + R` 大于整数能存储的最大值的时候，便产生了整数溢出问题。
对于此问题，主要问题出现在( L + R ) / 2，我们可以对此表达式在数学层面做出改进，如：

`( L + R ) / 2` ⇒ `L / 2 + R / 2` ⇒ `L + ( - L / 2 + R / 2 )` ⇒ **`L + ( R - L ) / 2`**

```java
int m=l+(r-l)/2;
```

亦或者，使用无符号的右移运算代替除法，即：**`( L + R ) >>> 1`**

右移一位之后，确保符号位为0，即使 `L + R` 发生了溢出，也能回到正确的结果。

```java
int m=(l+r)>>>1;
```

**溢出情况演示**：

```java
public class IntegerOverflow {
    public static void main(String[] args) {
        int l = 0;
        int r = Integer.MAX_VALUE - 1;

        int m = (l + r) / 2;
        System.out.println(m);      // 1073741823

        // 假若target在中间值右侧
        l = m + 1;
        m = (l + r) / 2;
        System.out.println(m);      // -536870913  由于溢出导致的负数

        // 改进1: m 表达式
        l = m + 1;
        m = l + (r - l) / 2;
        System.out.println(m);

        // 改进2: 无符号的右移运算
        l = m + 1;
        m = (l + r) >>> 1;
        System.out.println(m);
    }
}
```

### 相关面试题

1. 有一个有序表为 `1, 5, 8, 11, 19, 22, 31, 35, 40, 45, 48, 49, 50`，当二分查找的值为`48`的结点时，查找成功需要比较的次数是多少次（京东实习生招聘）
   <font color=Green>4次</font>
2. 使用二分法在序列`1, 4, 6, 7, 15, 33, 39, 50, 64, 78, 75, 81, 89, 96`中查找元素81时，需要经过（<font color=Green>
   4</font>）次比较（美团点评校招）
3. 在已经的`128`个数组中二分查找一个数，需要比较的次数最多不超过多少次（北京易道博时社区）<font color=Green>log2 128 =
   7</font>

- 奇数二分取中间
- 偶数二分中间靠左
- 注意每次更新左右节点的时候会向中心靠拢一位

## 冒泡排序

> *更形象的描述请参考：[animation/bubble_sort.html](http://htmlpreview.github.io/?https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/animation/bubble_sort.html)*

### 冒泡排序思路

设数组的长度为N：

1. 比较前后相邻的两个数据，如果前面数据大于后面的数据，就将这两个数据交换；
2. 这样对数组的第0个数据到`N - 1`个数据进行一次遍历后，最大的一个数据就“沉”到数组第`N - 1`个位置；
3. `N = N - 1`，如果N不为0就重复前面二步，否则排序完成。

### 冒泡排序代码

**初步实现**：

```java
public class BubbleSort {
    public static void main(String[] args) {
        int[] array = {3, 54, 18, 23, 42, 27, 6, 58, 34};
        bubble(array);
        System.out.println(Arrays.toString(array));
    }

    public static void bubble(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
            System.out.println("第" + (i + 1) + "轮：" + Arrays.toString(a));
        }
    }
}
```

这便是初步的冒泡排序实现，但是仍然有优化的空间。

首先便是**减少比较次数**，对于内层循环，显然不需要在每一个外层循环，都在内部进行从头到尾的判断，因为随着外层循环的进行，在数组尾部的“沉”下的正确的排序会越来越多。

所以首先可以对内层循环条件进行改进，即 `j < a.length - 1 - i` 。

然后便是可以**进一步减少冒泡次数**，当某一外层循环没有发生内层循环的任何一次交换，那么可以认为该数组已经排序完毕，结束外层循环。

**bubble代码**：

```java
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
```

但这仍有优化空间，目前为止，冒泡排序的思路都是每次外层循环仅仅将一个数据“沉”入最后，即使有些时候会碰巧满足多个数据也正确地沉到了合适的地方，
但这毕竟是无心之举，程序没法做到对此的判断，以减少判断次数。这便是冒泡排序的可改进之处——我们可以记录每次循环最后一次发生交换时的索引，
在此索引之后的数据都是已经排序了的。下次循环的时候，便可以在那个索引的位置终止循环，**再次减少比较次数**。当一次循环之后这个索引是0，
即对应了上一种优化方法，内层没有发生任何一次交换，那么直接结束循环。

**bubble2代码**：

```java
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
```

对此进一步优化之后，**代码最终实现如下**：

```java
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
```

## 选择排序

> *更形象的描述请参考：[animation/selection_sort.html](https://htmlpreview.github.io/?https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/animation/insertion_sort.html)*

### 选择排序思路

选择排序(Selection sort)是一种简单直观的排序算法。

其基本思想是：首先在未排序的数列中找到最小(or 最大)
元素，然后将其存放到数列的起始位置；接着，再从剩余未排序的元素中继续寻找最小(or 最大)元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。

### 选择排序代码

```java
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
```

以上代码为了减少交换次数，每一轮都先找出最小的索引，在每一轮可以先找最小的索引，在每一轮循环结束后再交换元素。

### 与冒泡排序的比较

1. 二者平均复杂度都是`O(n^2)`
2. 选择排序一般快于冒泡排序，因为其交换次数少
3. 但如果集合有序度高，冒泡排序需要被优先考虑
4. 冒泡排序属于稳定排序算法，而选择排序属于不稳定排序

## 插入排序

> *更形象的描述请参考：[insertion_sort.html](https://htmlpreview.github.io/?https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/animation/insertion_sort.html)*

### 插入排序的基本思想

插入排序的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。插入排序在实现上，通常采用in-place排序
（即只需用到`O(1)`的额外空间的排序），因而在从后向前扫描过程中，需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。

例如我们有一组数字：`｛5，2，4，6，1，3｝`，我们要将这组数字从小到大进行排列。
我们从第二个数字开始，将其认为是新增加的数字，这样第二个数字只需与其左边的第一个数字比较后排好序；在第三个数字，认为前两个已经排好序的数字为手里整理好的牌，那么只需将第三个数字与前两个数字比较即可；以此类推，直到最后一个数字与前面的所有数字比较结束，插入排序完成。

以升序为例

1. 将数组分为两个区域，已排序区域和待排序区域，每一轮按顺序从未排序区域中取出第一个元素，插入到排序区域。且保证排序区域的顺序；
2. 重复以上步骤，直到整个数组有序。

### 插入排序代码实现

```java
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
            for (j = i - 1 ; j >= 0; j--) {
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
```

### 与选择排序比较

1. 二者平均时间复杂度都是`O(n^2)`；
2. 大部分情况下，插入都略优于选择和冒泡排序；
3. 有序集合插入排序的时间复杂度为`O(n)`；
4. 插入属于稳定排序算法，而选择属于不稳定排序。

### 插入排序的缺点

希尔排序是插入排序的一种，解决了插入排序中的一些缺点，比如插入排序在每次往前插入时只能将数据移动一位，效率比较低。当遇到较大数据在数组的前方时，
需要给这些数移动较多的次数，希尔排序应运而生。

> *请参考：[animation/shell_sort.html](https://htmlpreview.github.io/?https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/animation/shell_sort.html)*

**参考资料**

* https://en.wikipedia.org/wiki/Shellsort


## 快速排序

> *更形象的描述请参考：[animation/quick_sort.html](https://htmlpreview.github.io/?https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/animation/quick_sort.html)*

### 快速排序基本思想

1. 每轮排序选择一个基准点`（pivot）`进行分区；
    1. 让小于基准点的元素进入一个分区，大于基准点的元素进入另一个分区；
    2. 当分区完成时，基准点元素的位置就是其最终位置；
2. 在分区内重复以上过程，直至子分区元素个数少于等于1，这体现的是分治的思想。

### 实现方式

1. 单边循环快排（lomuto洛缪托分区方案）
    1. 选择最右元素作为基准点元素；
    2. j 指针负责找到比基准点小的元素，一旦找到了则与 `i` 进行交换；
    3. i 指针维护小于基准点元素的边界，也是每次交换的目标索引；
    4. 最后基准点与 `i` 交换，`i` 即为分区位置。

**单边循环快排的实现代码**

```java
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
```

2. 双边循环快排（并不完全等价于hoare霍尔分区方案）
    1. 选择最左元素作为基准点元素；
    2. j 指针负责从右向左找比基准点小的元素，i 指针负责从左向右找比基准点大的元素，一旦找到二者交换，直至 `i`，`j` 相交；
    3. 最后基准点与 `i`（此时 `i` 与 `j` 相等）交换，`i` 即为分区位置。

**双边循环快排的实现代码**

```java
public static int paratition2(int[]a,int l,int h){
        int pv=a[l];
        int i=l;
        int j=h;
        // 只要i小于j，循环一直进行
        while(i<j){
        // j从右找比pv小的，当j停下的时候，指向的数是比基准点pv小的值
        while(i<j &&a[j]>pv){
        j--;
        }
        // i从左找比pv大的，当i停下的时候，指向的数是比基准点pv大的值
        while(i<j &&a[i]<=pv){
        i++;
        }
        // 交换i，j所指向的数
        int temp=a[i];
        a[i]=a[j];
        a[j]=temp;
        }
        // 退出while循环的时候，i和j已经相等了，此时基准点与i或者j交换位置
        int temp=a[i];
        a[i]=a[l];
        a[l]=temp;

        System.out.println(Arrays.toString(a)+", i = "+i+", j = "+j);

        return i;
        }
```

以上双边循环快排代码有几个需要注意的点：

1. 在对`i`从左找比`pv`大的数的时候，其循环条件为`a[i] <= pv`，等于号是因为在第一次循环时，`a[i]`一定等于`pv`
   ，若不加等于的条件，会直接离开循环；
2. 内层循环一定要加`i < j`的判断，不加判断会导致内层循环对`i`或`j`的自增/减会出现`i`大于`j`的情况；
3. 内层的循环一定要从`j`的自减开始。仔细思考，因为当以`i`找到比`pv`大的值结束内部第一个循环时，会让`i`*
   *率先停留在一个大于`pv`的数**上，
   之后`j`开始从右向左寻找比`pv`小的值的时候，**最多只能停留在和`i`重合**的位置上，此时`l`与`i`或`j`所指向的数调换位置，
   会把`i`或`j`指向的这个大于`pv`的置换到基准点，这就导致了在该段数组中最左侧出现了一个大于`pv`的数，使得排序发生错乱。

### 快速排序的特点

- 平均时间复杂度为 O(nlog<font size=1>2</font> n)，最坏时间复杂度为 O(n^2)
- 数据量较大的时候，快速排序优势非常明显
- 属于不稳定排序，相同大小元素可能会被打乱顺序



