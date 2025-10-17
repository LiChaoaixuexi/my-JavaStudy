package 排序算法.选择排序.简单选择排序;

/**
 * 选择排序
 * 选择排序（Selection sort）是一种简单直观的排序算法。
 * 它的工作原理是每次从待排序的数据元素中选出最小（或最大）
 * 的一个元素，存放在序列的起始位置，直到全部待排序的数据元素排完。
 * 时间复杂度：最好情况：O(n^2)，最坏情况：O(n^2)，平均情况：O(n^2)
 * 空间复杂度：O(1)
 * 稳定性：不稳定
 */
public class SelectionSort {

    //1.简单选择排序
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 2, 8, 4, 1};
        selectionSort(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}
