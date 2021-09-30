import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @description 十大排序算法
 * @author: chenzeyong
 * @create: 2020-12-19 15:05
 **/
@Data
@Slf4j
public class Sort {
    /**
     * 冒泡(升序)
     * 时间复杂度:a^n
     *
     * @param arr
     * @return
     */
    public static Integer[] bubbleSort(Integer[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    /**
     * 选择排序(升序)
     * 时间复杂度 a^n
     *
     * @param a
     * @return
     */
    public static Integer[] selectionSort(Integer[] a) {
        int minIndex;
        for (int i = 0; i < a.length - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
        return a;
    }

    /**
     * 插入排序, 时间复杂度n^2
     *
     * @param a
     * @return
     */
    public static Integer[] insertionSort(Integer[] a) {
        int current, preIndex;
        for (int i = 0; i < a.length; i++) {
            current = a[i];
            preIndex = i - 1;
            while (preIndex >= 0 && a[preIndex] > current) {
                a[preIndex + 1] = a[preIndex];
                preIndex--;
            }
            a[preIndex + 1] = current;
        }
        return a;
    }

    /**
     * 希尔排序, (优化版的插入排序)时间复杂度n^1.3
     *
     * @param arr
     * @return
     */
    public static Integer[] shellSort(Integer[] arr) {
        for (int gap = (int) Math.floor(arr.length / 2); gap > 0; gap = (int) Math.floor(gap / 2)) {
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                int current = arr[i];
                while (j - gap >= 0 && current < arr[j - gap]) {
                    arr[j] = arr[j - gap];
                    j = j - gap;
                }
                arr[j] = current;
            }
        }
        return arr;
    }

    /**
     * 快排,时间复杂度nlog2^n
     */
    public static void quickSort(Integer[] a, int l, int r) {
        if (l >= r) {
            return;
        }
        int key = a[l], i = l, j = r;
        while (i < j) {
            while (a[j] >= key && i < j) {
                j--;
            }
            while (a[i] <= key && i < j) {
                i++;
            }
            if (i < j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        a[l] = a[i];
        a[i] = key;
        quickSort(a, l, i - 1);
        quickSort(a, i + 1, r);
    }

    /**
     * 堆排, 时间复杂度o(nlog2^n)
     *
     * @param a
     * @return
     */
    public static Integer[] heapSort(Integer[] a) {
        int len = a.length;
        buildMaxHeap(a, len);// 构建大顶堆
        for (int i = len - 1; i > 0; i--) {
            swap(a, 0, i);//替换堆顶元素和最后一个元素
            len--;
            heapify(a, 0, len);
        }
        return a;
    }

    public static void buildMaxHeap(Integer[] a, Integer len) {
        for (int i = len / 2; i >= 0; i--) {
            heapify(a, i, len);
        }
    }

    public static void heapify(Integer[] a, Integer root, Integer len) {
        int left = 2 * root + 1, right = 2 * root + 2, largestIndex = root;
        if (left < len && a[left] > a[largestIndex]) {
            largestIndex = left;
        }
        if (right < len && a[right] > a[largestIndex]) {
            largestIndex = right;
        }
        if (largestIndex != root) {
            swap(a, root, largestIndex);
            heapify(a, largestIndex, len);
        }
    }

    public static void swap(Integer[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * 计数排序, 号称最快比较排序算法(仅适用于:当k不是很大并且序列比较集中), 时间复杂度o(n+k)
     * 缺点, 不能排序负数,
     * <p>
     * 解决负数办法:
     * 通过当前值减去最小值的赋值形式（source[i]-min），
     * 解决了不能为负数的问题。
     * （例如当前数组值为-1，减去最小值（-1），得出的结果值最小也为0）
     *
     * @param sourceArray
     * @return
     */
    public static int[] sort(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int maxValue = getMaxValue(arr);

        return countingSort(arr, maxValue);
    }

    public static int[] countingSort(int[] arr, int maxValue) {
        int bucketLen = maxValue + 1;
        int[] bucket = new int[bucketLen];

        for (int value : arr) {// 不能有负数
            bucket[value]++;
        }

        int sortedIndex = 0;
        for (int j = 0; j < bucketLen; j++) {
            while (bucket[j] > 0) {
                arr[sortedIndex++] = j;
                bucket[j]--;
            }
        }
        return arr;
    }

    public static int getMaxValue(int[] arr) {
        int maxValue = arr[0];
        for (int value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    /**
     * 桶排序, 时间复杂度O(n+k)
     *
     * @param a
     * @return
     */
    public static int[] bucketSort(int[] a) {
        // 桶数量
        Integer bucketCount = 5;
        if (a.length == 0) {
            return a;
        }

        // 找出最大值最小值
        int min = a[0];
        int max = a[0];
        for (int i : a) {
            if (i < min) {
                min = i;
            }
            if (i > max) {
                max = i;
            }
        }
        // 计算桶距
        Integer c = (int) Math.floor((max - min) / bucketCount) + 1;

        Integer buckets[][] = new Integer[bucketCount][0];

        // 根据函数的映射关系算法, 将数据分类放入桶中
        for (int i = 0; i < a.length; i++) {
            // 先计算这个数应该在第几个桶中
            int k = (int) Math.floor((a[i] - min) / c);
            // 将这个数放入动态扩容的数组中;
            buckets[k] = arrApend(buckets[k], a[i]);
        }

        // 根据插入排序将桶内的数据排序
        int d = 0;
        for (Integer[] k : buckets) {
            if (k.length <= 0) {
                continue;
            }
            // 根据插入排序将桶内的数据排序
            k = insertionSort(k);
            // 将数组a重新赋值
            for (int value : k) {
                a[d++] = value;
            }
        }
        return a;
    }

    // 动态扩容数组
    public static Integer[] arrApend(Integer[] a, int value) {
        Integer[] b = Arrays.copyOf(a, a.length + 1);
        b[a.length] = value;
        return b;
    }

    public static void main(String[] args) {
        int a[] = {2, 4, 1, 3, 5};
//        System.out.println(Arrays.toString(bubbleSort(a)));// 冒泡
//        System.out.println(Arrays.toString(selectionSort(a)));// 选择
//        System.out.println(Arrays.toString(insertionSort(a)));// 插入
//        System.out.println(Arrays.toString(shellSort(a)));// 希尔
//        quickSort(a, 0, a.length - 1);// 快排
//        System.out.println(Arrays.toString(a));
//        heapSort(a);// 堆排
//        System.out.println(Arrays.toString(a));
//        System.out.println(Arrays.toString(sort(a)));// 计数排序,O(n+k)
        System.out.println(Arrays.toString(bucketSort(a)));

    }
}
