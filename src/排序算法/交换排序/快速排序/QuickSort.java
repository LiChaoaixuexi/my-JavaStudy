package 排序算法.交换排序.快速排序;

/**
 * 快速排序
 * 基本思想：选择一个基准值，将比基准值小的放在左边，比基准值大的放在右边，然后递归的进行左右两边的排序。
 * 时间复杂度：平均情况O(nlogn)，最坏情况O(n^2)
 * 空间复杂度：O(logn)
 * 稳定性：不稳定
 * @author: copyLi
 * @create: 2025-10-13 13:10
 */
public class QuickSort {

    public static void quickSort(int[] arr) {
        int originLeft = 0;
        int originRight = arr.length - 1;
        simpleQuickSort(arr, originLeft, originRight);
    }

    //1.简单的双指针快速排序(挖坑法)：
    //这是典型的“挖坑+替换”实现：先以 a[left] 为 pivot，左右交替将元素写回到“坑”位置，最后把 pivot 放回。
    public static void simpleQuickSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }
        //选择最左边的元素作为基准值
        int privot = partition(arr, left, right);
        //对基准值左边的元素进行快速排序
        partition(arr, left, privot - 1);
        //对基准值右边的元素进行快速排序
        partition(arr, privot + 1, right);
    }

    public static int partition(int[] arr, int left, int right) {
        int privot = arr[left];//基准值
        while (left < right) {
            while (left < right && arr[right] >= privot) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left] <= privot) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = privot;
        return left;
    }

    public static void main(String[] args) {
        int[] arr = {3, 5, 1, 4, 2, 6};
        quickSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
