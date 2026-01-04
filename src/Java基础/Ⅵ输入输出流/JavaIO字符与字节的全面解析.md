# Java I/O 流：字符与字节的全面解析

好的，你想深入了解 **Java 的 I/O 流机制**，包括 **字符流** 和 **字节流**，以及文件的读写操作。  

我会用 **概念讲解 + 代码示例 + 流程图** 的方式，帮你彻底掌握 Java I/O。

---

## 一、Java I/O 流的基本概念

### 1. 什么是流（Stream）？

- **流**是 Java 用来处理输入/输出的一种抽象概念。

- 数据像水流一样，从一个源头流向一个目的地。

- 流分为：

    - **输入流**：从外部设备（文件、网络、键盘）读取数据到程序。

    - **输出流**：将程序中的数据写入到外部设备（文件、网络、显示器）。

---

### 2. 流的分类

Java I/O 流主要分为 **两大类**：

|类型|处理单位|适用场景|典型类|
|---|---|---|---|
|**字节流**|字节（byte）|处理二进制数据（图片、视频、音频等）|`InputStream` / `OutputStream`|
|**字符流**|字符（char）|处理文本数据（字符串、文件中的文字）|`Reader` / `Writer`|
---

### 3. 字节流与字符流的区别

- **字节流**：

    - 以 8 位字节为单位进行读写。

    - 可以处理任意类型的文件（包括文本和二进制）。

    - 不涉及编码转换。

- **字符流**：

    - 以 16 位字符为单位进行读写。

    - 专门处理文本文件，会根据编码（如 UTF-8、GBK）进行字符转换。

    - 更适合处理文本，避免乱码问题。

---

## 二、字节流（Byte Stream）

### 1. 核心类

- **输入字节流**：`InputStream`（抽象类）

    - 常用子类：`FileInputStream`（文件输入流）

- **输出字节流**：`OutputStream`（抽象类）

    - 常用子类：`FileOutputStream`（文件输出流）

---

### 2. 字节流读写文件示例

#### 2.1 读取文件（`FileInputStream`）

```Java
import java.io.FileInputStream;
import java.io.IOException;

public class ByteStreamReadDemo {
    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            // 创建文件输入流
            fis = new FileInputStream("data.txt");

            int data;
            // 每次读取一个字节
            while ((data = fis.read()) != -1) {
                System.out.print((char) data); // 转成字符输出
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
```

---

#### 2.2 写入文件（`FileOutputStream`）

```Java
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteStreamWriteDemo {
    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            // 创建文件输出流（true 表示追加写入）
            fos = new FileOutputStream("output.txt", true);

            String content = "Hello, Byte Stream!\n";
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
```

---

## 三、字符流（Character Stream）

### 1. 核心类

- **输入字符流**：`Reader`（抽象类）

    - 常用子类：`FileReader`（文件字符输入流）

- **输出字符流**：`Writer`（抽象类）

    - 常用子类：`FileWriter`（文件字符输出流）

---

### 2. 字符流读写文件示例

#### 2.1 读取文件（`FileReader`）

```Java
import java.io.FileReader;
import java.io.IOException;

public class CharStreamReadDemo {
    public static void main(String[] args) {
        FileReader fr = null;
        try {
            // 创建文件字符输入流
            fr = new FileReader("data.txt");

            int data;
            // 每次读取一个字符
            while ((data = fr.read()) != -1) {
                System.out.print((char) data);
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
```

---

#### 2.2 写入文件（`FileWriter`）

```Java
import java.io.FileWriter;
import java.io.IOException;

public class CharStreamWriteDemo {
    public static void main(String[] args) {
        FileWriter fw = null;
        try {
            // 创建文件字符输出流（true 表示追加写入）
            fw = new FileWriter("output.txt", true);

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
```

---

## 四、缓冲流（Buffered Stream）

为了提高读写效率，Java 提供了 **缓冲流**，它在字节流/字符流的基础上增加了缓冲区。

### 1. 字节缓冲流

- `BufferedInputStream`

- `BufferedOutputStream`

### 2. 字符缓冲流

- `BufferedReader`

- `BufferedWriter`

---

### 3. 缓冲流示例（`BufferedReader` 按行读取）

```Java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferedReaderDemo {
    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("data.txt"));

            String line;
            // 按行读取
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

---

## 五、Java I/O 流分类总结图

```Plain Text
┌───────────────────────────────────────────────────────────┐
│                        Java I/O 流                         │
├───────────────────────────────────────────────────────────┤
│ 字节流 (Byte Stream)                    字符流 (Char Stream)│
├─────────────────────┐ ┌───────────────────────────────────┐
│ InputStream         │ │ Reader                            │
│ ├─ FileInputStream  │ │ ├─ FileReader                     │
│ ├─ BufferedInputStream│ ├─ BufferedReader                 │
│ └─ ...              │ │ └─ ...                            │
├─────────────────────┘ └───────────────────────────────────┘
│ OutputStream        │  │ Writer                            │
│ ├─ FileOutputStream │  │ ├─ FileWriter                     │
│ ├─ BufferedOutputStream│ ├─ BufferedWriter                 │
│ └─ ...              │  │ └─ ...                            │
└─────────────────────┘  └───────────────────────────────────┘
```

---

## 六、字节流与字符流对比表

|特性|字节流|字符流|
|---|---|---|
|处理单位|字节（byte）|字符（char）|
|处理数据类型|二进制数据（图片、视频、音频等）|文本数据（字符串、文件中的文字）|
|编码转换|不涉及编码转换|会根据编码（如 UTF-8、GBK）进行字符转换|
|效率|较高|较低（需要编码转换）|
|常用类|`FileInputStream` / `FileOutputStream`|`FileReader` / `FileWriter`|
|缓冲流|`BufferedInputStream` / `BufferedOutputStream`|`BufferedReader` / `BufferedWriter`|
---

## 七、总结

1. **字节流**：适合处理二进制文件（图片、视频等），不涉及编码转换。

2. **字符流**：适合处理文本文件，会自动进行编码转换，避免乱码。

3. **缓冲流**：可以提高读写效率，建议在实际开发中使用。

4. **关闭流**：使用完流后一定要关闭，释放资源（可以用 `try-with-resources` 自动关闭）。
