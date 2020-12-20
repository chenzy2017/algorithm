/**
 * @description 查找算法
 * @author: chenzeyong
 * @create: 2020-12-20 16:57
 **/
public class Find {

    //二分查找, O(logn)
    public static int binarySearch(int[] a, int value) {
        int min = 0, max = a.length - 1, mid;
        while (min <= max) {
            mid = (min + max) / 2;
            if (a[mid] > value) {
                max = mid - 1;
            } else if (a[mid] < value) {
                min = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        System.out.println(binarySearch(a, 4));
    }

}
