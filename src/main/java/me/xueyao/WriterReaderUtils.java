package me.xueyao;

import java.io.Closeable;
import java.io.IOException;

/**
 * @description 读写操作工具类
 * @author Yao Xue
 * @date Jul 31, 2017 5:06:31 PM
 */
public class WriterReaderUtils {
    
    /**
     * @description: 关闭输入输入流对象
     * @param ios 输入输出流对象 
     */
    public static void closeTo(Closeable ... ios) {
        for (Closeable io : ios) {
            if (io != null) {
                try {
                    io.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
