package 算法自主练习.queue;

//1.字符串中的第一个唯一字符
//有：返回索引位置
//无：返回-1

import java.util.HashMap;

public class FirstCharOfString {

    public int firstUniqChar(String s) {
        HashMap<Character,Boolean> dic = new HashMap<>();
        char[] str = s.toCharArray();
        for (char c:str) {
            dic.put(c,!dic.containsKey(c));
        }
        for (int i = 0; i < str.length; i++) {
            if (dic.get(str[i])) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        FirstCharOfString test = new FirstCharOfString();
        int n = test.firstUniqChar("sstring");
        System.out.println(n);
    }
}
