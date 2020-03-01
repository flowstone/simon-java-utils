package me.xueyao.collection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
		

public class StringUtil {

	/**
     * 判断一个字符串是否为null或空值.
     *
     * @param str 		指定的字符串
     * @return 			
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    
    /**
	 * 判断是否不是空
	 * 
     * @param str 		指定的字符串
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return str!=null && str.trim().length()!=0;
	}

    /**
	  * 是否是纯数字.
	  *
	  * @param str 		指定的字符串
	  * @return 		
	  */
	public static boolean isNumber(String str) {
		boolean isNumber = false;
		String expr = "^[0-9]+$";
		if (str.matches(expr)) {
			isNumber = true;
		}
		return isNumber;
	}
 	
 	/**
	  * 是否只是字母和数字.
	  *
	  * @param str 		指定的字符串
	  * @return 
	  */
 	public static boolean isNumberLetter(String str) {
 		boolean isNoLetter = false;
 		String expr = "^[A-Za-z0-9]+$";
 		if (str.matches(expr)) {
 			isNoLetter = true;
 		}
 		return isNoLetter;
 	}
 	
 	/**
     * 描述：手机号格式验证.
     *
     * @param str 指定的手机号码字符串
     * @return 是否为手机号码格式:是为true，否则false
     */
 	public static boolean isMobileNo(String str) {
 		boolean isMobileNo = false;
 		try {
 			//13开头
			//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
 			Pattern p = Pattern.compile("^(13|14|15|17|18)\\d{9}$");
			Matcher m = p.matcher(str);
			isMobileNo = m.matches();
		} catch (Exception e) {
			
		}
 		return isMobileNo;
 	}
 	
    /**
	  * 从输入流中获得String.
	  *
	  * @param is 输入流
	  * @return 获得的String
	  */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			//最后一个\n删除
			if(sb.indexOf("\n")!=-1 && sb.lastIndexOf("\n") == sb.length()-1){
				sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n")+1);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
     * 根据指定的字符把源字符串分割成一个数组  
     *   
     * @param src  
     * @return  
     */ 
    public static List<String> parseString2ListByCustomerPattern(String pattern, String src) {  
        if (src == null) {
        	 return null;  
        }  
        List<String> list = new ArrayList<String>();  
        String[] result = src.split(pattern);  
        for (int i = 0; i < result.length; i++) {  
            list.add(result[i]);  
        }  
        return list;  
    }
    
    /**
     * 将字符串转化为int
     * 
     * @param s
     * @return
     */
    public static int toInt(String s) {  
    	int result = 0;
    	if (!isEmpty(s)) {  
            try {  
            	result = Integer.parseInt(s);  
            } catch (Exception e) {  
                // 非数值字符串默认返回0
            }  
        }  
        return result;  
    } 
    
    

    /**
     * 将字符串转化为long
     * 
     * @param s
     * @return
     */
    public static long toLong(String s) {  
    	long result = 0;
    	if (!isEmpty(s)) {  
            try {  
            	result = Long.parseLong(s);  
            } catch (Exception e) {  
                
            }  
        }  
        return result;  
    }  
    
    /**
     * 将字符串转化为float
     * 
     * @param s
     * @return
     */
    public static Float toFloat(String s) {
		//0.00
    	float result = new Float(0);
    	if (!isEmpty(s)) {  
            try {  
            	result = Float.parseFloat(s);  
            } catch (Exception e) {  
                
            }  
        }  
        return result;  
    }  
    
    /**
     * 将字符串转化为double
     * 
     * @param s
     * @return
     */
    public static double toDouble(String s) {  
    	double result = 0;
    	if (!isEmpty(s)) {  
    		try {  
            	result = Double.parseDouble(s);  
            } catch (Exception e) {  
                
            }  
        }  
        return result;  
    }
    
    public static String getUUID() {
    	return UUID.randomUUID().toString();
    }
    
    public static String getSimpleUUID() {
    	return UUID.randomUUID().toString().replace("-", "");
    }
    


}
