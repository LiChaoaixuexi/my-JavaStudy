package Java基础.Ⅵ输入输出流.字节流;

import java.io.FileInputStream;
import java.io.IOException;

/*
   读取文件示例
 */

public class ByteStreamReadDemo {
    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            // 创建文件输入流
            fis = new FileInputStream("D:\\data.txt");

            int data;
            // 每次读取一个字节
            while ((data = fis.read()) != -1) {
                System.out.print(data + " ");
                System.out.println((char) data); // 转成字符输出
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close(); // 关闭流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
