package Java基础.Ⅵ输入输出流.字节流;

import java.io.FileOutputStream;
import java.io.IOException;

/*
   写入文件示例
 */
public class ByteStreamWriteDemo {
    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            // 创建文件输出流（true 表示追加写入）
            fos = new FileOutputStream("D:\\output.txt", true);

            String content = "oHello, Byte Stream!\n";
            fos.write(content.getBytes()); // 将字符串转成字节数组写入
            System.out.println("写入完成！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close(); // 关闭流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
