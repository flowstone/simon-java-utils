package me.xueyao.file;

import me.xueyao.date.DateTools;
import me.xueyao.MathUtils;
import me.xueyao.RandomUtils;
import me.xueyao.Validators;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 文件工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-11-22 上午9:52:05 $
 */
public abstract class FileUtils {

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



    private static final String FOLDER_SEPARATOR = "/";
	private static final char EXTENSION_SEPARATOR = '.';
    /**
     * 递归取得某个目录下所有的文件
     * 
     * @param path
     *            目录
     * @return 文件List
     */
    public static List<File> getNestedFiles(String path) {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Nonexistent directory[" + path + "]");
        }

        return new Recursiver().getFileList(directory);
    }

    /**
     * 把字符串写到文件中
     * 
     * @param path
     *            文件路径
     * @param str
     *            字符串
     * @param append
     *            是否追加，否的话会覆盖原来的内容
     */
    public static void writeString(String path, String str, boolean append) {
        FileWriter out = null;
        try {
            out = new FileWriter(path, append);
            out.write(str);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not write String[" + path + "]", e);
        }
        finally {
            close(out);
        }
    }

    /**
     * 从文件中读取字符串，使用默认字符集
     * 
     * @param path
     *            文件路径
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
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read String[" + path + "]", e);
        }
        finally {
            close(in);
            close(out);
        }
    }

    /**
     * 从输入流中读取字符串，使用默认字符集
     * 
     * @param in
     *            输入流
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
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read stream", e);
        }
        finally {
            close(in);
            close(out);
        }
    }

    /**
     * 读取指定路径的Properties文件
     * 
     * @param path
     *            路径
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
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read Properties[" + path + "]", e);
        }
        finally {
            close(in);
        }

        return properties;
    }

    /**
     * 从输入流读取Properties对象
     * 
     * @param in
     *            输入流
     * @return Properties对象
     */
    public static Properties readProperties(InputStream in) {
        Properties properties = new Properties();

        try {
            properties.load(in);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read Properties", e);
        }
        finally {
            close(in);
        }

        return properties;
    }

    /**
     * 把Properties对象写到指定路径的文件里
     * 
     * @param path
     *            路进
     * @param properties
     *            Properties对象
     */
    public static void writeProperties(String path, Properties properties) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(path);
            properties.store(out, null);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not write Properties[" + path + "]", e);
        }
        finally {
            close(out);
        }
    }

    /**
     * 关闭输入流
     * 
     * @param in
     *            输入流
     */
    public static void close(InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        }
        catch (IOException e) {
        }
    }

    /**
     * 关闭输出流
     * 
     * @param out
     *            输出流
     */
    public static void close(OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        }
        catch (IOException e) {
        }
    }

    /**
     * 关闭 Reader
     * 
     * @param in
     *            Reader
     */
    public static void close(Reader in) {
        try {
            if (in != null) {
                in.close();
            }
        }
        catch (IOException e) {
        }
    }

    /**
     * 关闭 Writer
     * 
     * @param out
     *            Writer
     */
    public static void close(Writer out) {
        try {
            if (out != null) {
                out.close();
            }
        }
        catch (IOException e) {
        }
    }

    /**
     * 关闭 Closeable 对象。
     * 
     * @param closeable
     *            Closeable 对象
     */
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (IOException e) {
        }
    }

    /**
     * 取得文件的后缀名。
     * 
     * @param fileName
     *            文件名
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
     * 复制给定的数组中的内容到输出流中。
     * 
     * @param in
     *            需要复制的数组
     * @param out
     *            复制到的输出流
     * @throws IOException
     *             当发生 I/O 异常时抛出
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        try {
            out.write(in);
        }
        finally {
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
    }

    /**
     * 将以 byte 为单位的文件大小转换为一个可读性更好的文件大小，最终结果精确到一位小数。
     * 
     * @param size
     *            以 byte 为单位的文件大小
     * @return 更具可读性的文件大小（包括单位：GB、MB、KB、B），例如：102 B、1.5 KB、23.8 MB、34.2 GB
     */
    public static String byteCountToDisplaySize(long size) {
        String displaySize;

        if (size / ONE_GB > 0) {
            displaySize = MathUtils.div(size * 1.0, ONE_GB, 1) + " GB";
        }
        else if (size / ONE_MB > 0) {
            displaySize = MathUtils.div(size * 1.0, ONE_MB, 1) + " MB";
        }
        else if (size / ONE_KB > 0) {
            displaySize = MathUtils.div(size * 1.0, ONE_KB, 1) + " KB";
        }
        else {
            displaySize = String.valueOf(size) + " B";
        }

        return displaySize;
    }

    private static class Recursiver {

        private static ArrayList<File> files = new ArrayList<File>();

        public List<File> getFileList(File file) {
            File children[] = file.listFiles();

            for (int i = 0; i < children.length; i++) {
                if (children[i].isDirectory()) {
                    new Recursiver().getFileList(children[i]);
                }
                else {
                    files.add(children[i]);
                }
            }

            return files;
        }
    }



