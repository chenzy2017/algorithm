package lc;

/**
 * 剑指 Offer II 001. 整数除法
 * 给定两个整数 a 和 b ，求它们的除法的商 a/b ，要求不得使用乘号 '*'、除号 '/' 以及求余符号 '%'
 * 注意：
 * <p>
 * 整数除法的结果应当截去（truncate）其小数部分，例如：truncate(8.345) = 8以及truncate(-2.7335) = -2
 * 假设我们的环境只能存储 32 位有符号整数，其数值范围是 [−2^31,2^31−1]。本题中，如果除法结果溢出，则返回 2^31− 1
 * 示例 1：
 * <p>
 * 输入：a = 15, b = 2
 * 输出：7
 * 解释：15/2 = truncate(7.5) = 7
 * 示例 2：
 * <p>
 * 输入：a = 7, b = -3
 * 输出：-2
 * 解释：7/-3 = truncate(-2.33333..) = -2
 */
public class 剑指Offer_1_整数除法 {
    public static void main(String[] args) {
        System.out.println(divide(-2147483648, 2));
    }

    public static int divide(int a, int b) {
        int flag = 0;
        int sum = 0;
        if (a == 0) {
            return 0;
        } else if (a == b) {
            return 1;
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            return -Integer.MIN_VALUE - 1;
        }
        if ((a > 0 && b > 0) || (a < 0 && b < 0)) {
            flag = 1;
        } else {
            flag = -1;
        }
        if (a > 0) {
            a = -a;
        }
        if (b > 0) {
            b = -b;
        }
        while (a <= b) {
            int value = b;
            int k = 1;
            while (value >= - Math.pow(2,30) && a <= value + value) {
                value += value;
                k += k;
            }
            a -= value;
            sum += k;
        }
        return flag * sum;
    }
}
