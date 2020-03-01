package me.xueyao.file;

import lombok.extern.slf4j.Slf4j;
import me.xueyao.RandomUtils;
import me.xueyao.Validators;
import me.xueyao.date.DateTools;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件的基本操作：创建，删除等
 *
 * @author Simon.Xue
 * @date 2016-06-10
 */
@Slf4j
public class FileUtil {

    private static final String FOLDER_SEPARATOR = "/";
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 创建文件夹
     *
     * @param folderName 文件路径名称
     */
    public static boolean creatFolder(String folderName) {
        File folder = new File(folderName);
        boolean result = false;
        if (!folder.exists()) {
            //创建多级文件夹
            result = folder.mkdirs();
            if (result) {
                log.info("创建文件夹成功,folderName:{}", folderName);
            } else {
                log.warn("创建文件夹失败,folderName:{}", folderName);
            }
        } else {
            result = true;
            log.error("文件夹已存在,folderName:{}", folderName);
        }
        return result;
    }

    /**
     * 创建文件
     *
     * @param folderName 文件路径
     * @param fileName   文件名称
     */
    public static boolean createFile(String folderName, String fileName) throws IOException {
        boolean result = false;
        if (creatFolder(folderName)) {
            File file = new File(folderName + File.separator + fileName);
            //文件不存在
            if (!file.exists()) {
                try {
                    result = file.createNewFile();
                    log.info("创建文件成功,filePath:{}", file.getAbsolutePath());
                } catch (IOException e) {
                    log.error("创建文件失败 , filePath : {}", file.getAbsolutePath());
                }
            } else {
                //文件存在
                log.warn("文件已经存在 , filePath：{}", file.getAbsolutePath());
            }
        } else {
            //do nothing
        }
        return result;
    }


