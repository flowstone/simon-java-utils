package me.xueyao.date;

import me.xueyao.collection.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
	public final static String FORMAT_YEAR = "yyyy年";
	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_DATE_YYMMDD = "yyyyMMdd";
	public final static String FORMAT_TIME = "hh:mm";
	public final static String FORMAT_DATE_TIME = "yyyy-MM-dd hh:mm";
	public final static String FORMAT_DATE_TIME_2 = "yyyy-MM-dd HH:mm";
	public final static String TIME_FORMAT_SHOW_MILLISECOND_WITH_COLON = "yyyy-MM-dd HH:mm:ss";
	public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 hh:mm";
	public final static String MONTH_DAY = "MMdd";
	private static final int YEAR = 365 * 24 * 60 * 60;// 年
	private static final int MONTH = 30 * 24 * 60 * 60;// 月
	private static final int DAY = 24 * 60 * 60;// 天
	private static final int HOUR = 60 * 60;// 小时
	private static final int MINUTE = 60;// 分钟

	/**
	 * 获取当前日期的指定格式的字符串:new Date()--->String
	 *
	 * @param format
	 */
	public static String getCurrentTime(String format) {
		if (StringUtil.isEmpty(format)) {
			simpleDateFormat.applyPattern(FORMAT_DATE_TIME);
		} else {
			simpleDateFormat.applyPattern(format);
		}
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 将long类型的时间格式化：long--->String
	 * 
	 * @param format
	 * @param date
	 */
	public static String formatTimes(String format, long date) {
		if (StringUtil.isEmpty(format)) {
			simpleDateFormat.applyPattern(FORMAT_DATE_TIME);
		} else {
			simpleDateFormat.applyPattern(format);
		}
		return simpleDateFormat.format(new Date(date));
	}

	/**
	 * 将日期字符串以指定格式转换为Date:String--->Date
	 * 
	 * @param time
	 *            日期字符串
	 * @param format
	 *            指定的日期格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
	 * @return
	 */
	public static Date getTimeFromString(String timeStr, String format) {
		try {
			return StringUtil.isEmpty(timeStr) ? null : new SimpleDateFormat(format).parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Date以指定格式转换为日期时间字符串:Date---->String
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
	 * @return
	 */
	public static String getStringFromTime(Date time, String format) {
		if (format == null || format.trim().equals("")) {
			simpleDateFormat.applyPattern(FORMAT_DATE_TIME);
		} else {
			simpleDateFormat.applyPattern(format);
		}
		return simpleDateFormat.format(time);
	}

	// -----------------使用频率较少----------------------------------//

	/**
	 * 根据时间戳获取描述性时间，如3分钟前，1天前
	 * 
	 * @param timestamp
	 *            时间戳 单位为毫秒
	 * @return 时间字符串
	 */
	public static String getDescriptionTimeFromTimestamp(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		//GlobalLog.MY_LOGGER.debug("timeGap: " + timeGap);
		String timeStr = null;
		if (timeGap > YEAR) {
			timeStr = timeGap / YEAR + "年前";
		} else if (timeGap > MONTH) {
			timeStr = timeGap / MONTH + "个月前";
		} else if (timeGap > DAY) {// 1天以上
			timeStr = timeGap / DAY + "天前";
		} else if (timeGap > HOUR) {// 1小时-24小时
			timeStr = timeGap / HOUR + "小时前";
		} else if (timeGap > MINUTE) {// 1分钟-59分钟
			timeStr = timeGap / MINUTE + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

	/**
	 * 根据时间戳获取时间字符串，并根据指定的时间分割数partionSeconds来自动判断返回描述性时间还是指定格式的时间
	 * 
	 * @param timestamp
	 *            时间戳 单位是毫秒
	 * @param partionSeconds
	 *            时间分割线，当现在时间与指定的时间戳的秒数差大于这个分割线时则返回指定格式时间，否则返回描述性时间
	 * @param format
	 * @return
	 */
	public static String getMixTimeFromTimestamp(long timestamp,
			long partionSeconds, String format) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		if (timeGap <= partionSeconds) {
			return getDescriptionTimeFromTimestamp(timestamp);
		} else {
			return getFormatTimeFromTimestamp(timestamp, format);
		}
	}

	/**
	 * 根据时间戳获取指定格式的时间，如2011-11-30 08:40
	 * 
	 * @param timestamp  时间戳 单位为毫秒
	 * @param format     指定格式 如果为null或空串则使用默认格式"yyyy-MM-dd HH:MM"
	 * @return
	 */
	public static String getFormatTimeFromTimestamp(long timestamp,
			String format) {
		if (StringUtil.isEmpty(format)) {
			simpleDateFormat.applyPattern(FORMAT_DATE);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int year = Integer.valueOf(simpleDateFormat.format(
					new Date(timestamp)).substring(0, 4));
			if (currentYear == year) {// 如果为今年则不显示年份
				simpleDateFormat.applyPattern(FORMAT_MONTH_DAY_TIME);
			} else {
				simpleDateFormat.applyPattern(FORMAT_DATE_TIME);
			}
		} else {
			simpleDateFormat.applyPattern(format);
		}
		return simpleDateFormat.format(new Date(timestamp));
	}
	
	
	/**
	 * 计算剩余时长
	 * 
	 * 超过1小时，分钟不参入计算
	 * 不足小时，按分钟计算
	 * 不足1分钟，按1分钟算
	 * 
	 */
	public static String timeLeft(Date startDate,long validTime) {
		String timeLeftStr = null;
		
		long currentTime = System.currentTimeMillis();
		long startTime = startDate.getTime();
		
		if(startTime + validTime > currentTime) { //未超时
			
			long timeLeftValue = validTime + startDate.getTime() - currentTime;
			long second = timeLeftValue / 1000;
			if(second < 60) { //1分钟内，显示1分钟
				timeLeftStr = "1分钟";
			} else {
				long minutes = second / 60;
				if(minutes > 60) { //超过1个小时
					long hour = minutes / 60;
					timeLeftStr = hour + "小时";
				} else {
					timeLeftStr = minutes + "分钟";
				}
			}
			
		} else {
			//订单超时了
			timeLeftStr = "小于1分钟";
		}
		return timeLeftStr;
	}
	
	//由出生日期获得年龄
    public static int getAge(Date birthDay)  {
        Calendar cal = Calendar.getInstance();
 
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
 
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
 
        int age = yearNow - yearBirth;
 
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth){
                	age--;
				}
            } else{
                age--;
            }
        }
        return age;
    }

}