/**
	 * @desc:判断指定路径是否存在，如果不存在，根据参数决定是否新建
	 * @autor:chenssy
	 * @date:2014年8月7日
	 *
	 * @param filePath
	 * 			指定的文件路径
	 * @param isNew
	 * 			true：新建、false：不新建
	 * @return 存在返回TRUE，不存在返回FALSE
	 */
	public static boolean isExist(String filePath,boolean isNew){
		File file = new File(filePath);
		if(!file.exists() && isNew){    
			return file.mkdirs();    //新建文件路径
		}
		return false;
	}
	
	/**
	 * 获取文件名，构建结构为 prefix + yyyyMMddHH24mmss + 10位随机数 + suffix + .type
	 * @autor:chenssy
	 * @date:2014年8月11日
	 *
	 * @param type
	 * 				文件类型
	 * @param prefix
	 * 				前缀
	 * @param suffix
	 * 				后缀
	 * @return
	 */
	public static String getFileName(String type,String prefix,String suffix){
		String date = DateTools.getCurrentTime("yyyyMMddHH24mmss");   //当前时间
		String random = RandomUtils.generateNumberString(10);   //10位随机数
		
		//返回文件名  
		return prefix + date + random + suffix + "." + type;
	}
	
	/**
	 * 获取文件名，文件名构成:当前时间 + 10位随机数 + .type
	 * @autor:chenssy
	 * @date:2014年8月11日
	 *
	 * @param type
	 * 				文件类型
	 * @return
	 */
	public static String getFileName(String type){
		return getFileName(type, "", "");
	}
	
	/**
	 * 获取文件名，文件构成：当前时间 + 10位随机数
	 * @autor:chenssy
	 * @date:2014年8月11日
	 *
	 * @return
	 */
	public static String getFileName(){
		String date = DateTools.getCurrentTime("yyyyMMddHH24mmss");   //当前时间
		String random = RandomUtils.generateNumberString(10);   //10位随机数
		
		//返回文件名  
		return date + random;
	}
	
	/**
	 * 获取指定文件的大小
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 *
	 * @author:chenssy
	 * @date : 2016年4月30日 下午9:10:12
	 */
	@SuppressWarnings("resource")
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
		}
		return size;
	}
	
	/**
	 * 删除所有文件，包括文件夹
	 * 
	 * @author : chenssy
	 * @date : 2016年5月23日 下午12:41:08
	 *
	 * @param dirpath
	 */
    public void deleteAll(String dirpath) {  
    	 File path = new File(dirpath);  
         try {  
             if (!path.exists())  
                 return;// 目录不存在退出   
             if (path.isFile()) // 如果是文件删除   
             {  
                 path.delete();  
                 return;  
             }  
             File[] files = path.listFiles();// 如果目录中有文件递归删除文件   
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
     * @author : chenssy
     * @date : 2016年5月23日 下午12:41:59
     *
     * @param inputFile
     * 						源文件
     * @param outputFile
     * 						目的文件
     * @param isOverWrite
     * 						是否覆盖文件
     * @throws IOException
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
     * @author : chenssy
     * @date : 2016年5月23日 下午12:43:24
     *
     * @param inputFile
     * 						源文件
     * @param outputFile
     * 						目的文件
     * @param isOverWrite
     * 						是否覆盖文件
     * @throws IOException
     */
    private static void copyPri(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
		if (inputFile.isFile()) {		//文件
			copySimpleFile(inputFile, outputFile, isOverWrite);
		} else {
			if (!outputFile.exists()) {		//文件夹	
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
     * @author : chenssy
     * @date : 2016年5月23日 下午12:44:07
     *
     * @param inputFile
     * 						源文件
     * @param outputFile
     * 						目的文件
     * @param isOverWrite
     * 						是否覆盖
     * @throws IOException
     */
    private static void copySimpleFile(File inputFile, File outputFile,
			boolean isOverWrite) throws IOException {
		if (outputFile.exists()) {
			if (isOverWrite) {		//可以覆盖
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
		int read = 0;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		in.close();
		out.close();
	}
    
    /**
     * 获取文件的MD5
     * 
     * @author : chenssy
     * @date : 2016年5月23日 下午12:50:38
     *
     * @param file
     * 				文件
     * @return
     */
	public static String getFileMD5(File file){
		if (!file.exists() || !file.isFile()) {  
            return null;  
        }  
        MessageDigest digest = null;
        FileInputStream in = null;  
        byte buffer[] = new byte[1024];  
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
	 * 
	 * @author : chenssy
	 * @date : 2016年5月23日 下午12:51:59
	 *
	 * @param file
	 * 				文件
	 * @return
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
	 * 文件重命名
	 * 
	 * @author : chenssy
	 * @date : 2016年5月23日 下午12:56:05
	 *
	 * @param oldPath
	 * 					老文件
	 * @param newPath
	 * 					新文件
	 */
    public boolean renameDir(String oldPath, String newPath) {  
        File oldFile = new File(oldPath);// 文件或目录   
        File newFile = new File(newPath);// 文件或目录   
        
        return oldFile.renameTo(newFile);// 重命名   
    }
	
	/**
	 * 下载文件时，针对不同浏览器，进行附件名的编码
	 * @author : simon xue
	 * @date : 2017年11月28日 下午19:58:05
	 * @param filename
	 *            下载文件名
	 * @param agent
	 *            客户端浏览器
	 * @return 编码后的下载附件名
	 * @throws IOException
	 */
	public static String encodeDownloadFilename(String filename, String agent)
			throws IOException {
		if (agent.contains("Firefox")) { // 火狐浏览器
			filename = "=?UTF-8?B?"
					+ new BASE64Encoder().encode(filename.getBytes("utf-8"))
					+ "?=";
			filename = filename.replaceAll("\r\n", "");
		} else { // IE及其他浏览器
			filename = URLEncoder.encode(filename, "utf-8");
			filename = filename.replace("+"," ");
		}
		return filename;
	}

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
        if (!f.exists()) {
            return;
        }

        // 获得文件数组
        File files[] = f.listFiles(new FileFilter() {
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
    public static void getFileType(File f, Map<String,Integer> map) {
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
