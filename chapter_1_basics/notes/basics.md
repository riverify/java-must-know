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

当 `L > R` 时，表示没有找到，循环结束。
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
对于此问题，主要问题出现在`( L + R ) / 2`，我们可以对此表达式在数学层面做出改进，如：

`( L + R ) / 2` 

⇒ `L / 2 + R / 2` 

⇒ `L + ( - L / 2 + R / 2 )` 

⇒ **`L + ( R - L ) / 2`**

```java
int m = l + (r - l) / 2;
```

亦或者，使用无符号的右移运算代替除法，即：**`( L + R ) >>> 1`**

右移一位之后，确保符号位为0，即使 `L + R` 发生了溢出，也能回到正确的结果。

```java
int m = (l + r) >>> 1;
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
2. 使用二分法在序列`1, 4, 6, 7, 15, 33, 39, 50, 64, 78, 75, 81, 89, 96`中查找元素81时，需要经过（<font color=Green>4</font>）次比较（美团点评校招）
3. 在已经的`128`个数组中二分查找一个数，需要比较的次数最多不超过多少次（北京易道博时社区）<font color=Green>log2 128 = 7</font>

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
```
以上双边循环快排代码有几个需要注意的点：
1. 在对`i`从左找比`pv`大的数的时候，其循环条件为`a[i] <= pv`，等于号是因为在第一次循环时，`a[i]`一定等于`pv`，若不加等于的条件，会直接离开循环；
2. 内层循环一定要加`i < j`的判断，不加判断会导致内层循环对`i`或`j`的自增/减会出现`i`大于`j`的情况；
3. 内层的循环一定要从`j`的自减开始。仔细思考，因为当以`i`找到比`pv`大的值结束内部第一个循环时，会让`i`**率先停留在一个大于`pv`的数**上，
之后`j`开始从右向左寻找比`pv`小的值的时候，**最多只能停留在和`i`重合**的位置上，此时`l`与`i`或`j`所指向的数调换位置，
会把`i`或`j`指向的这个大于`pv`的置换到基准点，这就导致了在该段数组中最左侧出现了一个大于`pv`的数，使得排序发生错乱。

### 快速排序的特点
- 平均时间复杂度为 O(nlog<font size=1>2</font> n)，最坏时间复杂度为 O(n^2)
- 数据量较大的时候，快速排序优势非常明显
- 属于不稳定排序，相同大小元素可能会被打乱顺序


## List 集合

### 目标
- 掌握 ArrayList 的扩容机制
- 掌握 Iterator 的 fail-fast、fail-safe 机制
- 能够说清楚 LinkedList 对比 ArrayList 的区别，并重视纠正部分错误的认知

### ArrayList 扩容机制
1. `ArrayList()` 会使用长度为零的数组
2. `ArrayList(int initialCapacity)` 会使用指定容量`initialCapacity`的数组
3. `public ArrayList(Collection<? extends E> c)` 会使用 c 的大小作为数组容量
4. `add(Object o)` 首次扩容为 10，再次扩容为上次容量的 1.5 倍，通过原本的容量增加一个原本容量的右移一位实现
5. `addAll(Collection c)` 没有元素时，扩容为 `Math.max(10, 实际元素个数)`，有元素时为 `Math.max(原容量 1.5 倍, 实际元素个数)`

### Fail-Fast 与 Fail-Safe 演示
* `ArrayList` 是 `fail-fast` 的典型代表，遍历的同时不能修改，尽快失败
```java
private static void failFast() {
    ArrayList<Student> list = new ArrayList<>();
    list.add(new Student("A"));
    list.add(new Student("B"));
    list.add(new Student("C"));
    list.add(new Student("D"));
    for (Student student : list) {
        System.out.println(student);
    }
    System.out.println(list);
}
```
> 当遍历list合集的时候，假设遍历到`student.name.equals("B")`时，在另一个线程对该集合执行`add(new Student("E"))`操作，程序便会发生异常:
```
Student{name='A'}
Student{name='B'}
Exception in thread "main" java.util.ConcurrentModificationException
	at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1013)
	at java.base/java.util.ArrayList$Itr.next(ArrayList.java:967)
	at com.riverify.list.FailFastVsFailSafe.failFast(FailFastVsFailSafe.java:16)
	at com.riverify.list.FailFastVsFailSafe.main(FailFastVsFailSafe.java:50)
```

