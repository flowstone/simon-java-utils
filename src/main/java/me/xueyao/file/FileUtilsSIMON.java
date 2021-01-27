package me.xueyao.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

/**
 * @description 文件操作工具类
 * @author Yao Xue
 * @date Jul 31, 2017 5:13:28 PM
 */
public class FileUtilsSIMON {
    /**
     * 获得文件夹的大小
     * @param dir  文件对象
     * @return  文件夹的大小(单位:字节)
     */
    public static long folderSize(File dir){
        long length = 0;
        // 获得文件数组
        File files[] = dir.listFiles();
        // 遍历数组
        for (File file : files) {
            // 判断是否是文件夹
            if(file.isDirectory()) {
                length += folderSize(file);
            } else {
                length += file.length();
            }
        }
        return length;
    }
    
    /**
     * 过滤器筛选将指定文件夹下的小于size的小文件获取并打印
     * @param f  文件对象
     * @param size  要过滤文件的大小(单位:KB)
     */
    public static void filterFile(File f ,  final Integer size){
        if(f.isFile()){
            System.out.println(f + "不是文件夹");
            return;
        }
        if(!f.exists()) return;
        // 获得文件数组
        File files[] = f.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                // 判断是否是文件夹，如果是则返回true
                if(pathname.isDirectory()) return true;
                // 判断是否是隐藏文件
                if(pathname.isHidden()) return false;
                // 获得文件大小
                long length = pathname.length();
                if(length / 1024 < size) return true;
                return false;
            }
        });
        
        // 遍历数组
        for (File file : files) {
            if(file.isDirectory()) {
                filterFile(file,size);
                continue;
            }
            System.out.println(file);
        }
    }
    
    /**
     * 删除某个文件下的所有文件(包括子文件夹)
     * @param dir  文件对象
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
     * 获取文件类型及其个数
     * @param f  文件对象
     * @param map  map集合,存扩展名及个数
     */
    public static void getFileType(File f,Map<String,Integer> map) {
        if(f.isDirectory()) {
            File files[] = f.listFiles();
            for (File file : files) {
                getFileType(file,map);
            }
        } else {
            // 获得文件名
            String fileName = f.getName();
            // 获得文件扩展名
            String key = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 判断map是否包含key
            if(map.containsKey(key)){
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }
    }
    
    /**
     * 打印目录结构
     * @param file  文件对象
     * @param level 目录的层次
     */
    public static void printDir(File file, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(file.getName());
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                printDir(f,level + 1);
            }
        } 
    }
}
