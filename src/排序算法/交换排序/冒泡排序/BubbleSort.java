package 排序算法.交换排序.冒泡排序;

/**
 * 冒泡排序
 * 1.原理：通过对待排序序列从前向后（从下标较小的元素开始）,依次对相邻两个元素的值进行两两比较，
 * 若发现前一个数大于后一个数则交换，使值较大的元素逐渐从前移向后部，就如果水底下的气泡一样逐渐向上冒。
 * 2.时间复杂度：O(n^2)
 * 3.空间复杂度：O(1)
 * 4.稳定性：稳定
 * 5.优化：在每一轮排序后，记录最后一次交换的位置，该位置之后的元素都是有序的，再往后就不需要判断了。
 * @author: copyLi
 * @create: 2025-10-13 13:00
 */
public class BubbleSort {

    //1.基础冒泡排序
    public static int[] simpleBubbleSort(int[] array){
        int n = array.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-1-i; j++) {
                if (array[j] > array[j+1]) {
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }
        return array;
    }

    //2.优化冒泡排序
    public static int[] optimizeBubbleSort(int[] array){
        int n = array.length;
        boolean result = false;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-1-i; j++) {
                if (array[j] > array[j+1]) {
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                    result = true;
                }
            }
            //如果一轮排序后没有发生交换，说明数组已经有序，可以提前结束排序
            if (result == false) {
                break;
            }
        }
        return array;
    }

    public static void main(String[] args) {
        int[] array = {3, 9, -1, 10, 20};
        int[] ints = simpleBubbleSort(array);
        for (int i = 0; i < ints.length; i++) {
            System.out.print(ints[i] + " ");
        }
    }
}
