package me.xueyao.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 日期工具
 * @author tiant5
 *
 */
public class DateUtils {
	
	
	public static final String C_DATE_DIVISION = "-";
	public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String C_TIME_PATTON_DEFAULT_WITHOUT_SS = "yyyy-MM-dd HH:mm";
	public static final String C_TIME_PATTON_DEFAULTS = "yyyy{MM-dd HH:mm}ss";
	public static final String C_TIME_PATTON_DEFAULTY = "MM-dd HH:mm";
	public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";
	public static final String C_DATE_PATTON_DEFAULT_S = "yyyy/MM/dd";
	public static final String C_DATE_PATTON_yyyyMM = "yyyy-MM";
	public static final String C_DATA_PATTON_YYYYMMDD = "yyyyMMdd";
	public static final String C_TIME_PATTON_HHMMSS = "HHmmss";
	public static final String C_TIME_PATTON_hhmmss = "HH:mm:ss";
	public static final String C_TIME_PATTON_hhmm = "HH:mm";
	public static final String C_TIME_PATTON_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String C_TIME_PATTON_yyyy = "yyyy";
	public static final String C_DATA_PATTON_YYMMDD = "yyMMdd";

	public static final int C_ONE_SECOND = 1000;
	public static final int C_ONE_MINUTE = 60 * C_ONE_SECOND;
	public static final int C_ONE_HOUR = 60 * C_ONE_MINUTE;
	public static final long C_ONE_DAY = 24 * C_ONE_HOUR;