    /**
     * 文件重命名
     *
     * @param folderName  文件路径
     * @param oldFileName 文件旧名称
     * @param newFileName 文件新名称
     */
    public static boolean renameFile(String folderName, String oldFileName, String newFileName) {
        boolean result = false;
        File file = new File(folderName + File.separator + oldFileName);
        // 文件存在
        if (file.exists()) {
            File newFile = new File(folderName + File.separator + newFileName);
            result = file.renameTo(newFile);
            log.info("文件重命名成功,filePath:{}", file.getAbsolutePath());
        } else {
            //文件不存在
            log.error("文件不存在，无法重命名,filePath:{}", file.getAbsolutePath());
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param folderName 文件路径
     * @param fileName   文件名称
     */
    public static boolean deleteFile(String folderName, String fileName) {
        boolean result = false;
        File file = new File(folderName + File.separator + fileName);
        // 文件存在
        if (file.exists()) {
            result = file.delete();
            log.info("文件删除成功,filePath:{}", file.getAbsolutePath());
        } else { //文件不存在
            log.warn("文件不存在，无法删除,filePath:{}", file.getAbsolutePath());
        }
        return result;
    }

    /**
     * 根据文件名目录打散
     *
     * @param name 文件名
     * @return
     */
    public static String getDir(String name) {
        if (name != null) {
            int code = name.hashCode();
            return "/" + (code & 15) + "/" + (code >>> 4 & 15);
        }
        return null;
    }

    /**
     * 递归取得某个目录下所有的文件
     *
     * @param path 目录
     * @return 文件List
     */
    public static List<File> getNestedFiles(String path) {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Nonexistent directory[" + path + "]");
        }

        return new Recursiver().getFileList(directory);
    }

    private static class Recursiver {
        private static List<File> files = new ArrayList<>();

        public List<File> getFileList(File file) {
            File[] children = file.listFiles();

            for (int i = 0; i < children.length; i++) {
                if (children[i].isDirectory()) {
                    new Recursiver().getFileList(children[i]);
                } else {
                    files.add(children[i]);
                }
            }
            return files;
        }
    }

    /**
     * 打印目录结构
     *
     * @param file  文件对象
     * @param level 目录的层次
     */
    public static void printDir(File file, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(file.getName());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                printDir(f, level + 1);
            }
        }
    }

    /**
     * 获取文件类型及其个数
     *
     * @param f   文件对象
     * @param map map集合,存扩展名及个数
     */
    public static void getFileType(File f, Map<String, Integer> map) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                getFileType(file, map);
            }
        } else {
            // 获得文件名
            String fileName = f.getName();
            // 获得文件扩展名
            String key = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 判断map是否包含key
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }
    }

    /**
     * 删除某个文件下的所有文件(包括子文件夹)
     *
     * @param dir 文件对象
     */
    public static void delete(File dir) {
        // 获取所有文件
        File[] files = dir.listFiles();
        // 遍历子目录,删除子目录
        for (File file : files) {
            if (file.isFile()) {
                boolean success = file.delete();
                // 判断是否删除成功,主要用于测试自己写的代码
                if (success) {
                    System.out.println(file + "删除成功");
                } else {
                    System.out.println(file + "正在使用,删除失败");
                }
            } else {
                // 如果是文件夹递归删除
                delete(file);
            }
        }

        // 来到这里说明,该文件夹为空了.
        boolean success = dir.delete();
        // 判断是否删除成功,主要用于测试自己写的代码
        if (success) {
            System.out.println(dir + "删除成功");
        } else {
            System.out.println(dir + "正在使用,删除失败");
        }
    }

    /**
     * 过滤器筛选将指定文件夹下的小于size的小文件获取并打印
     *
     * @param f    文件对象
     * @param size 要过滤文件的大小(单位:KB)
     */
    public static void filterFile(File f, final Integer size) {
        if (f.isFile()) {
            System.out.println(f + "不是文件夹");
            return;
        }
        if (!f.exists()) {
            return;
        }

        // 获得文件数组
        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                // 判断是否是文件夹，如果是则返回true
                if (pathname.isDirectory()) {
                    return true;
                }
                // 判断是否是隐藏文件
                if (pathname.isHidden()) {
                    return false;
                }
                // 获得文件大小
                long length = pathname.length();
                if (length / 1024 < size) {
                    return true;
                }
                return false;
            }
        });

        // 遍历数组
        for (File file : files) {
            if (file.isDirectory()) {
                filterFile(file, size);
                continue;
            }
            System.out.println(file);
        }
    }

    /**
     * 获得文件夹的大小
     *
     * @param dir 文件对象
     * @return 文件夹的大小(单位 : 字节)
     */
    public static long folderSize(File dir) {
        long length = 0;
        // 获得文件数组
        File[] files = dir.listFiles();
        // 遍历数组
        for (File file : files) {
            // 判断是否是文件夹
            if (file.isDirectory()) {
                length += folderSize(file);
            } else {
                length += file.length();
            }
        }
        return length;
    }

    /**
     * 获取文件的MD5
     *
     * @param file 文件
     * @return
     * @author : chenssy
     * @date : 2016年5月23日 下午12:50:38
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }


    /**
     * 获取文件的后缀
     * @param file 文件
     * @deprecated
     * @date : 2016年5月23日 下午12:51:59
     */
    public static String getFileSuffix(String file) {
        if (file == null) {
            return null;
        }
        int extIndex = file.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }
        int folderIndex = file.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }
        return file.substring(extIndex + 1);
    }

    /**
     * 取得文件的后缀名。
     * @param fileName 文件名
     * @return 后缀名
     */
    public static String getExtension(String fileName) {
        if (Validators.isEmpty(fileName)) {
            return null;
        }

        int pointIndex = fileName.lastIndexOf(".");
        return pointIndex > 0 && pointIndex < fileName.length() ? fileName.substring(pointIndex + 1).toLowerCase()
                : null;
    }

    /**
     * @param filePath 指定的文件路径
     * @param isNew    true：新建、false：不新建
     * @return 存在返回TRUE，不存在返回FALSE
     * @desc:判断指定路径是否存在，如果不存在，根据参数决定是否新建
     * @date:2014年8月7日
     */
    public static boolean isExist(String filePath, boolean isNew) {
        File file = new File(filePath);
        if (!file.exists() && isNew) {
            //新建文件路径
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 获取文件名，文件名构成:当前时间 + 10位随机数 + .type
     *
     * @param type 文件类型
     * @return
     * @autor:chenssy
     * @date:2014年8月11日
     */
    public static String getFileName(String type) {
        return getFileName(type, "", "");
    }

    /**
     * 获取文件名，构建结构为 prefix + yyyyMMddHH24mmss + 10位随机数 + suffix + .type
     * @param type   文件类型
     * @param prefix 前缀
     * @param suffix 后缀
     * @return
     * @date:2014年8月11日
     */
    public static String getFileName(String type, String prefix, String suffix) {
        //当前时间
        String date = DateTools.getCurrentTime("yyyyMMddHH24mmss");
        //10位随机数
        String random = RandomUtils.generateNumberString(10);

        //返回文件名
        return prefix + date + random + suffix + "." + type;
    }

    /**
     * 获取文件名，文件构成：当前时间 + 10位随机数
     *
     * @return
     * @date:2014年8月11日
     */
    public static String getFileName() {
        //当前时间
        String date = DateTools.getCurrentTime("yyyyMMddHH24mmss");
        //10位随机数
        String random = RandomUtils.generateNumberString(10);

        //返回文件名
        return date + random;
    }

    /**
     * 删除所有文件，包括文件夹
     *
     * @param dirpath
     * @date : 2016年5月23日 下午12:41:08
     */
    public void deleteAll(String dirpath) {
        File path = new File(dirpath);
        try {
            if (!path.exists()) {
                return;// 目录不存在退出
            }
            // 如果是文件删除
            if (path.isFile())
            {
                path.delete();
                return;
            }
            // 如果目录中有文件递归删除文件
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i].getAbsolutePath());
            }
            path.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件或者文件夹
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖文件
     * @throws IOException
     * @date : 2016年5月23日 下午12:41:59
     */
    public static void copy(File inputFile, File outputFile, boolean isOverWrite)
            throws IOException {
        if (!inputFile.exists()) {
            throw new RuntimeException(inputFile.getPath() + "源目录不存在!");
        }
        copyPri(inputFile, outputFile, isOverWrite);
    }

    /**
     * 复制文件或者文件夹
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖文件
     * @throws IOException
     * @date : 2016年5月23日 下午12:43:24
     */
    private static void copyPri(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
        if (inputFile.isFile()) {
            //文件
            copySimpleFile(inputFile, outputFile, isOverWrite);
        } else {
            //文件夹
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }
            // 循环子文件夹
            for (File child : inputFile.listFiles()) {
                copy(child, new File(outputFile.getPath() + "/" + child.getName()), isOverWrite);
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖
     * @throws IOException
     * @date : 2016年5月23日 下午12:44:07
     */
    private static void copySimpleFile(File inputFile, File outputFile,
                                       boolean isOverWrite) throws IOException {
        if (outputFile.exists()) {
            //可以覆盖
            if (isOverWrite) {
                if (!outputFile.delete()) {
                    throw new RuntimeException(outputFile.getPath() + "无法覆盖！");
                }
            } else {
                // 不允许覆盖
                return;
            }
        }
        InputStream in = new FileInputStream(inputFile);
        OutputStream out = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();
    }


    /**
     * 文件重命名
     *
     * @param oldPath 老文件
     * @param newPath 新文件
     * @date : 2016年5月23日 下午12:56:05
     */
    public boolean renameDir(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        return oldFile.renameTo(newFile);
    }

    /**
     * 获取指定文件的大小
     *
     * @param file
     * @return
     * @throws Exception
     * @date : 2016年4月30日 下午9:10:12
     */
    @SuppressWarnings("resource")
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

}
