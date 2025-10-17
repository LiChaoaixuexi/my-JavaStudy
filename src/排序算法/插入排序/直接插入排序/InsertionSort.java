package 排序算法.插入排序.直接插入排序;

import java.util.Arrays;

/**
 * 插入排序
 * 思路：
 *     在待排序的元素中，假设前n-1个元素已有序，现将第n个元素插入到前面已经排好的序列中，
 * 使得前n个元素有序。按照此法对所有元素进行插入，直到整个序列有序。
 *     但我们并不能确定待排元素中究竟哪一部分是有序的，所以我们一开始只能认为第一个元素是
 * 有序的，依次将其后面的元素插入到这个有序序列中来，直到整个序列有序为止。
 * 时间复杂度：
 *    最好情况：O(n)
 *    最坏情况：O(n^2)
 *    平均情况：O(n^2)
 * 空间复杂度：O(1)
 * 稳定性：稳定
 */
public class InsertionSort {

    //1.基础插入排序
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
            System.out.println("第 " + i + " 次" + Arrays.toString(arr));
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 5, 3, 2, 1, 2, 3};
        insertionSort(arr);
        System.out.println("最终结果：" + Arrays.toString(arr));
    }
}
