package 算法自主练习.sortA;

import java.util.Arrays;
import java.util.Comparator;;

public class Main {

    public static void main(String[] args) {

        int[] a = {1,4,2,6,5,9,7};
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
        Integer[] b = {1,5,2,6,3,8,4};
        Comparator comparator = new MyComparator();
        Arrays.sort(b,comparator);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
    }

    static class MyComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

}