	/**
	 * 计算天数差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffDate(Date   date1,   Date   date2)
	{
		return (int)((date1.getTime()-date2.getTime())/(24*60*60*1000));
	}
	/**
	 * 获取当前日期
	 * 
	 * @return DATE
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();
		return currDate;
	}

	/**
	 * 获取当前日期字符串
	 * 
	 * @return String
	 */
	public static String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();
		return format(currDate);
	}
	
	/**
	 * 获取当前日期字符串
	 * 
	 * @return String
	 */
	public static String getCurrentTimeStr() {
		Calendar cal = Calendar.getInstance();
		return formatTime(cal.getTime());
	}

	/**
	 * 按指定格式获取当前时间字符串
	 * 
	 * @param strFormat
	 * @return String
	 */
	public static String getCurrentDateStr(String strFormat) {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate, strFormat);
	}
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat(C_TIME_PATTON_yyyyMMddHHmmss);
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 将字符类型转为日期类型(yyyy-MM-dd)
	 * 
	 * @param dateValue
	 * @return Date
	 */
	public static Date parseDate(String dateValue) {
		return parseDate(C_DATE_PATTON_DEFAULT, dateValue);
	}
	
	/**
	 * 将字符串转为时间类型"HH:mm:ss"
	 */
	public static Date parseHour(String dateValue){
		return parseDate(C_TIME_PATTON_hhmmss, dateValue);
	}

	/**
	 * 
	 * @describe 获得当前时间
	 */
	public static String getCurrentDateTime(){
		Calendar cal = Calendar.getInstance();
		return format(cal.getTime(), C_TIME_PATTON_hhmmss);
	}
	
	/**
	 * 将字符类型转为日期类型(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param dateValue
	 * @return Date
	 */
	public static Date parseDateTime(String dateValue) {
		return parseDate(C_TIME_PATTON_DEFAULT, dateValue);
	}

	/**
	 * 按指定类型将字符类型转为日期类型
	 * 
	 * @param strFormat
	 * @param dateValue
	 * @return Date
	 */
	public static Date parseDate(String strFormat, String dateValue) {
		if (dateValue == null)
			return null;

		if (strFormat == null)
			strFormat = C_TIME_PATTON_DEFAULT;

		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;

		try {
			newDate = dateFormat.parse(dateValue);
		} catch (ParseException pe) {
			newDate = null;
		}

		return newDate;
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String format(Date aTs_Datetime) {
		return format(aTs_Datetime, C_DATE_PATTON_DEFAULT);
	}
	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串
	 * 
	 * @param aTs_Datetime    yyyy/MM/dd
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String formatString(Date aTs_Datetime) {
		return format(aTs_Datetime, C_DATE_PATTON_DEFAULT_S);
	}
	
	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串
	 * 
	 * @param
	 *            。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String formatToString(Date Datetime) {
		String str = "";
		long current=System.currentTimeMillis();//当前时间毫秒数
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数  
        Date today= new Timestamp(zero);
        Date yesterday = addDays(today,-1);  
        if(Datetime.after(today) || Datetime.equals(today)){
        	str = format(Datetime, C_TIME_PATTON_hhmm);
        }else if(Datetime.after(yesterday)|| Datetime.equals(yesterday)){
        	str = "昨天  "+format(Datetime, C_TIME_PATTON_hhmm);
        }else{
        	str = format(Datetime, C_TIME_PATTON_DEFAULTY);
        }
		return str;
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String formatTime(Date aTs_Datetime) {
		return format(aTs_Datetime, C_TIME_PATTON_DEFAULTS);
	}
	
	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String formatTimes(Date aTs_Datetime) {
		return format(aTs_Datetime, C_TIME_PATTON_DEFAULT);
	}

	/**
	 * 将Date类型的日期转换为指定格式的字符串
	 * 
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Date aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * 将Date类型的时间转换为字符串
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static String getFormatTime(Date dateTime) {
		return format(dateTime, C_TIME_PATTON_HHMMSS);
	}
	
	
	/**
	 * 将Date类型的时间转换为字符串,保留时分
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static String getFormatTimeHM(Date dateTime) {
		return format(dateTime, "HH:mm");
	}
	
	/**
	 * 将Date类型的时间转换为字符串
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static String getFormatTimeAsYMD(Date dateTime) {
		return format(dateTime, C_DATA_PATTON_YYYYMMDD);
	}

	/**
	 * 将Timestamp类型的时间转换为指定格式字符串
	 * 
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Timestamp aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}
	
	public static Date addYears(Date date, int years){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}
	
	/**
	 * 取得指定日期N天后的日期
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_MONTH, days);

		return cal.getTime();
	}

	/**
	 * 取得指定日期N月后的日期
	 * 
	 * @param date
	 * @param
	 * @return
	 */
	public static Date addMonth(Date date, int month){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH,month);
		return cal.getTime();
	}
	
	
	/**
	 * 取得指定日期N小时的天数
	 * 
	 * @param date
	 * @param
	 * @return
	 */
	public static Date addHour(Date date, int time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, time);
		return cal.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数(date2 - date1)
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	
	/**
	 * 计算两个日期之间相差的毫秒数(date2 - date1)
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long millSecondBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = time2 - time1;
		return between_days;
	}
	
	/**
	 * 判断时间是否在给定时间之间
	 * @param d 中间时间
	 * @param
	 * @param
	 * @return
	 */
	public static int dayTimesBetween(Date d,Date begin_date, Date end_date) {
		int i = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		long t = cal.getTimeInMillis();
		cal.setTime(begin_date);
		long begin_date_l = cal.getTimeInMillis();
		cal.setTime(end_date);
		long end_date_l = cal.getTimeInMillis();
		if (t-begin_date_l>=0&&t-end_date_l<=0) {
			i = 1;
		}
		return i;
	}
	
	/**
	 * 获取时间的月份
	 * @param nowDate
	 * @return
	 */
	 public static int getMonth(Date  nowDate)
	  {
		  int re=0;
		  Calendar nowCalendar=Calendar.getInstance();
			 nowCalendar.setTime(nowDate);
			
			 re=nowCalendar.get(2)+1; 
		  return re;
	  }
	  
	 /**
	  * 获取时间的年份
	  * @param nowDate
	  * @return
	  */
	  public static int getYear(Date  nowDate)
	  {
		  int re=0;
		  Calendar nowCalendar=Calendar.getInstance();
			 nowCalendar.setTime(nowDate);
			 re=nowCalendar.get(1); 
		  return re;
	  }
	  
	  /**
	   * 获取时间的在什么时间段
	   * @param nowDate
	   * @return
	   */
	  public static String getHourStr(Date  nowDate)
	  {
		  int re=0;
		  int re2=0;
		  String date= "";
		  Calendar nowCalendar=Calendar.getInstance();
			 nowCalendar.setTime(nowDate);
			 re=nowCalendar.get(11); 
			 re2 = re+1;
			 if(re<10){
				 date = "0"+re;
			 }else{
				 date = re+"";
			 }
			 if(re2<10){
				 date = date + "-0"+re2;
			 }else{
				 date = date +"-"+re2;
			 }
			 
		  return date;
	  }
	  
	  /**
	   * 获取时间的小时
	   * @param nowDate
	   * @return
	   */
	  public static int getHour(Date  nowDate)
	  {
		  int re=0;
		  Calendar nowCalendar=Calendar.getInstance();
			 nowCalendar.setTime(nowDate);
			 re=nowCalendar.get(11); 
		  return re;
	  }
	  
	  /**
	   * 获取时间的星期
	   * @param nowDate
	   * @return
	   */
	  public static int getDay0fWeek(Date  nowDate)
	  {
		  int re=0;
		  Calendar nowCalendar=Calendar.getInstance();
			 nowCalendar.setTime(nowDate);
			 re=nowCalendar.get(7); 
		  return re;
	  }
	  
	  /**
	   *  获取时间，是一个月的第几天
	   * @param nowDate
	   * @return
	   */
	  public static int getDay0fMonth(Date  nowDate)
	  {
		  int re=0;
		  Calendar nowCalendar=Calendar.getInstance();
			 nowCalendar.setTime(nowDate);
			 re=nowCalendar.get(5); 
		  return re;
	  }
	  
	  /**
	   *  将string 装为date
	   * @param textDate
	   * @return
	   */
	  public static Date getDate(String textDate) {
		    SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Date date = null;
		    try {
		      date = sdfInput.parse(textDate);
		    } catch (ParseException ex) {
		    }
		    return date;
		  }
		/*
		 * 获取默认的开始时间，取服务器当前时间
		 */
	   	public static Date toDayStartTimeDefault(){
	   		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");  
	   		
	   		Date currentTime2 =new Date();  
	   		String endTimeString = f.format(currentTime2)+" 00:00:00";
	   		  Date endTime=getDate(endTimeString);
	   		return endTime;
	   	}
	   	
	   	
	   	/*
		 * 获取默认的结束时间，取服务器当前时间
		 */
	   	public static Date toDayEndTimeDefault(){
	   		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");  
	   		
	   		Date currentTime2 =new Date();  
	   		String endTimeString = f.format(currentTime2)+" 23:59:59";
	   		  Date endTime=getDate(endTimeString);
	   		return endTime;
	   	}
	   	
	   	/**
	   	 * 获取当天2点钟的时间
	   	 */
	   	public static Date toDayTime2Default(){
	   		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");  
	   		
	   		Date currentTime2 =new Date();  
	   		String endTimeString = f.format(currentTime2)+" 02:00:00";
	   		  Date endTime=getDate(endTimeString);
	   		return endTime;
	   	}
	   	
	   	/**
	   	 * 获取格式为:MM-dd的时间字符串
	   	 */
	   	public static String getShortDate(Date dateTime){
	   		if(dateTime == null) return "";
	   		 String time = formatTime(dateTime);
	   		 return time.substring(5, 10);
	   	}
	   	
	   	/**
	   	 * 获取格式为:MM-dd HH:mm的时间字符串
	   	 */
	   	public static String getShortTime(Date dateTime){
	   		if(dateTime == null) return "";
	   		 String time = formatTime(dateTime);
	   		 return time.substring(5, 16);
	   	}
	   	/**
	   	 * 计算两个日期之间相差的天数，小时，分钟(date2 - date1)
	   	 */
	   	public static String timesBetween(Date date1, Date date2) {
	   		 String re = ""; 
	   		long timeBetweenLong = date1.getTime()- date2.getTime() ;
	   		   long dayTimeLong = timeBetweenLong/(24*60*60*1000);
	   		     if(dayTimeLong > 0)
	   		     {
	   		    	 re = re+ dayTimeLong +"天";
	   		     }
	   		  long hoursTimeLong = timeBetweenLong%(24*60*60*1000)/(60*60*1000);    
	   		 if(hoursTimeLong > 0)
   		     {
   		    	 re = re+  hoursTimeLong +"小时";
   		     }
	   		 
	   	  long mTimeLong = timeBetweenLong%(24*60*60*1000)%(60*60*1000)/(60*1000); 		   		 
		    	 re = re+  mTimeLong +"分钟"; 		   		 
	   		 return re ;
	   	}

	/**
	 * 将日期文字转化为具体的日期
	 * 
	 * @return
	 */
	public static Date string2Date(String dateString, String formatString) {
		SimpleDateFormat formate = new SimpleDateFormat(formatString);
		Date date;
		try {
			date = formate.parse(dateString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 返回为相对时间串
	 * @param date
	 * @return
	 */
	public static String trans4TimeStr(Date date)
	{
		if (date == null)
		{
			return "出错";
		}
		Date curDate = new Date();
		long mTime = date.getTime();
		long cTime = curDate.getTime();
		long result = cTime - mTime;
		double dayTime = (double)result / (double)(1000 * 60 * 60 * 24); 
		if ( dayTime < 0)
		{
			return "时间出错";
		}
		if ( dayTime < 1)
		{
			int hour = (int)(dayTime * 24);
			if (hour == 0)
			{
				int minitus = (int)(dayTime * 24 * 60);
				return minitus + "分钟前";
			}
			return hour + "小时前";
		}
		return (int)dayTime + "天前";
	}
	
	
	/**
	 * 
	 * 将日期转化为字符串
	 * @return
	 */
	public static String Date2String(Date date, String formatString) {
		SimpleDateFormat formate = new SimpleDateFormat(formatString);
		return formate.format(date);
	}
	
	
	/**
	 * 返回为1表示星期一
	 * 返回为7表示星期天
	 * 其它一次类推
	 * @param date
	 * @return
	 */
	public static int indexOfCurrentWeek(Date date)
	{
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(date);           
        int w=cal.get(Calendar.DAY_OF_WEEK)-1; 
        if(w==0)
        {
        	w = 7;
        }
        return w;
	}
	
	/**
	 * 返回为1表示星期一
	 * 返回为7表示星期天
	 * 其它一次类推,返回月日，周几 -上下午
	 * @param date
	 * @return
	 */
	public static String indexOfDateWeek(Date date)
	{
		String DateStr = "";
		String str = "";
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(date);           
        int w=cal.get(Calendar.DAY_OF_WEEK)-1; 
        if(w==0)
        {
        	str = "周日";
        }
        if(w==1)
        {
        	str = "周一";
        }
        if(w==2)
        {
        	str = "周二";
        }
        if(w==3)
        {
        	str = "周三";
        }
        if(w==4)
        {
        	str = "周四";
        }
        if(w==5)
        {
        	str = "周五";
        }
        if(w==6)
        {
        	str = "周六";
        }
        if(getHour(date)<=12){
        DateStr = getMonth(date)+"月"+date.getDate()+"日 "+" "+str+"-上午";
        }else{
        	DateStr = getMonth(date)+"月"+date.getDate()+"日 "+" "+str+"-下午";
        }
        return DateStr;
	}
	
	/**
	 * 返回为1表示星期一
	 * 返回为7表示星期天
	 * 其它一次类推,返回年月日，-上下午+时分
	 * @param date
	 * @return
	 */
	public static String DateWeek(Date date)
	{
		String DateStr = "";
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(date);            
        
        if(getHour(date)<=12){
        DateStr = format(date)+" "+"-上午"+date.getHours()+":"+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes())+"前";
        }else{
        	DateStr = format(date)+" "+"-下午"+date.getHours()+":"+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes())+"前";
        }
        return DateStr;
	}
	
	public static String getTimeString(Date date, String style) {
		SimpleDateFormat myFmt2 = new SimpleDateFormat(style);
		return myFmt2.format(date);
	}
	
	
	/**
	 * 是否到期
	 * @param date
	 * @return
	 */
	public static boolean isTimeUp(Date date)
	{
		Calendar cal = Calendar.getInstance(); 
        return cal.after(date);
	}
	
	/*
	 * 获取今天N小时N分N秒的时间
	 */
	public static Date get_today_hour_minite(int hour,int minute,int second)
	{
		 Calendar c = Calendar.getInstance();
	     c.set(Calendar.HOUR_OF_DAY, hour);
	     c.set(Calendar.MINUTE, minute);
	     c.set(Calendar.SECOND, second);
//	    Date m6 = c.getTime();
		return c.getTime();
	}
/**
 * 比较两个日期的大小	
 * @param
 * @param
 * @return
 */
	public static int compare_time(String date1,String date2){
		java.text.DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	Calendar c1= Calendar.getInstance();
	Calendar c2= Calendar.getInstance();
	try    
	{     
	c1.setTime(df.parse(date1));     
	c2.setTime(df.parse(date2));     
	}catch(ParseException e){
	System.err.println("格式不正确");     
	}     
	int result=c1.compareTo(c2);
	return result;
	}
	
	/**
	 * 将当前时间增加分钟数
	 */
	public static Date addMinutes(Date date,int minutes){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	
	/**
	 * 
	 * @Description:返回时间间隔内的日期
	 * @param begin_date
	 * @param end_date
	 * @return
	 */
	public static List<String> timeSepreate(String begin_date,String end_date){
		List<String> str = new ArrayList<String>();
		Date begin_d = DateUtils.parseDate(begin_date);
		Date end_d = DateUtils.parseDate(end_date);
		String begin_d_d = "";
		do{
			begin_d_d = DateUtils.format(begin_d);
			str.add(begin_d_d);
			begin_d = DateUtils.addDays(begin_d, 1);
		}while(begin_d.compareTo(end_d)<=0);
		return str;
	}
	
	/**
	 * 将当前时间增加秒钟数
	 */
	public static Date addSeconds(Date date,int seconds){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
