package Java基础.Ⅵ输入输出流.字符流;

import java.io.FileWriter;
import java.io.IOException;

/*
 * 字符流写入文件示例
 */
public class CharStreamWriteDemo {
    public static void main(String[] args) {
        FileWriter fw = null;
        try {
            // 创建文件字符输出流（true 表示追加写入）
            fw = new FileWriter("D:\\output.txt", true);

            String content = "Hello, Character Stream!\n";
            fw.write(content); // 直接写入字符串
            System.out.println("写入完成！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
