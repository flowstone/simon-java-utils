package me.xueyao;

import me.xueyao.validate.Validators;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符串工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-11-22 上午9:46:33 $
 */
public abstract class StringUtils {

    public static final String EMPTY = "";
    public static final String SEPARATOR_MULTI = ";";
    public static final String SEPARATOR_SINGLE = "#";
    public static final String SQL_REPLACE = "_";

    private static final String BOOLEAN_TRUE_STRING = "true";
    private static final String BOOLEAN_FALSE_STRING = "false";
    private static final String BOOLEAN_TRUE_NUMBER = "1";
    private static final String BOOLEAN_FALSE_NUMBER = "0";

    /**
     * 在str右边加入足够多的addStr字符串
     * 
     * @param str
     * @param addStr
     * @param length
     * @return
     */
    public static String addStringRight(String str, String addStr, int length) {
        StringBuilder builder = new StringBuilder(str);
        while (builder.length() < length) {
            builder.append(addStr);
        }
        return builder.toString();
    }

    /**
     * 在字符串str拼接分隔符regex和字符串sub
     * 
     * @param str
     * @param sub
     * @param regex
     * @return
     */
    public static String addSubString(String str, String sub, String regex) {
        return Validators.isEmpty(str) ? sub : str + regex + sub;
    }

    /**
     * 在字符串str右边补齐0直到长度等于length
     * 
     * @param str
     * @param length
     * @return
     */
    public static String addZeroRight(String str, int length) {
        return addStringRight(str, "0", length);
    }

    /**
     * 计算字符串str中字符sub的个数
     * 
     * @param str
     * @param sub
     * @return
     */
    public static int charCount(String str, char sub) {
        int charCount = 0;
        int fromIndex = 0;

        while ((fromIndex = str.indexOf(sub, fromIndex)) != -1) {
            fromIndex++;
            charCount++;
        }
        return charCount;
    }

    /**
     * 计算字符串str右边出现多少次sub
     * 
     * @param str
     * @param sub
     * @return
     */
    public static int charCountRight(String str, String sub) {
        if (str == null) {
            return 0;
        }

        int charCount = 0;
        String subStr = str;
        int currentLength = subStr.length() - sub.length();
        while (currentLength >= 0 && subStr.substring(currentLength).equals(sub)) {
            charCount++;
            subStr = subStr.substring(0, currentLength);
            currentLength = subStr.length() - sub.length();
        }
        return charCount;
    }

    /**
     * <p>
     * Counts how many times the substring appears in the larger String.
     * </p>
     * 
     * <p>
     * A <code>null</code> or empty ("") String input returns <code>0</code>.
     * </p>
     * 
     * <pre>
     *   StringUtils.countMatches(null, *)                           = 0
     *   StringUtils.countMatches(&quot;&quot;, *)                   = 0
     *   StringUtils.countMatches(&quot;abba&quot;, null)            = 0
     *   StringUtils.countMatches(&quot;abba&quot;, &quot;&quot;)    = 0
     *   StringUtils.countMatches(&quot;abba&quot;, &quot;a&quot;)   = 2
     *   StringUtils.countMatches(&quot;abba&quot;, &quot;ab&quot;)  = 1
     *   StringUtils.countMatches(&quot;abba&quot;, &quot;xxx&quot;) = 0
     * </pre>
     * 
     * @param str
     *            the String to check, may be null
     * @param sub
     *            the substring to count, may be null
     * @return the number of occurrences, 0 if either String is <code>null</code>
     */
    public static int countMatches(String str, String sub) {
        if (Validators.isEmpty(str) || Validators.isEmpty(sub)) {
            return 0;
        }

        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     * 截取固定长度的字符串，剩余部分真实长度不会超过len，超长部分用suffix代替。
     * 
     * @param str
     * @param len
     * @param suffix
     * @return
     * @deprecated 已经被 {@link #cutOut(String, int, String)} 取代，该方法的显示格式会更整齐。
     */
    @Deprecated
    public static String cutOff(String str, int len, String suffix) {
        if (Validators.isEmpty(str)) {
            return str;
        }

        int byteIndex = 0;
        int charIndex = 0;

        while (charIndex < str.length() && byteIndex < len) {
            if (str.charAt(charIndex) >= 256) {
                byteIndex += 2;
            }
            else {
                byteIndex++;
            }

            charIndex++;
        }
        return byteIndex < len ? str.substring(0, charIndex) : str.substring(0, charIndex) + suffix;
    }

    /**
     * 截取固定长度的字符串，超长部分用suffix代替，最终字符串真实长度不会超过maxLength.
     * 
     * @param str
     * @param maxLength
     * @param suffix
     * @return
     */
    public static String cutOut(String str, int maxLength, String suffix) {
        if (Validators.isEmpty(str)) {
            return str;
        }

        int byteIndex = 0;
        int charIndex = 0;

        while (charIndex < str.length() && byteIndex <= maxLength) {
            char c = str.charAt(charIndex);
            if (c >= 256) {
                byteIndex += 2;
            }
            else {
                byteIndex++;
            }
            charIndex++;
        }

        if (byteIndex <= maxLength) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, charIndex));
        sb.append(suffix);

        while (getRealLength(sb.toString()) > maxLength) {
            sb.deleteCharAt(--charIndex);
        }

        return sb.toString();
    }

