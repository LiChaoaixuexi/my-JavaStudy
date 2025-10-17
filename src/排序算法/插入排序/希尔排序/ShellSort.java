package 排序算法.插入排序.希尔排序;

import java.util.Arrays;

/**
 * 希尔排序：
 *    希尔排序是插入排序的一种改进版，由计算机科学家 Donald Shell 于 1959 年提出。
 * 它的核心思想是：先将整个数组分割成若干个 “子序列”，对每个子序列进行插入排序；然
 * 后逐步缩小子序列的间隔，重复排序；最后当间隔为 1 时，对整个数组进行一次插入排序，
 * 完成最终排序。
 * 复杂度分析：
 *    时间复杂度：希尔排序的时间复杂度依赖于间隔的选择，目前没有精确的数学表达式。对于常见的 gap = n/2^k 方式，时间复杂度约为 O(n²)，但实际效率通常优于直接插入排序；更优的间隔设计（如 Hibbard 序列）可使时间复杂度接近 O(n^1.5)。
 *    空间复杂度：O(1)，仅使用常数级额外空间（属于原地排序）。
 *    稳定性：不稳定（因为相同元素可能被分到不同子序列，导致相对位置变化）。
 * 记忆技巧：
 *    希尔排序 = 分组插入排序 + 逐步缩小组距。
 *    核心逻辑：先让数组 “大概有序”，最后用插入排序收尾。
 *    代码结构：外层循环控制间隔（从大到小），内层循环对每个子序列做插入排序。
 */
public class ShellSort {
    public static void shellSort(int[] arr) {
        int n = arr.length;
        // 数组为空或长度为1，直接返回
        if (arr == null || n < 1) {
            return;
        }

        // 外层循环控制间隔（从大到小）
        // 初始间隔为n/2，每次缩小为原来的1/2
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // 内层循环对每个子序列做插入排序
            for (int i = gap; i < n; i++) {
                int key = arr[i];
                int j = i - gap;

                while (j >= 0 && arr[j] > key) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }
                arr[j + gap] = key;
            }
            System.out.println("间隔为" + gap + "时的排序结果：" + Arrays.toString(arr));
        }
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {8, 9, 1, 7, 2, 3, 5, 4, 6, 0};
        System.out.println("排序前：" + Arrays.toString(arr));
        shellSort(arr);
        System.out.println("排序后：" + Arrays.toString(arr));
    }

}
