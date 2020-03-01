package me.xueyao.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**	
 * md5工具
 * 
 * @author Simon.Xue
 * @date 2016-06-10
 *
 */
public class MD5 {
	
	 /**
     * 计算字符串的MD5值(UTF-8)
     *
     * @param source
     * @return MD5值
     */
    public static String getMD5(String source) {
        return getMD5(source, null);
    }
	
    /**
     * 计算字符串的MD5值(UTF-8)
     *
     * @param source
     * @return MD5值
     */
    public static String getMD5(String source, String update) {
        return getMD5(source, update, Charset.forName("UTF-8"));
    }
    
    /**
     * 计算32位MD5值
     */
    public static String getMD5(String source, String update, Charset charset) {
    	String hashValue = "";
        try {
        	MessageDigest md = MessageDigest.getInstance("MD5");
        	md.update(source.getBytes(charset));
        	if (update != null) {
        		hashValue = byte2hex(md.digest(update.getBytes(charset)));
            } else {
            	hashValue = byte2hex(md.digest());
            }
        } catch (NoSuchAlgorithmException e) {
        	System.err.println("计算MD5值出错:" + e.getMessage());
        }
        return hashValue;
    }
    
    /** 
     * 字节数组转十六进制字符串 
     */
    public static String byte2hex(byte[] b) {
    	if (b == null) {
    		return "";
		}
    	String hex = "";
    	String tmp = "";
        for (int n = 0; n < b.length; n++) {
            tmp = (Integer.toHexString(b[n] & 0xff));
            if (tmp.length() == 1) {
            	hex = hex + "0" + tmp;
            } else {
            	hex = hex + tmp;
            }
        }
        return hex.toLowerCase();
    }
    
    /**
     * 计算文件md5
     */
    public static String getFileMD5(String fileStr) {
		File file = new File(fileStr);
    	if (!file.isFile()) {
    		System.err.println("文件不存在,fileStr:" + fileStr);
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
			System.err.println("计算md5出错,fileStr:" + fileStr);
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}


}