    /**
     * 在字符串str左边补齐0直到长度等于length
     * 
     * @param str
     * @param len
     * @return
     */
    public static String enoughZero(String str, int len) {
        while (str.length() < len) {
            str = "0" + str;
        }
        return str;
    }

    /**
     * 用来显示异常信息的html过滤器
     * 
     * @param value
     * @return
     */
    public static String exceptionFilter(String value) {
        return Validators.isEmpty(value) ? "" : value.replaceAll("\n", "<br>").replaceAll("\t", "&nbsp; &nbsp; ");
    }

    /**
     * @param text
     *            将要被格式化的字符串 <br>
     *            eg:参数一:{0},参数二:{1},参数三:{2}
     * 
     * @param args
     *            将替代字符串中的参数,些参数将替换{X} <br>
     *            eg:new Object[] { "0001", "0005049", new Integer(1) }
     * @return 格式化后的字符串 <br>
     *         eg: 在上面的输入下输出为:参数一:0001,参数二:0005049,参数三:1
     */
    public static String format(String text, Object[] args) {
        if (Validators.isEmpty(text) || args == null || args.length == 0) {
            return text;
        }
        for (int i = 0, length = args.length; i < length; i++) {
            text = replace(text, "{" + i + "}", args[i].toString());
        }
        return text;
    }

    /**
     * 格式化浮点型数字成字符串, 保留两位小数位.
     * 
     * @param number
     *            浮点数字
     * @return 格式化之后的字符串
     */
    public static String formatDecimal(double number) {
        NumberFormat format = NumberFormat.getInstance();

        format.setMaximumIntegerDigits(Integer.MAX_VALUE);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        return format.format(number);
    }

    /**
     * 格式化浮点类型数据.
     * 
     * @param number
     *            浮点数据
     * @param minFractionDigits
     *            最小保留小数位
     * @param maxFractionDigits
     *            最大保留小数位
     * @return 将浮点数据格式化后的字符串
     */
    public static String formatDecimal(double number, int minFractionDigits, int maxFractionDigits) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(minFractionDigits);
        format.setMaximumFractionDigits(minFractionDigits);

