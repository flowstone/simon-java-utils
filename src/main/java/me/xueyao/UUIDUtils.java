package me.xueyao;

import java.util.UUID;
/**
* 防止文件名重复UUID重写文件名的方法
*/
public class UUIDUtils {
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
