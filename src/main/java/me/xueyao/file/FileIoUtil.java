package me.xueyao.file;


import lombok.extern.slf4j.Slf4j;
import me.xueyao.format.MathUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLEncoder;
import java.util.Properties;

/**
 * 文件IO操作：写文本，读文本
 *
 * @author Simon.Xue
 * @date 2016-06-10
 */
@Slf4j
public class FileIoUtil {
    /**
     * 文件大小单位：1KB。
     */
    public static final long ONE_KB = 1024;

    /**
     * 文件大小单位：1MB。
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * 文件大小单位：1GB。
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    private static final int BUFFER_SIZE = 4096;


    /**
     * 将文本内容写入文件
     *
     * @param folderName 文件夹名称
     * @param fileName   文件名称
     * @param text       文本内容
     * @param append     追加(true)
     */
    public static void writeText(String folderName, String fileName, String text, boolean append) throws IOException {
        try {
            if (FileUtil.creatFolder(folderName)) {
                FileWriter fw = new FileWriter(folderName + File.separator + fileName, append);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(text);
                log.info("写入文件成功!");
                bw.flush();
                bw.close();
            } else {
                //do nothing
            }
        } catch (IOException e) {
            log.error("folderName:{},fileName:{}, text:{}, append:{}", folderName, fileName, text, append);
            throw e;
        }
    }

    /**
     * 从文件中读取出文本
     *
     * @param filePath 文件绝对路径
     * @throws IOException
     */
    public static String readText(String filePath) throws IOException {
        String result = null;
        try {
            StringBuilder buffer = new StringBuilder();
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            String readStr = null;
            while ((readStr = bufferReader.readLine()) != null) {
                buffer.append(readStr);
            }
            bufferReader.close();
            result = buffer.toString();
            buffer.setLength(0);
        } catch (FileNotFoundException e) {
            log.error("文件不存在，filePath:{}", filePath);
            throw e;
        } catch (IOException e) {
            log.error("文件IO异常，filePath:{}", filePath);
            throw e;
        }
        return result;
    }

    /**
     * 把字符串写到文件中
     *
     * @param path   文件路径
     * @param str    字符串
     * @param append 是否追加，否的话会覆盖原来的内容
     */
    public static void writeString(String path, String str, boolean append) {
        FileWriter out = null;
        try {
            out = new FileWriter(path, append);
            out.write(str);
        } catch (IOException e) {
            throw new RuntimeException("Could not write String[" + path + "]", e);
        } finally {
            close(out);
        }
    }

    /**
     * 从文件中读取字符串，使用默认字符集
     *
     * @param path 文件路径
     * @return 文件内容的字符串
     */
    public static String readString(String path) {
        FileInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(path);
            out = new ByteArrayOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            return new String(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Could not read String[" + path + "]", e);
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * 从输入流中读取字符串，使用默认字符集
     *
     * @param in 输入流
     * @return 流内容的字符串
     */
    public static String readString(InputStream in) {
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            return new String(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Could not read stream", e);
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * 读取指定路径的Properties文件
     *
     * @param path 路径
     * @return Properties对象
     */
    public static Properties readProperties(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        Properties properties = new Properties();
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Could not read Properties[" + path + "]", e);
        } finally {
            close(in);
        }

        return properties;
    }

    /**
     * 从输入流读取Properties对象
     *
     * @param in 输入流
     * @return Properties对象
     */
    public static Properties readProperties(InputStream in) {
        Properties properties = new Properties();

        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Could not read Properties", e);
        } finally {
            close(in);
        }

        return properties;
    }

    /**
     * 把Properties对象写到指定路径的文件里
     *
     * @param path       路进
     * @param properties Properties对象
     */
    public static void writeProperties(String path, Properties properties) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(path);
            properties.store(out, null);
        } catch (IOException e) {
            throw new RuntimeException("Could not write Properties[" + path + "]", e);
        } finally {
            close(out);
        }
    }

    /**
     * 关闭输入流
     *
     * @param in 输入流
     */
    public static void close(InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * 关闭输出流
     *
     * @param out 输出流
     */
    public static void close(OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * 关闭 Reader
     *
     * @param in Reader
     */
    public static void close(Reader in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * 关闭 Writer
     *
     * @param out Writer
     */
    public static void close(Writer out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * 关闭 Closeable 对象。
     *
     * @param closeable Closeable 对象
     */
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
        }
    }


    /**
     * 复制给定的数组中的内容到输出流中。
     *
     * @param in  需要复制的数组
     * @param out 复制到的输出流
     * @throws IOException 当发生 I/O 异常时抛出
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * 复制file中的内容到输出流中
     * @param file 文件内容
     * @param out 输出流
     * @throws IOException
     */
    public static void copyFile(File file, OutputStream out) throws IOException {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                out.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            out.close();
        }

    }

    /**
     * 将以 byte 为单位的文件大小转换为一个可读性更好的文件大小，最终结果精确到一位小数。
     *
     * @param size 以 byte 为单位的文件大小
     * @return 更具可读性的文件大小（包括单位：GB、MB、KB、B），例如：102 B、1.5 KB、23.8 MB、34.2 GB
     */
    public static String byteCountToDisplaySize(long size) {
        String displaySize;

        if (size / ONE_GB > 0) {
            displaySize = MathUtils.div(size * 1.0, ONE_GB, 1) + " GB";
        } else if (size / ONE_MB > 0) {
            displaySize = MathUtils.div(size * 1.0, ONE_MB, 1) + " MB";
        } else if (size / ONE_KB > 0) {
            displaySize = MathUtils.div(size * 1.0, ONE_KB, 1) + " KB";
        } else {
            displaySize = String.valueOf(size) + " B";
        }

        return displaySize;
    }


    /**
     * 下载文件时，针对不同浏览器，进行附件名的编码
     *
     * @param filename 下载文件名
     * @param agent    客户端浏览器
     * @return 编码后的下载附件名
     * @throws IOException
     * @author : simon xue
     * @date : 2017年11月28日 下午19:58:05
     */
    public static String encodeDownloadFilename(String filename, String agent)
            throws IOException {
        // 火狐浏览器
        if (agent.contains("Firefox")) {
            filename = "=?UTF-8?B?"
                    + new BASE64Encoder().encode(filename.getBytes("utf-8"))
                    + "?=";
            filename = filename.replaceAll("\r\n", "");
        } else { // IE及其他浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        return filename;
    }


}