        return format.format(number);
    }

    /**
     * 取得字符串的真实长度，一个汉字长度为两个字节。
     * 
     * @param str
     *            字符串
     * @return 字符串的字节数
     */
    public static int getRealLength(String str) {
        if (str == null) {
            return 0;
        }

        char separator = 256;
        int realLength = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= separator) {
                realLength += 2;
            }
            else {
                realLength++;
            }
        }
        return realLength;
    }

    /**
     * HTML 文本过滤，如果 value 为 <code>null</code> 或为空串，则返回 "&amp;nbsp;"。
     * 
     * <p>
     * 转换的字符串关系如下：
     * 
     * <ul>
     * <li>&amp; --> &amp;amp;</li>
     * <li>&lt; --> &amp;lt;</li>
     * <li>&gt; --> &amp;gt;</li>
     * <li>&quot; --> &amp;quot;</li>
     * <li>\n --> &lt;br/&gt;</li>
     * <li>\t --> &amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;</li>
     * <li>空格 --> &amp;nbsp;</li>
     * </ul>
     * 
     * <strong>此方法适用于在 HTML 页面上的非文本框元素（div、span、table 等）中显示文本时调用。</strong>
     * 
     * @param value
     *            要过滤的文本
     * @return 过滤后的 HTML 文本
     */
    public static String htmlFilter(String value) {
        if (value == null || value.length() == 0) {
            return "&nbsp;";
        }

        return value.replaceAll("&", "&amp;").replaceAll("\t", "    ").replaceAll(" ", "&nbsp;")
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("\n", "<br/>");
    }

    /**
     * HTML 文本过滤，如果 value 为 <code>null</code> 或为空串，则返回空串。
     * 
     * <p>
     * 转换的字符串关系如下：
     * 
     * <ul>
     * <li>&amp; --> &amp;amp;</li>
     * <li>&lt; --> &amp;lt;</li>
     * <li>&gt; --> &amp;gt;</li>
     * <li>&quot; --> &amp;quot;</li>
     * <li>\n --> &lt;br/&gt;</li>
     * </ul>
     * 
     * <strong>此方法适用于在 HTML 页面上的文本框（text、textarea）中显示文本时调用。</strong>
     * 
     * @param value
     *            要过滤的文本
     * @return 过滤后的 HTML 文本
     */
    public static String htmlFilterToEmpty(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }

        return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;");
    }

    /**
     * 忽略值为 <code>null</code> 的字符串
     * 
     * @param str
     *            字符串
     * @return 如果字符串为 <code>null</code>, 则返回空字符串.
     */
    public static String ignoreNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * 只否包括"\""等不利于文本框显示的字符
     * 
     * @param arg
     * @return
     */
    public static boolean isNotAllowed4TextBox(String arg) {
        if (Validators.isEmpty(arg)) {
            return false;
        }

        return arg.indexOf("\"") >= 0;
    }

    /**
     * 过滤html的"'"字符(转义为"\'")以及其他特殊字符, 主要用于链接地址的特殊字符过滤.
     * 
     * @param str
     *            要过滤的字符串
     * @return 过滤后的字符串
     */
    public static String linkFilter(String str) {
        if (Validators.isEmpty(str)) {
            return str;
        }

        return htmlFilter(htmlFilter(str.replaceAll("'", "\\\\'")));
    }

    /**
     * <p>
     * Replaces all occurrences of a String within another String.
     * </p>
     * 
     * @param text
     * @param repl
     * @param with
     * @return
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    /**
     * <p>
     * Replaces a String with another String inside a larger String, for the first <code>max</code> values of the search
     * String.
     * 
     * @param text
     * @param repl
     * @param with
     * @param max
     * @return
     */
    public static String replace(String text, String repl, String with, int max) {
        if (text == null || Validators.isEmpty(repl) || with == null || max == 0) {
            return text;
        }

        StringBuilder buf = new StringBuilder(text.length());
        int start = 0, end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * 清除字符串左侧的空格
     * 
     * @param str
     * @return
     */
    public static String ltrim(String str) {
        return ltrim(str, " ");
    }

    /**
     * 清除字符串左侧的指定字符串
     * 
     * @param str
     * @param remove
     * @return
     */
    public static String ltrim(String str, String remove) {
        if (str == null || str.length() == 0 || remove == null || remove.length() == 0) {
            return str;
        }

        while (str.startsWith(remove)) {
            str = str.substring(remove.length());
        }
        return str;
    }

    /**
     * 清除字符串右侧的空格
     * 
     * @param str
     * @return
     */
    public static String rtrim(String str) {
        return rtrim(str, " ");
    }

    /**
     * 清除字符串右侧的指定字符串
     * 
     * @param str
     * @param remove
     * @return
     */
    public static String rtrim(String str, String remove) {
        if (str == null || str.length() == 0 || remove == null || remove.length() == 0) {
            return str;
        }

        while (str.endsWith(remove) && (str.length() - remove.length()) >= 0) {
            str = str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * 把字符串按照规则分割，比如str为“id=123&name=test”，rule为“id=#&name=#”，分隔后为["123", "test"];
     * 
     * @param str
     * @param rule
     * @return
     */
    public static String[] split(String str, String rule) {
        if (rule.indexOf(SEPARATOR_SINGLE) == -1 || rule.indexOf(SEPARATOR_SINGLE + SEPARATOR_SINGLE) != -1) {
            throw new IllegalArgumentException("Could not parse rule");
        }

        String[] rules = rule.split(SEPARATOR_SINGLE);
        // System.out.println(rules.length);

        if (str == null || str.length() < rules[0].length()) {
            return new String[0];
        }

        boolean endsWithSeparator = rule.endsWith(SEPARATOR_SINGLE);

        String[] strs = new String[endsWithSeparator ? rules.length : rules.length - 1];
        if (rules[0].length() > 0 && !str.startsWith(rules[0])) {
            return new String[0];
        }

        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < strs.length; i++) {
            startIndex = str.indexOf(rules[i], endIndex) + rules[i].length();
            if (i + 1 == strs.length && endsWithSeparator) {
                endIndex = str.length();
            }
            else {
                endIndex = str.indexOf(rules[i + 1], startIndex);
            }

            // System.out.println(startIndex + "," + endIndex);

            if (startIndex == -1 || endIndex == -1) {
                return new String[0];
            }
            strs[i] = str.substring(startIndex, endIndex);
        }

        return strs;
    }

    /**
     * 替换sql like的字段中的通配符，包括[]%_
     * 
     * @param str
     * @return
     */
    public static String sqlWildcardFilter(String str) {
        if (Validators.isEmpty(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '[') {
                sb.append("[[]");
            }
            else if (c == ']') {
                sb.append("[]]");
            }
            else if (c == '%') {
                sb.append("[%]");
            }
            else if (c == '_') {
                sb.append("[_]");
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 把字符串按照指定的字符集进行编码
     * 
     * @param str
     * @param charSetName
     * @return
     */
    public static String toCharSet(String str, String charSetName) {
        try {
            return new String(str.getBytes(), charSetName);
        }
        catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    /**
     * 把一个字节数组转换为16进制表达的字符串
     * 
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            hexString.append(enoughZero(Integer.toHexString(bytes[i] & 0xff), 2));
        }
        return hexString.toString();
    }

    /**
     * 把16进制表达的字符串转换为整数
     * 
     * @param hexString
     * @return
     */
    public static int hexString2Int(String hexString) {
        return Integer.valueOf(hexString, 16).intValue();
    }

    /**
     * 将以ASCII字符表示的16进制字符串以每两个字符分割转换为16进制表示的byte数组.<br>
     * e.g. "e024c854" --> byte[]{0xe0, 0x24, 0xc8, 0x54}
     * 
     * @param str
     *            原16进制字符串
     * @return 16进制表示的byte数组
     */
    public static byte[] hexString2Bytes(String str) {
        if (Validators.isEmpty(str)) {
            return null;
        }

        if (str.length() % 2 != 0) {
            str = "0" + str;
        }

        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < result.length; i++) {
            // High bit
            byte high = (byte) (Byte.decode("0x" + str.charAt(i * 2)).byteValue() << 4);
            // Low bit
            byte low = Byte.decode("0x" + str.charAt(i * 2 + 1)).byteValue();
            result[i] = (byte) (high ^ low);
        }
        return result;
    }

    /**
     * 清除字符串两边的空格，null不处理
     * 
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str == null ? str : str.trim();
    }

    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String returning an empty String ("") if the
     * String is empty ("") after the trim or if it is <code>null</code>.
     * </p>
     * 
     * @param str
     *            the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     */
    public static String trimToEmpty(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 清除字符串中的回车和换行符
     * 
     * @param str
     * @return
     */
    public static String ignoreEnter(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        return str.replaceAll("\r|\n", "");
    }

    /**
     * 清除下划线，把下划线后面字母转换成大写字母
     * 
     * @param str
     * @return
     */
    public static String underline2Uppercase(String str) {
        if (Validators.isEmpty(str)) {
            return str;
        }

        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '_' && i < charArray.length - 1) {
                charArray[i + 1] = Character.toUpperCase(charArray[i + 1]);
            }
        }

        return new String(charArray).replaceAll("_", "");
    }

    /**
     * 根据分割符对字符串进行分割，每个分割后的字符串将被 <tt>trim</tt> 后放到列表中。
     * 
     * @param str
     *            将要被分割的字符串
     * @param delimiter
     *            分隔符
     * @return 分割后的结果列表
     */
    public static final List<String> split(String str, char delimiter) {
        // return no groups if we have an empty string
        if (str == null || "".equals(str)) {
            return Collections.emptyList();
        }

        ArrayList<String> parts = new ArrayList<String>();
        int currentIndex;
        int previousIndex = 0;

        while ((currentIndex = str.indexOf(delimiter, previousIndex)) > 0) {
            String part = str.substring(previousIndex, currentIndex).trim();
            parts.add(part);
            previousIndex = currentIndex + 1;
        }

        parts.add(str.substring(previousIndex, str.length()).trim());

        return parts;
    }

    /**
     * 判断 value 的值是否表示条件为真。例子：
     * 
     * <ul>
     * <li>"1" -> true</li>
     * <li>"true" -> true</li>
     * <li>"True" -> true</li>
     * <li>"TRUE" -> true</li>
     * <li>"2" -> false</li>
     * <li>"false" -> false</li>
     * <li>"test" -> false</li>
     * </ul>
     * 
     * @param value
     *            字符串
     * @return 如果 value 等于 “1” 或者 “true”（大小写无关） 返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isValueTrue(String value) {
        return BOOLEAN_TRUE_NUMBER.equals(value) || BOOLEAN_TRUE_STRING.equalsIgnoreCase(value);
    }

    /**
     * 判断 value 的值是否表示条件为假。例子：
     * 
     * <ul>
     * <li>"0" -> true</li>
     * <li>"false" -> true</li>
     * <li>"False" -> true</li>
     * <li>"FALSE" -> true</li>
     * <li>"1" -> false</li>
     * <li>"true" -> false</li>
     * <li>"test" -> false</li>
     * </ul>
     * 
     * @param value
     *            字符串
     * @return 如果 value 等于 “0” 或者 “false”（大小写无关） 返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isValueFalse(String value) {
        return BOOLEAN_FALSE_NUMBER.equals(value) || BOOLEAN_FALSE_STRING.equalsIgnoreCase(value);
    }

    /**
     * 判断两个字符串是否equals
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }


    /**
	 * 将半角的符号转换成全角符号.(即英文字符转中文字符)
	 * @autor:chenssy
	 * @date:2014年8月7日
	 *
	 * @param str
	 * 			要转换的字符
	 * @return
	 */
	public static String changeToFull(String str) {
        String source = "1234567890!@#$%^&*()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_=+\\|[];:'\",<.>/?";
        String[] decode = { "１", "２", "３", "４", "５", "６", "７", "８", "９", "０",
                "！", "＠", "＃", "＄", "％", "︿", "＆", "＊", "（", "）", "ａ", "ｂ",
                "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ",
                "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ",
                "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ",
                "Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ",
                "Ｙ", "Ｚ", "－", "＿", "＝", "＋", "＼", "｜", "【", "】", "；", "：",
                "'", "\"", "，", "〈", "。", "〉", "／", "？" };
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int pos = source.indexOf(str.charAt(i));
            if (pos != -1) {
                result += decode[pos];
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
	
	/**
	 *  将字符转换为编码为Unicode，格式 为'\u0020'<br>
	 * 		  unicodeEscaped(' ') = "\u0020"<br>
	 * 		  unicodeEscaped('A') = "\u0041"
	 * @autor:chenssy
	 * @date:2014年8月7日
	 *
	 * @param ch
	 * 			待转换的char 字符
	 * @return
	 */
	public static String unicodeEscaped(char ch) {
		if (ch < 0x10) {
			return "\\u000" + Integer.toHexString(ch);
		} else if (ch < 0x100) {
			return "\\u00" + Integer.toHexString(ch);
		} else if (ch < 0x1000) {
			return "\\u0" + Integer.toHexString(ch);
		}
		return "\\u" + Integer.toHexString(ch);
	}
	
	/**
	 * 进行toString操作，若为空，返回默认值
	 * @autor:chenssy
	 * @date:2014年8月9日
	 *
	 * @param object
	 * 				要进行toString操作的对象
	 * @param nullStr
	 * 				返回的默认值
	 * @return
	 */
	public static String toString(Object object,String nullStr){
		return object == null ? nullStr : object.toString();
	}
	
	/**
	 * 将字符串重复N次，null、""不在循环次数里面 <br>
	 * 		 当value == null || value == "" return value;<br>
	 * 		 当count <= 1 返回  value
	 * @autor:chenssy
	 * @date:2014年8月9日
	 *
	 * @param value
	 * 				需要循环的字符串
	 * @param count
	 * 				循环的次数
	 * @return
	 */
	public static String repeatString(String value,int count){
		if(value == null || "".equals(value) || count <= 1){
			return value;
		}
		
		int length = value.length();
		if(length == 1){          //长度为1，存在字符
			return repeatChar(value.charAt(0), count);
		}
		
		int outputLength = length * count;
		switch (length) {
		case 1:
			return repeatChar(value.charAt(0), count);
		case 2:
			char ch0 = value.charAt(0);
			char ch1 = value.charAt(1);
			char[] output2 = new char[outputLength];
			for (int i = count * 2 - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default: 
			StringBuilder buf = new StringBuilder(outputLength);
			for (int i = 0; i < count; i++) {
				buf.append(value);
			}
			return buf.toString();
		}

	}
	
	/**
	 * 将某个字符重复N次
	 * @autor:chenssy
	 * @date:2014年8月9日
	 *
	 * @param ch
	 * 			需要循环的字符
	 * @param count
	 * 			循环的次数
	 * @return
	 */
	public static String repeatChar(char ch, int count) {
		char[] buf = new char[count];
		for (int i = count - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}

	/**
	 * 判断字符串是否全部都为小写
	 * @autor:chenssy
	 * @date:2014年8月9日
	 *
	 * @param value
	 * 				待判断的字符串
	 * @return
	 */
	public static boolean isAllLowerCase(String value){
		if(value == null || "".equals(value)){
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (Character.isLowerCase(value.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断字符串是否全部大写
	 * @autor:chenssy
	 * @date:2014年8月9日
	 *
	 * @param value
	 * 				待判断的字符串
	 * @return
	 */
	public static boolean isAllUpperCase(String value){
		if(value == null || "".equals(value)){
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (Character.isUpperCase(value.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 反转字符串
	 * @autor:chenssy
	 * @date:2014年8月9日
	 *
	 * @param value
	 * 				待反转的字符串
	 * @return
	 */
	public static String reverse(String value){
		if(value == null){
			return null;
		}
		return new StringBuffer(value).reverse().toString();
	}
	
	/**
	 * @desc:截取字符串，支持中英文混乱，其中中文当做两位处理
	 * @autor:chenssy
	 * @date:2014年8月10日
	 *
	 * @param resourceString
	 * @param length
	 * @return
	 */
	public static String subString(String resourceString,int length){
		String resultString = "";
		if (resourceString == null || "".equals(resourceString) || length < 1) {
			return resourceString;
		}

		if (resourceString.length() < length) {
			return resourceString;
		}

		char[] chr = resourceString.toCharArray();
		int strNum = 0;
		int strGBKNum = 0;
		boolean isHaveDot = false;

		for (int i = 0; i < resourceString.length(); i++) {
			if (chr[i] >= 0xa1){// 0xa1汉字最小位开始
				strNum = strNum + 2;
				strGBKNum++;
			} else {
				strNum++;
			}

			if (strNum == length || strNum == length + 1) {
				if (i + 1 < resourceString.length()) {
					isHaveDot = true;
				}
				break;
			}
		}
		resultString = resourceString.substring(0, strNum - strGBKNum);
		if (isHaveDot) {
			resultString = resultString + "...";
		}

		return resultString;
	}
	
	/**
	 * 
	 * @autor:chenssy
	 * @date:2014年8月10日
	 *
	 * @param htmlString
	 * @param length
	 * @return
	 */
	public static String subHTMLString(String htmlString,int length){
		return subString(delHTMLTag(htmlString), length);
	}
	
	/**
	 * 过滤html标签，包括script、style、html、空格、回车标签
	 * @autor:chenssy
	 * @date:2014年8月10日
	 *
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr){
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式  
	    String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式  
	    String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
	    String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符 
	    
	    Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
        Matcher m_script = p_script.matcher(htmlStr);  
        htmlStr = m_script.replaceAll(""); // 过滤script标签  
  
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
        Matcher m_style = p_style.matcher(htmlStr);  
        htmlStr = m_style.replaceAll(""); // 过滤style标签  
  
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
        Matcher m_html = p_html.matcher(htmlStr);  
        htmlStr = m_html.replaceAll(""); // 过滤html标签  
  
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
        Matcher m_space = p_space.matcher(htmlStr);  
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签  
        
        return htmlStr.trim(); // 返回文本字符串
	}

}