* `CopyOnWriteArrayList` 是 `fail-safe` 的典型代表，遍历的同时可以修改，原理是读写分离
```java
private static void failSafe() {
    CopyOnWriteArrayList<Student> list = new CopyOnWriteArrayList<>();
    list.add(new Student("A"));
    list.add(new Student("B"));
    list.add(new Student("C"));
    list.add(new Student("D"));
    for (Student student : list) {
        System.out.println(student);
    }
    System.out.println(list);
}
```
> 需要注意的是，这会失去一致性，遍历的结果为修改前的结果
```
Student{name='A'}
Student{name='B'}
Student{name='C'}
Student{name='D'}
[Student{name='A'}, Student{name='B'}, Student{name='C'}, Student{name='D'}, Student{name='E'}]
```

### fail-fast 源码

> JDK version: 17

在`ArrayList`内部，首先需要知道的是`modCount`成员变量，它是从父类`AbstractList`中继承而来，初始值为`0`，负责如实记录整个`ArrayList`被修改的次数。
```java
protected transient int modCount = 0;
```
每次对集合长度的改变（如`add()`），都会调用`updateSizeAndModCount(int sizeChange)`这个方法，这个方法负责更新`modCount`。
```java
public boolean add(E e) {
    modCount++;
    add(e, elementData, size);
    return true;
}
```

同时在`ArrayList`内部有一个`Itr`的内部类，该内部类实现了`Iterator`接口，使用增强 for 循环的时候，会在首次循环创建这个`Itr`对象：
```java
public Iterator<E> iterator() {
    return new Itr();
}
```
在`Itr`的内部，也存在一个类似`modCount`的成员变量，它的初始值就等于`modCount`，用于表示当迭代刚刚开始时，集合被修改的次数。
每次执行迭代器执行`next()`方法的时候，都会先调用`checkForComodification()`方法。
```java
private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;
    
    ...
    
    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length)
            throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }
    
    ...
    
}
```

> `checkForComodification()` 方法：
```java
final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}
```
此时，若在迭代过程中，集合被修改了，那么`modCount`一定会发生变化，此时`modCount`和`expectedModCount`就不相等，
就会抛出`ConcurrentModificationException`异常，实现了`fail-fast`的机制。

### fail-safe 源码

> JDK version: 17

对于`CopyOnWriteArrayList`，它的迭代过程则有所不同，是通过内部类`COWIterator`实现的，首次进入增强 for 循环的时候，会创建一个`COWIterator`对象。
所携带的参数分别是`getArray()`和 `0`，其中`getArray()`方法返回的是一个`Object[]`型的数组，即当前集合的快照、当前集合的一个副本。
```java
public Iterator<E> iterator() {
    return new COWIterator<E>(getArray(), 0);
}
```
在`COWIterator`内部，无参构造函数中，会将当前集合的快照赋值给`snapshot`，并将`cursor`初始化为`0`。
```java
COWIterator(Object[] es, int initialCursor) {
    cursor = initialCursor;
    snapshot = es;
}
```
在`COWIterator`内部，`next()`方法中，会先判断`snapshot`是否为空，若为空，则抛出`NoSuchElementException`异常。
```java
public E next() {
    if (! hasNext())
        throw new NoSuchElementException();
    return (E) snapshot[cursor++];
}
```
由此可见，`CopyOnWriteArrayList`的迭代过程是通过遍历`snapshot`来实现的，而`snapshot`是在迭代开始的时候就已经确定了，所以在迭代过程中，
集合被修改了，也不会影响到`snapshot`，所以也就不会抛出`ConcurrentModificationException`异常，实现了`fail-safe`的机制。

我们可以查看一下修改集合的一个方法`add()`，在`add()`方法中，会调用`getArray()`方法，该方法会返回一个`Object[]`型的数组，即当前集合的快照。
之后便会通过`Arrays.copyOf()`方法，将`snapshot`数组复制一份，然后将新元素添加到新数组的末尾，最后使用`setArray()`方法，记录新数组。
```java
public boolean add(E e) {
    synchronized (lock) {
        Object[] es = getArray();
        int len = es.length;
        es = Arrays.copyOf(es, len + 1);
        es[len] = e;
        setArray(es);
        return true;
    }
}
```


