package lc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 剑指 Offer 34. 二叉树中和为某一值的路径
 * 输入一棵二叉树和一个整数，打印出二叉树中节点值的和为输入整数的所有路径。从树的根节点开始往下一直到叶节点所经过的节点形成一条路径。
 * <p>
 * 示例:
 * 给定如下二叉树，以及目标和 target = 22，
 * <p>
 * 5
 * / \
 * 4   8
 * /   / \
 * 11  13  4
 * /  \    / \
 * 7    2  5   1
 * 返回:
 * <p>
 * [
 * [5,4,11,2],
 * [5,8,4,5]
 * ]
 */
public class 剑指Offer34_二叉树中和为某一值的路径 {
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        private TreeNode() {
        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    private LinkedList<List<Integer>> path = new LinkedList<>();
    private LinkedList<Integer> data = new LinkedList<>();

    private LinkedList<List<Integer>> dfs(TreeNode cur, Integer target) {
        getSum(cur, target);
        return path;
    }

    private void getSum(TreeNode cur, Integer target) {
        if (cur == null) {
            return;
        }
        target -= cur.val;
        data.add(cur.val);
        if (target == 0 && cur.left == null && cur.right == null) {
            path.add(new ArrayList<>(data));
        }
        getSum(cur.left, target);
        getSum(cur.right, target);
        path.removeLast();
    }
}

