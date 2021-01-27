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
        int[] nums1 = {10, 15, 20};
        int m = 25;
        String s = "babad";
        System.out.println(longestPalindrome(s));
    }


    public static String longestPalindrome(String s) {

        return s;
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