### LinkedList

**要求**
* 能够说清楚 `LinkedList` 对比 `ArrayList` 的区别，并重视纠正部分错误的认知

**LinkedList**
1. 基于双向链表，无需连续内存；
2. 随机访问慢（要沿着链表遍历）；
3. 头尾插入删除性能高；
4. 占用内存多。

**ArrayList**
1. 基于数组，需要连续内存；
2. 实现了`RandomAccess`接口，随机访问快（指根据下标访问）；
3. 尾部插入、删除性能可以，其它部分插入、删除都会移动数据，因此性能会低；
4. 可以利用 cpu 缓存，局部性原理。


## HashMap
### 要求

* 掌握 HashMap 的基本数据结构；
* 掌握树化；
* 理解索引计算方法、二次 hash 的意义、容量对索引计算的影响；
* 掌握 put 流程、扩容、扩容因子；
* 理解并发使用 HashMap 可能导致的问题；
* 理解 key 的设计；

### 基本数据结构
> 和 jdk 版本有关的。
* 1.7 数组 + 链表
* 1.8 数组 + （链表 | 红黑树）

> 更形象的演示，见 src 中的 `hash/hash-demo.jar`，运行需要 `jdk14` 以上环境，进入 jar 包目录，执行下面命令，然后在浏览器中打开 http://localhost:8080/ 即可查看演示效果（需要确保 8080 端口未被占用）
>
> ```
> java -jar --add-exports java.base/jdk.internal.misc=ALL-UNNAMED hash-demo.jar
> ```

### 树化与退化

**树化意义**

* 红黑树用来避免 DoS 攻击，防止链表超长时性能下降，树化应当是偶然情况，是保底策略；
* hash 表的查找，更新的时间复杂度是 `O(1)`，而红黑树的查找，更新的时间复杂度是 `O(log2 N )`，TreeNode 占用空间也比普通 Node 的大，如非必要，尽量还是使用链表；
* hash 值如果足够随机，则在 hash 表内按泊松分布，在负载因子 0.75 的情况下，长度超过 8 的链表出现概率是 0.00000006，树化阈值选择 8 就是为了让树化几率足够小。

**树化规则**

* 当链表长度超过树化阈值 **8** 时，先尝试扩容来减少链表长度，只有数组容量已经 >= 64，才会进行树化。

**退化规则**

* 情况1：在**扩容时**如果拆分树时，树元素个数 **<= 6** 则会退化链表；
* 情况2：**remove** 树节点**前**，若 `root`、`root.left`、`root.right`、`root.left`.`left` 有一个为 null ，在移除后也会退化为链表。
> 注意：两种情况分别是对应增加和删除节点时的退化。

### 索引计算

**索引计算方法**

* 首先，计算对象的 `hashCode()`；
* 再进行调用 HashMap 的 `hash()` 方法进行二次哈希；
    * 二次 `hash()` 是为了综合高位数据，让哈希分布更为均匀；
* 最后 `& (capacity – 1)` 得到索引。

**数组容量为何是 2 的 n 次幂**

1. 计算索引时效率更高：如果是 2 的 n 次幂可以使用位与运算代替取模；
2. 扩容时重新计算索引效率更高：`hash & oldCap == 0` 的元素留在原来位置 ，否则`新位置 = 旧位置 + oldCap`。

**注意**

* 二次 hash 是为了配合 **容量是 2 的 n 次幂** 这一设计前提，如果 hash 表的容量不是 2 的 n 次幂，则不必二次 hash；
* **容量是 2 的 n 次幂** 这一设计计算索引效率更好，但 hash 的分散性就不好，需要二次 hash 来作为补偿，没有采用这一设计的典型例子是 Hashtable。

### put 与扩容

**put 流程**

1. HashMap 是懒惰创建数组的，首次使用才创建数组；
2. 计算索引（桶下标）；
3. 如果桶下标还没人占用，创建 Node 占位返回；
4. 如果桶下标已经有人占用：
    1. 已经是 TreeNode 走红黑树的添加或更新逻辑；
    2. 是普通 Node，走链表的添加或更新逻辑，如果链表长度超过树化阈值，走树化逻辑。
5. 返回前检查容量是否超过阈值，一旦超过进行扩容。

**1.7 与 1.8 的区别**

