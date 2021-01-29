import java.util.ArrayList;
import java.util.Arrays;
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
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int m = 2147395600;
        String s = "MCMXCIV";
        String a = "1";

        System.out.println(romanToInt(s));
//        moveZeroes(nums);
    }

    // 罗马数字转阿拉伯
    public static int romanToInt(String s) {
        char[] a = s.toCharArray();
        int b = getValue(a[0]);
        int sum = 0;
        for (int i = 1; i < a.length; i++) {
            int c = getValue(a[i]);
            if (c <= b) {
                sum += b;
            } else {
                sum -= b;
            }
            b = c;
        }
        return sum + b;
    }

    // 罗马数字映射表
    public static int getValue(char c) {
        if (c == 'I') {
            return 1;
        } else if (c == 'V') {
            return 5;
        } else if (c == 'X') {
            return 10;
        } else if (c == 'L') {
            return 50;
        } else if (c == 'C') {
            return 100;
        } else if (c == 'D') {
            return 500;
        } else if (c == 'M') {
            return 1000;
        } else {
            return 0;
        }
    }

    // 是否能开平方
    public boolean isPerfectSquare(int num) {
        int a = (int) Math.exp(0.5 * Math.log(num));
        if ((a + 1) * (a + 1) == num || a * a == num) {
            return true;
        } else {
            return false;
        }
    }

    // 编写sqrt函数
    public static int mySqrt(int x) {
        int a = (int) Math.exp(0.5 * Math.log(x));
        // 因为指数运算和对数运算都是double型的, 会丢失精度, 所以还需要比较a+1和a哪个才是正确的;
        if (Math.pow(a + 1, 2) <= x) {
            return a + 1;
        } else {
            return a;
        }
    }

    // 链表结构
    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    // 链表移动元素
    public static ListNode removeElements(ListNode head, int val) {
        if (head == null) {
            return null;
        }
        head.next = removeElements(head.next, val);
        return head.val == val ? head.next : head;
    }

    // 删除链表重复元素
    public ListNode deleteDuplicates(ListNode head) {
        ListNode cur = head;
        while (cur != null && cur.next != null) {
            if (cur.val == cur.next.val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

    // 移除重复元素
    public static int removeDuplicates(int[] nums) {
        int n = nums.length, count = 1, left = 0;
        for (int i = 0; i < n; i++) {
            if (nums[left] != nums[i]) {
                nums[++left] = nums[i];
                count++;
            }
        }
        System.out.println(Arrays.toString(nums));
        return count;
    }

    // 移除指定元素
    public static int removeElement(int[] nums, int val) {
        int n = nums.length, count = 0, left = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] != val) {
                count++;
                nums[left++] = nums[i];
            }
        }
        return count;
    }

    // 移动0元素
    public static void moveZeroes(int[] nums) {
        int n = nums.length;
        int left = 0;

        for (int i = 0; i < n; i++) {
            if (nums[i] != 0) {
                nums[left++] = nums[i];
            }
        }
        for (int i = left; i < n; i++) {
            nums[i] = 0;
        }
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
