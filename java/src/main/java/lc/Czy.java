package lc;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 剑指 Offer II 001. 整数除法
 * 给定两个 01 字符串 a 和 b ，请计算它们的和，并以二进制字符串的形式输出。
 * 输入为 非空 字符串且只包含数字 1 和 0。
 * <p>
 * 示例 1:
 * <p>
 * 输入: a = "11", b = "10"
 * 输出: "101"
 * 示例 2:
 * <p>
 * 输入: a = "1010", b = "1011"
 * 输出: "10101"
 */
public class Czy {
    public static void main(String[] args) {
//        System.out.println(countBits(1));
        System.out.println(Arrays.toString(countBits(1)));
    }

    public static int[] countBits(int num) {
        int[] result = new int[num + 1];
        for (int i = 1; i <= num; i++) {
            if(i % 2 == 1)
            {
                result[i] = result[i-1] + 1;
            }
            else
            {
                result[i] = result[i/2];
            }
        }
        return result;
    }
}