1. 链表插入节点时，1.7 是头插法，1.8 是尾插法；

2. 1.7 是大于等于阈值且没有空位时才扩容，而 1.8 是大于阈值就扩容；

3. 1.8 在扩容计算 Node 索引时，会优化。

**扩容（加载）因子为何默认是 0.75f**

1. 在空间占用与查询时间之间取得较好的权衡；
2. 大于这个值，空间节省了，但链表就会比较长影响性能；
3. 小于这个值，冲突减少了，但扩容就会更频繁，空间占用也更多。

### 并发问题

**扩容死链（1.7 会存在）**

1.7 源码如下：

```java
void transfer(Entry[] newTable, boolean rehash) {
    int newCapacity = newTable.length;
    for (Entry<K,V> e : table) {
        while(null != e) {
            Entry<K,V> next = e.next;
            if (rehash) {
                e.hash = null == e.key ? 0 : hash(e.key);
            }
            int i = indexFor(e.hash, newCapacity);
            e.next = newTable[i];
            newTable[i] = e;
            e = next;
        }
    }
}
```

* e 和 next 都是局部变量，用来指向当前节点和下一个节点；
* 线程1（绿色）的临时变量 e 和 next 刚引用了这俩节点，还未来得及移动节点，发生了线程切换，由线程2（蓝色）完成扩容和迁移。

![image-20210831084325075](https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/notes/img/image-20210831084325075.png?raw=true)

* 线程2 扩容完成，由于头插法，链表顺序颠倒。但线程1 的临时变量 e 和 next 还引用了这俩节点，还要再来一遍迁移。

![image-20210831084723383](https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/notes/img/image-20210831084723383.png?raw=true)

* 第一次循环
    * 循环接着线程切换前运行，注意此时 e 指向的是节点 a，next 指向的是节点 b；
    * e 头插 a 节点，注意图中画了两份 a 节点，但事实上只有一个（为了不让箭头特别乱画了两份）；
    * 当循环结束是 e 会指向 next 也就是 b 节点。

![image-20210831084855348](https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/notes/img/image-20210831084855348.png?raw=true)

* 第二次循环
    * next 指向了节点 a；
    * e 头插节点 b；
    * 当循环结束时，e 指向 next 也就是节点 a。

![image-20210831085329449](https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/notes/img/image-20210831085329449.png?raw=true)

* 第三次循环
    * next 指向了 null；
    * e 头插节点 a，**a 的 next 指向了 b**（之前 a.next 一直是 null），b 的 next 指向 a，死链已成；
    * 当循环结束时，e 指向 next 也就是 null，因此第四次循环时会正常退出。

![image-20210831085543224](https://github.com/riverify/java-must-know/blob/main/chapter_1_basics/notes/img/image-20210831085543224.png?raw=true)




### key 的设计

**key 的设计要求**

1. HashMap 的 key 可以为 null，但 Map 的其他实现则不然；
2. 作为 key 的对象，必须实现 hashCode 和 equals，并且 key 的内容不能修改（不可变）；
3. key 的 hashCode 应该有良好的散列性。

如果 key 可变，例如修改了 age 会导致再次查询时查询不到

```java
public class HashMapMutableKey {
    public static void main(String[] args) {
        HashMap<Student, Object> map = new HashMap<>();
        Student stu = new Student("张三", 18);
        map.put(stu, new Object());

        System.out.println(map.get(stu));

        stu.age = 19;
        System.out.println(map.get(stu));
    }

    static class Student {
        String name;
        int age;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return age == student.age && Objects.equals(name, student.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}
```



**String 对象的 hashCode() 设计**

* 目标是达到较为均匀的散列效果，每个字符串的 hashCode 足够独特；
* 字符串中的每个字符都可以表现为一个数字，称为 `$S_i$`，其中 i 的范围是 `0 ~ n - 1`；
* 散列公式为： `$S_0∗31^{(n-1)}+ S_1∗31^{(n-2)}+ … S_i ∗ 31^{(n-1-i)}+ …S_{(n-1)}∗31^0$`；
* 31 代入公式有较好的散列特性，并且 31 * h 可以被优化为
    * 即 `$32 ∗h -h $`；
    * 即 `$2^5  ∗h -h$`；
    * 即 `$h≪5  -h$`。




