package Java基础.Ⅵ输入输出流.字符流;

import java.io.FileReader;
import java.io.IOException;

/*
   读取文件示例
 */
public class CharStreamReadDemo {
    public static void main(String[] args) {
        FileReader fr = null;
        try {
            // 创建文件字符输入流
            fr = new FileReader("D:\\data.txt");

            int data;
            // 每次读取一个字符
            while ((data = fr.read()) != -1) {
                System.out.print(data + " ");
                System.out.println((char) data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
