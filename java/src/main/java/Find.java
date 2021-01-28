import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        int[] nums = {0, 1, 0, 3, 12};
        int m = 1000;
        String s = "11";
        String a = "1";

        System.out.println();
        moveZeroes(nums);
    }

    public static void moveZeroes(int[] nums) {

    }

    // 二进制求和
    public static String addBinary(String a, String b) {
        char[] x = a.toCharArray();
        char[] y = b.toCharArray();
        int n1 = a.length() - 1, n2 = b.length() - 1, n = 0;
        StringBuilder result = new StringBuilder();
        while (n1 >= 0 || n2 >= 0) {
            int c = n1 >= 0 ? x[n1] - '0' : 0;
            int d = n2 >= 0 ? y[n2] - '0' : 0;
            int sum = c + d + n;
            if (sum >= 2) {
                n = 1;
                sum %= 2;
            } else {
                n = 0;
            }
            result.append(sum);
            n1--;
            n2--;
        }
        if (n == 1) {
            result.append("1");
        }
        return result.reverse().toString();
    }

    // 字符(里面是数字)相加
    public static String addStrings(String num1, String num2) {
        char[] a = num1.toCharArray();
        char[] b = num2.toCharArray();
        int n1 = num1.length() - 1, n2 = num2.length() - 1, x = 0;
        StringBuilder result = new StringBuilder();
        while (n1 >= 0 || n2 >= 0) {
            int c = n1 >= 0 ? a[n1] - '0' : 0;
            int d = n2 >= 0 ? b[n2] - '0' : 0;
            int sum = c + d + x;
            if (sum >= 10) {
                x = 1;
                sum %= 10;
            } else {
                x = 0;
            }
            result.append(sum);
            n1--;
            n2--;
        }
        if (x == 1) {
            result.append('1');
        }
        return result.reverse().toString();
    }

    // 字符(数字) 和 数组 相加
    public static List<Integer> addToArrayForm(int[] A, int K) {
        List<Integer> result = new ArrayList<>();
        for (int i = A.length - 1; i >= 0; i--) {
            int sum = A[i] + K % 10;
            K /= 10;
            if (sum >= 10) {
                K++;
                sum -= 10;
            }
            result.add(sum);
        }
        for (; K > 0; K /= 10) {
            result.add(K % 10);
        }
        Collections.reverse(result);
        return result;
    }

    // 数组里当数字+1
    public static int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if ((digits[i] + 1) % 10 != 0) {
                digits[i]++;
                return digits;
            } else {
                digits[i] = 0;
            }
        }
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    //动态规划,dp table, 状态转移方程
    public static int minCostClimbingStairs(int[] cost) {
        if (cost.length == 2) {
            return Math.min(cost[0], cost[1]);
        }
        int dp[] = new int[cost.length + 1];
        dp[0] = 0;
        dp[1] = 0;
        for (int i = 2; i <= cost.length; i++) {
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        return dp[cost.length];
    }

    // 斐波那契数列, dp变形
    public static int reverse(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int a = 0, b = 1, c = 1;
        for (int i = 2; i <= n; i++) {
            a = b;
            b = c;
            c = a + b;
        }
        return c;
    }
}
