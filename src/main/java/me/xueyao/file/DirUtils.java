package me.xueyao.file;
/**
* 根据文件名目录打散工具类
*/
public class DirUtils {
	public static String getDir(String name) {
		if (name != null) {
			int code = name.hashCode();
			return "/" + (code & 15) + "/" + (code >>> 4 & 15);
		}
		return null;
	}
}
