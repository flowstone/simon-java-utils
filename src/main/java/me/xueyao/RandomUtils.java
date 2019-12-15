package me.xueyao;

import java.util.Random;

/**
 * 生成随机数字、字符串的工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-11-22 上午10:10:13 $
 */
public abstract class RandomUtils {

    /**
     * 没有添加 I、O 的原因是避免和数字 1、0 混淆
     */
    private static final String ALPHA_NUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";

    private static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
	private static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
	private static final String NUMBER_CHAR = "0123456789";
    /**
     * 产生一个固定范围（min-max 之间）的随机正整数。
     * 
     * @param min
     *            最小值，
     * @param max
     *            最大值
     * @return min-max 之间的随机整数，包括 min、max
     */
    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * 产生固定长度的随机数字串。
     * 
     * @param length
     *            长度
     * @return 随机数字串
     */
    public static String getRandomNum(int length) {
        return (Double.toString(Math.random())).substring(2, (2 + length));
    }

    /**
     * 产生固定长度的随机字母数字串，其中字母为大写方式。
     * 
     * @param length
     *            长度
     * @return 随机字母数字串
     */
    public static String getRandomStr(int length) {
        char[] randomBytes = new char[length];
        for (int i = 0; i < length; i++) {
            randomBytes[i] = ALPHA_NUMERIC.charAt(getRandomInt(0, ALPHA_NUMERIC.length() - 1));
        }
        return new String(randomBytes);
    }

    /**
     * 产生固定长度的随机字母数字串，其中字母为小写方式。
     * 
     * @param length
     *            长度
     * @return 随机字母数字串
     */
    public static String getRandomStrLowerCase(int length) {
        return getRandomStr(length).toLowerCase();
    }


     /**
     * 获取定长的随机数，包含大小写、数字
     * @autor:chenssy
     * @date:2014年8月11日
     *
     * @param length
     * 				随机数长度
     * @return
     */
    public static String generateString(int length) { 
        StringBuffer sb = new StringBuffer(); 
        Random random = new Random();
        for (int i = 0; i < length; i++) { 
                sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length()))); 
        } 
        return sb.toString(); 
    } 
    
    /**
     * 获取定长的随机数，包含大小写字母
     * @autor:chenssy
     * @date:2014年8月11日
     *
     * @param length
     * 				随机数长度
     * @return
     */
    public static String generateMixString(int length) { 
        StringBuffer sb = new StringBuffer(); 
        Random random = new Random(); 
        for (int i = 0; i < length; i++) { 
                sb.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length()))); 
        } 
        return sb.toString(); 
    } 
    
    /**
     * 获取定长的随机数，只包含小写字母
     * @autor:chenssy
     * @date:2014年8月11日
     *
     * @param length	
     * 				随机数长度
     * @return
     */
    public static String generateLowerString(int length) { 
        return generateMixString(length).toLowerCase(); 
    } 
    
    /**
     * 获取定长的随机数，只包含大写字母
     * @autor:chenssy
     * @date:2014年8月11日
     *
     * @param length
     * 				随机数长度
     * @return
     */
    public static String generateUpperString(int length) { 
        return generateMixString(length).toUpperCase(); 
    } 
    
    /**
     * 获取定长的随机数，只包含数字
     * @autor:chenssy
     * @date:2014年8月11日
     *
     * @param length
     * 				随机数长度
     * @return
     */
    public static String generateNumberString(int length){
    	StringBuffer sb = new StringBuffer(); 
        Random random = new Random(); 
        for (int i = 0; i < length; i++) { 
                sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length()))); 
        } 
        return sb.toString(); 
    }
    
}
