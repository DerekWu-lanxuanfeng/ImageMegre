package com.flame.tools.megreimage.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: flame-game-common 
 * @File: com.flame.game.core.common.date.DateUtil.java
 * @Description: 日期时间工具类
 * @Create: DerekWu  2015年12月31日 下午5:36:10 
 * @version: V1.0 
 */
public class DateUtil {

	/** 默认日期格式 - 最小到天 */
	public static final SimpleDateFormat DATE_DEF_FORMAT;

	/** 默认时间格式 - 最小到秒 */
	public static final SimpleDateFormat TIME_DEF_FORMAT;

	public static final int DATE_BY_MONTH = 1; // 按月分表
	public static final int DATE_BY_WEEK = 2; // 按周分表
	public static final int DATE_BY_DAY = 3; // 按天分表

	public static final long SECOND = 1000;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;
	public static final long WEEK = DAY * 7;

	private static final String[] WEEKS = { "星期日", "星期一", "星期二", "星期三", "星期四",
			"星期五", "星期六" };
	private static final int[] WEEKS_INDEX = { 7, 1, 2, 3, 4, 5, 6 };

	private static final int[] MONTHS = { 31,// 1月
			28,// 2月
			31,// 3月
			30,// 4月
			31,// 5月
			30,// 6月
			31,// 7月
			31,// 8月
			30,// 9月
			31,// 10月
			30,// 11月
			31,// 12月
	};

	/** 默认时间 1970-01-01 00:00:00 */
	private static final Date DEF_DATE;
	public static long SRC_TIME;

	static {
		DATE_DEF_FORMAT = new SimpleDateFormat("yyyyMMdd");
		TIME_DEF_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.set(1970, 0, 1, 0, 0, 0);
		DEF_DATE = c.getTime();
		try {
			SRC_TIME = new SimpleDateFormat("yyyyMMdd").parse("19700101")
					.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	//private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static Date getDefaultDate() {
		return DEF_DATE;
	}

	/**
	 * 根据指定的long数值 转换年/月/日/时/分/秒 后得到新实例
	 * 
	 * @param time
	 *            将要被转换的时间
	 * @return int[] {年,月,日,时,分,秒}
	 */
	public static int[] currentTime(long time) {
		int[] resultDate = new int[6];
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 格式化时间
		String dateString = simpleDateFormat.format(time);
		String[] result = dateString.split(" ");
		// 天以前的
		String beforeDay = result[0];
		String[] dday = beforeDay.split("-");
		resultDate[0] = Integer.parseInt(dday[0]);
		resultDate[1] = Integer.parseInt(dday[1]);
		resultDate[2] = Integer.parseInt(dday[2]);
		// 天以后的
		String nextDay = result[1];
		String[] nday = nextDay.split(":");
		resultDate[3] = Integer.parseInt(nday[0]);
		resultDate[4] = Integer.parseInt(nday[1]);
		resultDate[5] = Integer.parseInt(nday[2]);
		return resultDate;
	}

	/**
	 * 根据当前时间long值 转换年/月/日/时/分/秒 后得到新实例
	 * 
	 * @return int[] {年,月,日,时,分,秒}
	 */
	public static int[] currentTime() {
		return currentTime(System.currentTimeMillis());
	}

	/**
	 * 计算两个时间相隔的天数
	 * 
	 * @return
	 */
	public static int countDaysBetweenTime(long startTime, long afterTime) {
		long startDay = (startTime - SRC_TIME) / DAY;
		long afterDay = (afterTime - SRC_TIME) / DAY;
		return (int) (afterDay - startDay);
	}

	/**
	 * 获取两个时间相隔几周,如果endTime < startTime 返回-1;不足一周返回0;
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getIntervalWeek(long startTime, long endTime) {
		int intervalDay = countDaysBetweenTime(startTime, endTime);
		return intervalDay / 7;
	}

	/**
	 * 获取当天hour点的时间long
	 * 
	 * @param hour
	 * @return
	 */
	public static long getTodayTimeAt(int hour) {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取当天hour点的日期
	 * 
	 * @param hour
	 * @return
	 */
	public static Date getTodayHourTime(int hour) {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当天hour点minute分的时间long
	 * 
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getTodayTimeAt(int hour, int minute) {
		return getTodayCalendarAt(hour, minute).getTimeInMillis();
	}

	/**
	 * 获取当天hour点minute分的时间Date
	 * 
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date getTodayDateAt(int hour, int minute) {
		return getTodayCalendarAt(hour, minute).getTime();
	}

	/**
	 * 获取当天hour点minute分的时间Calendar
	 * 
	 * @param hour
	 * @param minute
	 * @return
	 */
	private static Calendar getTodayCalendarAt(int hour, int minute) {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * 获取指定日期seconds秒后的时间
	 * 
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date getDateTimeAt(Date date, int seconds) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	/**
	 * 获取指定日期m毫秒后的时间
	 * 
	 * @param date
	 * @param m
	 * @return
	 */
	public static Date getDateTimeAddMillisecond(Date date, int m) {
		Calendar cal = new GregorianCalendar();
		if (date == null) {
			cal.setTime(getCurrentUtilDate());
		} else {
			cal.setTime(date);
		}
		cal.add(Calendar.MILLISECOND, m);
		return cal.getTime();
	}

	/**
	 * 获取指定日期指定时分秒的日期
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @return
	 */
	public static Date getDateTimeAt(Date date, int hour, int minute,
			int seconds) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	/**
	 * 获取指定日期往后nextDay天hour时minute分seconds秒的日期
	 * 
	 * @param date
	 * @param nextDays
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @return
	 */
	public static Date getNextDaysDateTimeAt(Date date, int nextDays, int hour,
			int minute, int seconds) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, nextDays);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	/**
	 * 获取指定日期指定时分秒,星期几的日期
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @param week
	 * @return
	 */
	public static Date getDateTimeAt(Date date, int hour, int minute,
			int seconds, int week) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, seconds);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, week + 1);
		return cal.getTime();
	}

	/**
	 * 获取日期所在月的指定号，时，分，秒日期
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @param day
	 * @return
	 */
	public static Date getDateTimeDayOfMonth(Date date, int hour, int minute,
			int seconds, int day) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, seconds);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	/**
	 * 获取日期所在月的下一月份的指定号，时，分，秒日期
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @param day
	 * @return
	 */
	public static Date getDateTimeDayOfNextMonth(Date date, int hour,
			int minute, int seconds, int day) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, seconds);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	/**
	 * 获取指定日期是所在月几号
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayName(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取所在小时点,根据time得到几点钟
	 * 
	 * @param time
	 * @return
	 */
	public static int getHourAt(long time) {
		return (int) (((time - SRC_TIME) % DAY) / HOUR);
	}

	/**
	 * 获取中文标示的星期
	 * 
	 * @param time
	 *            需要转换的时间
	 * @return 返回星期日/一/二/三/四/五/六
	 */
	public static String getWeekName(long time) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		int weekIndex = c.get(Calendar.DAY_OF_WEEK) - 1;
		return WEEKS[weekIndex < 0 ? 0 : weekIndex];
	}

	/**
	 * 获取星期几
	 * 
	 * @param time
	 *            需要转换的时间
	 * @return 返回星期几标示的数字 7/1/2/3/4/5/6
	 */
	public static int getWeekIndex(long time) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		int weekIndex = c.get(Calendar.DAY_OF_WEEK) - 1;
		return WEEKS_INDEX[weekIndex < 0 ? 0 : weekIndex];
	}

	/**
	 * 获取日期星期几
	 * 
	 * @return 返回星期几标示的数字 7/1/2/3/4/5/6
	 */
	public static int getCurrentTimeWeekIndex() {
		return getWeekIndex(getCurrentUtilDate().getTime());
	}

	/**
	 * 时间格式转换,精确到秒
	 * 
	 * @param time
	 *            实际时间- long型
	 * @return
	 */
	public static String currentTimeFormatToSeconds(long time) {
		String format = TIME_DEF_FORMAT.format(time);
		return format;
	}
	
	/**
	 * 时间格式转换,精确到秒
	 * 
	 * @param time
	 * @return
	 */
	public static String timesFormatToSeconds(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
		String format = formatter.format(time);
		return format;
	}

	/**
	 * 时间格式转换,精确到分
	 * 
	 * @param time
	 * @return
	 */
	public static String currentTimeFormatToMinutes(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 格式化时间
		String format = formatter.format(time);
		return format;
	}

	/**
	 * 指定日期格式转换
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormatToMinutes(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 时间格式转换,精确到小时
	 * 
	 * @param time
	 * @return
	 */
	public static String currentTimeFormatToHour(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");// 格式化时间
		String format = formatter.format(time);
		return format;
	}

	/**
	 * 时间格式转换,精确到天
	 * 
	 * @param time
	 *            实际时间- long型
	 * @return
	 */
	public static String currentTimeFormatToDays(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");// 格式化时间
		String format = formatter.format(time);
		return format;
	}

	/**
	 * 获取当前日期在指定时分秒的时间
	 * 
	 * @param time
	 *            时：分：秒；如10:03:30
	 * @return
	 */
	public static Date currentDateAt(String time) {
		StringBuffer sb = new StringBuffer();
		sb.append(currentTimeFormatToDays(getCurrentUtilDate().getTime()))
				.append(" ").append(time);
		return getDateByStrDefaultFormat(sb.toString());
	}
	
	/**
	 * 获取当前日期在指定时分秒的时间
	 * 
	 * @param time
	 *            时：分：秒；如10:03:30
	 * @return
	 */
	public static Date currentDateAt(Date currDate, String time) {
		StringBuffer sb = new StringBuffer();
		sb.append(currentTimeFormatToDays(currDate.getTime()))
				.append(" ").append(time);
		return getDateByStrDefaultFormat(sb.toString());
	}

	/**
	 * 获取当前日期下一天在指定时分秒的时间
	 * 
	 * @param time
	 *            时：分：秒；如10:03:30
	 * @return
	 */
	public static Date nextDateAt(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		StringBuffer sb = new StringBuffer();
		sb.append(currentTimeFormatToDays(date.getTime())).append(" ")
				.append(time);
		return getDateByStrDefaultFormat(sb.toString());
	}

	/**
	 * 时间格式转换,精确到天 yyyy/MM/dd
	 * 
	 * @param time
	 *            实际时间- long型
	 * @return
	 */
	public static String theTimeFormatToDays(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");// 格式化时间
		String format = formatter.format(time);
		return format;
	}

	/**
	 * 时间格式转换,精确到月
	 * 
	 * @param time
	 *            实际时间- long型
	 * @return
	 */
	public static String currentTimeFormatToMonth(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");// 格式化时间
		String format = formatter.format(time);
		return format;
	}

	/**
	 * 时间格式转换,精确到年
	 * 
	 * @param time
	 *            实际时间- long型
	 * @return
	 */
	public static String currentTimeFormatToYear(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");// 格式化时间
		String format = formatter.format(time);
		return format;
	}

	/**
	 * 根据格式和指定年月日返回时间,格式和指定日期的格式一定要一致</br> 这里的格式是yyyy-MM-dd
	 * HH:mm:ss,传进来的日期格式一定要是这个样式</br>
	 * 
	 * @param date
	 *            如 >> 2013-10-28 21:21:10</br>
	 * @return</br>
	 */
	public static long getTime(String date) {
		return getTime("yyyy-MM-dd hh:mm:ss", date);
	}

	/**
	 * 根据指定的日期返回long型时间</br> 格式一定要一致:如</br> format = yyyy-MM-dd HH:mm:ss</br>
	 * 那么日期必须是 这个样式</br> 如:2013-10-28 21:21:10</br>
	 * 
	 * @param format
	 *            格式</br>
	 * @param date
	 *            日期</br>
	 * @return</br>
	 */
	public static long getTime(String format, String date) {
		try {
			return new SimpleDateFormat(format).parse(date).getTime();
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * 获取某一年的某一月有多少天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysByMonth(int year, int month) {
		if (month == 2) {
			int tmp = year % 4;
			if (tmp != 0) {
				// 平年 28天
				return MONTHS[1];
			}
			// 闰年 29天
			return MONTHS[1] + 1;
		}
		return MONTHS[month - 1];
	}

	/**
	 * 获得当前时间，返回java.util.Date
	 * 
	 * @return
	 */
	public static java.util.Date getCurrentUtilDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 获得当前时间，返回年份
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获得当前时间的小时分钟和秒
	 * 
	 * @return 返回一个数字8点半=83000，19点=190000
	 */
	public static int getCurrentHourMinuSec() {
		Calendar c = Calendar.getInstance();
		return (c.get(Calendar.HOUR_OF_DAY) * 10000)
				+ (c.get(Calendar.MINUTE) * 100) + c.get(Calendar.SECOND);
	}

	/**
	 * 获得当月的第一天的00点00分00秒，返回java.util.Date
	 * 
	 * @return
	 */
	public static java.util.Date getCurrentMonthFirstUtilDate() {
		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 01, 00, 00, 00);
		return c.getTime();
	}

	/**
	 * 获得当天的00点00分00秒，返回java.util.Date
	 * 
	 * @return
	 */
	public static java.util.Date getCurrentDayFirstUtilDate() {
		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
		return c.getTime();
	}

	/**
	 * 获得当天的23点59分59秒，返回java.util.Date
	 * 
	 * @return
	 */
	public static java.util.Date getCurrentDayLastUtilDate() {
		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return c.getTime();
	}

	/**
	 * 获得当前时间的小时
	 * 
	 * @return
	 */
	public static int getCurrentHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获得时间的分钟
	 * @return
	 */
	public static int getMinu(Date date) {
		java.util.Calendar caled = java.util.Calendar.getInstance();
		caled.setTime(date);
		return caled.get(Calendar.MINUTE);
	}

	/**
	 * 得到timer毫秒以后的时间
	 * 
	 * @param timer
	 * @return
	 */
	public static java.util.Date getAfterUtilDate(long timer) {
		return new java.util.Date(getCurrentUtilDate().getTime() + timer);
	}

	/**
	 * 获得当前时间，返回java.sql.Date
	 * 
	 * @return
	 */
	public static java.sql.Date getCurrentSqlDate() {
		return new java.sql.Date(System.currentTimeMillis());
	}

	/**
	 * 获得当前时间，返回字符串(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return
	 */
	public static String getCurrentDateAsString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(getCurrentUtilDate());
	}

	/**
	 * 获得当前时间，返回用户自定义格式字符串
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String getCurrentDateAsStringCustom(String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(getCurrentUtilDate());
	}

	/**
	 * 获得某个格式为 yyyy-MM-dd 的日期的 00:00:00 的时间
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateFirstTime(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(dateStr + " 00:00:00");
		} catch (ParseException e) {
			// logger.error(ResourceBundleService.getString("txt.exception"),
			// e);
		}
		return getCurrentMonthFirstUtilDate();
	}

	/**
	 * 获得某个格式为 yyyy-MM-dd 的日期的 23:59:59 的时间
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateLastTime(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(dateStr + " 23:59:59");
		} catch (ParseException e) {
			// logger.error(ResourceBundleService.getString("txt.exception"),
			// e);
		}
		return getCurrentDayLastUtilDate();
	}

	/**
	 * 获取yyyy-MM-dd HH:mm:ss 的日期对象
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateByStrDefaultFormat(String dateStr) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = formatter.parse(dateStr);
		} catch (Exception e) {
			//logger.info("getDateByStrDefaultFormat", e);
		}
		return date;

	}

	/**
	 * 获取指定时的的 00:00 00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateFirstTime(Date date) {
		java.util.Calendar caled = java.util.Calendar.getInstance();
		caled.setTime(date);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		return caled.getTime();
	}

	/**
	 * 获取指定时间的23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateLastTime(Date date) {
		java.util.Calendar caled = java.util.Calendar.getInstance();
		caled.setTime(date);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 23);
		caled.set(java.util.Calendar.MINUTE, 59);
		caled.set(java.util.Calendar.SECOND, 59);
		return caled.getTime();
	}

	/**
	 * 获得某个字符串的时间
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateByStrAndFormat(String dateStr, String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			// logger.error(ResourceBundleService.getString("txt.exception"),
			// e);
		}
		return getCurrentUtilDate();
	}

	/**
	 * 将日期转换为字符串默认格式:yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param date
	 * @return
	 */
	public static String parseDateToString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * 将java.util.Date转化为HH:00格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String parseDateToHourMinuteString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:00");
		return formatter.format(date);
	}

	/**
	 * 将日期转换成报告使用的日期字符串
	 * 
	 * @param date
	 * @return
	 */
	/*
	 * public static String parseDateToReportTimeString(Date date){
	 * SimpleDateFormat formatter = new
	 * SimpleDateFormat(ResourceBundleService.getString
	 * ("txt.common.dateTimeFormat")); return formatter.format(date); }
	 */

	/**
	 * 将字符串转换为日期java.sql.Date)
	 * 
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date parseStringToSqlDate(String dateStr) {
		return java.sql.Date.valueOf(dateStr);
	}

	/**
	 * 获得指定格式的日期对象
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static java.util.Date changeDateFormat(Date date, String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		try {
			date = formatter.parse(formatter.format(date));
		} catch (ParseException e) {
			// logger.error(ResourceBundleService.getString("txt.exception"),
			// e);
		}

		return date;
	}

	/**
	 * 获得指定格式的日期的字符串
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String getDateFormatStr(Date date, String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(date);
	}

	/**
	 * 将时间(毫秒)转化为字符串
	 * 
	 * @param time
	 * @return 格式 (XX时XX分XX秒)
	 */
	/*
	 * public static String parseTimeToString(long time){ StringBuffer
	 * stringBuffer = new StringBuffer(); long hour = 0,minute = 0,second = 0;
	 * time = time/1000; if(time>=3600){ hour = time/3600;
	 * stringBuffer.append(hour);
	 * stringBuffer.append(ResourceBundleService.getString("txt.common.hour"));
	 * } if(time>=60){ minute = (time-(hour*3600))/60;
	 * stringBuffer.append(minute);
	 * stringBuffer.append(ResourceBundleService.getString
	 * ("txt.common.minute")); } second = time-(hour*3600)-(minute*60);
	 * stringBuffer.append(second);
	 * stringBuffer.append(ResourceBundleService.getString
	 * ("txt.common.second")); return stringBuffer.toString(); }
	 */
	/**
	 * 日期增加-按日增加
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date dateIncreaseByDay(Date date, int days) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

	/**
	 * 当前时间增加
	 * 
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date dateIncrease(int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * 指定时间增加
	 * 
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date dateIncrease(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/** 距离当前时间天数,整数 */
	public static Integer distanceNowDay(Date dueDate) {
		int day = (int) ((getCurrentUtilDate().getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24L));
		return day;
	}

	/** 距离当前时间天数，小数 */
	public static double distanceNowDayDouble(Date dueDate) {
		Date d1 = getCurrentUtilDate();// 当前时间
		double l = (double) dueDate.getTime() - d1.getTime();
		double d = l / (24 * 60 * 60 * 1000);
		return d;
	}

	/** 返回两个日期之间的的分钟数 */
	public static Integer MinuteBetween(Date earlyDay, Date lateDay) {
		int min = (int) ((lateDay.getTime() - earlyDay.getTime()) / (1000L * 60));
		return min;
	}

	/** 返回两个日期之间的秒数 */
	public static Integer secondBetween(Date earlyDay, Date lateDay) {
		int m = (int) ((lateDay.getTime() - earlyDay.getTime()) / 1000L);
		return m;
	}

	/** 获取当前月的第一天 00:00:00 */
	public static Date getCurrentMonthFirstDay() {
		// java.text.SimpleDateFormat df = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.set(Calendar.DAY_OF_MONTH, 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		return gc.getTime();
	}

	/** 距离当前时间秒数 */
	public static Integer distanceNowSecond(Date date) {
		int second = (int) ((System.currentTimeMillis() - date.getTime()) / 1000L);
		return second;
	}

	/** 距离当前时间秒数 */
	public static Integer distanceNowSecond(long date) {
		int second = (int) ((System.currentTimeMillis() - date) / 1000L);
		return second;
	}

	//
	/** 获取上个月的第一天 00:00:00 */
	public static Date getFirstDayOfLastMonth() {
		// java.text.SimpleDateFormat df = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.set(Calendar.DAY_OF_MONTH, 1);
		gc.add(Calendar.MONTH, -1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		return gc.getTime();
	}

	/** 获取上个月的最后一天 00:00:00 */
	public static Date getLastDayOfLastMonth() {
		// java.text.SimpleDateFormat df = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.set(Calendar.DAY_OF_MONTH, 1);
		gc.add(Calendar.DATE, -1);
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 59);
		return gc.getTime();
	}

	/** 获取本月的最后一天 00:00:00 */
	public static Date getLastDayOfThisMonth() {
		// java.text.SimpleDateFormat df = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.set(Calendar.DAY_OF_MONTH, 1);
		gc.add(Calendar.MONTH, 1);
		gc.add(Calendar.DATE, -1);
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 59);
		return gc.getTime();
	}

	/** 判断两日期是不是同一天 */
	public static boolean isSameDay(Date d1, Date d2) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (formatter.format(d1).equals(formatter.format(d2))) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 获得多少天之前的时间
	 * 
	 * @param days
	 * @return
	 */
	public static java.util.Date getBeforeDays(int days) {
		return new Date(System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000l));
	}
	
	/**
	 * 获得某个时间多少天之前的时间
	 * @param date
	 * @param days
	 * @return
	 */
	public static java.util.Date getBeforeDays(Date date, int days) {
		return new Date(date.getTime() - (days * 24 * 60 * 60 * 1000l));
	}

	/**
	 * 得到当前时间分钟的正点分钟后
	 * 
	 * @param timer
	 * @return
	 */
	public static java.util.Date getMinuteAfterUtilDate(int minute) {
		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(getCurrentUtilDate());
		cal.add(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 根据分表策略获取分表后缀
	 * 
	 * @param type
	 *            分表策略
	 * @return
	 */
	public static String getTableSuffixByType(int type) {
		Calendar c = Calendar.getInstance();
		String tableSuffix = "";

		if (type == DATE_BY_MONTH) {
			String strMonth = c.get(Calendar.MONTH) + 1 + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}

			tableSuffix = c.get(Calendar.YEAR) + "" + (strMonth + "01");
			return tableSuffix;
		} else if (type == DATE_BY_WEEK) {
			int a = c.get(Calendar.DAY_OF_WEEK);
			c.add(Calendar.DATE, 2 - a); // 本周的第一天
			String strMonth = c.get(Calendar.MONTH) + 1 + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}
			String strDay = c.get(Calendar.DAY_OF_MONTH) + "";
			if (strDay.length() == 1) {
				strDay = "0" + strDay;
			}

			tableSuffix = c.get(Calendar.YEAR) + "" + (strMonth + strDay);
			return tableSuffix;
		} else if (type == DATE_BY_DAY) {
			String strMonth = c.get(Calendar.MONTH) + 1 + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}

			tableSuffix = c.get(Calendar.YEAR) + ""
					+ (strMonth + c.get(Calendar.DAY_OF_MONTH) + 1);
			return tableSuffix;
		}

		return tableSuffix;
	}

	/** 根据指定日期取定约定的表名后缀 */
	public static String getTableSuffixByTypeAndDate(int type, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String tableSuffix = "";

		if (type == DATE_BY_MONTH) {
			String strMonth = c.get(Calendar.MONTH) + 1 + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}

			tableSuffix = c.get(Calendar.YEAR) + "" + (strMonth + "01");
			return tableSuffix;
		} else if (type == DATE_BY_WEEK) {
			int a = c.get(Calendar.DAY_OF_WEEK);
			c.add(Calendar.DATE, 2 - a); // 本周的第一天
			String strMonth = c.get(Calendar.MONTH) + 1 + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}
			String strDay = c.get(Calendar.DAY_OF_MONTH) + "";
			if (strDay.length() == 1) {
				strDay = "0" + strDay;
			}

			tableSuffix = c.get(Calendar.YEAR) + "" + (strMonth + strDay);
			return tableSuffix;
		} else if (type == DATE_BY_DAY) {
			String strMonth = c.get(Calendar.MONTH) + 1 + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}

			tableSuffix = c.get(Calendar.YEAR) + ""
					+ (strMonth + c.get(Calendar.DAY_OF_MONTH) + 1);
			return tableSuffix;
		}

		return tableSuffix;
	}

	/**
	 * 获得指定日期的00点00分00秒，返回java.util.Date
	 * 
	 * @return
	 */
	public static java.util.Date getDayFirstUtilDate(Date date) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
		return c.getTime();
	}

	/**
	 * 获得指定日期的23点59分59秒，返回java.util.Date
	 * 
	 * @return
	 */
	public static java.util.Date getDayLastUtilDate(Date date) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return c.getTime();
	}

	/**
	 * 如果当前时间在 date time 之前 返回 今天time 如果在date之后 返回date下一天time
	 * 
	 * @param time
	 *            HH:mm:ss
	 * @throws ParseException
	 */
	public static Date nextUpdateTiem(String time, Date date)
			throws ParseException {
		return nextUpdateTiem(time, "HH:mm:ss", date);
	}

	/**
	 * 
	 * @param time
	 * @param format
	 *            时分秒的格式 比如 HH:mm:ss HH:mm
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date nextUpdateTiem(String time, String format, Date date)
			throws ParseException {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy:MM:dd");
		SimpleDateFormat sp1 = new SimpleDateFormat("yyyy:MM:dd " + format);
		String freshTime = time;

		String todayStr = sp.format(date);
		Date todayfreshTime = sp1.parse(todayStr + " " + freshTime);
		if (date.before(todayfreshTime)) {
			return todayfreshTime;
		} else {
			Date tomorrow = DateUtil.dateIncreaseByDay(date, 1);
			String tomorrowStr = sp.format(tomorrow);
			Date tomorrowfreshTime = sp1.parse(tomorrowStr + " " + freshTime);
			return tomorrowfreshTime;
		}

	}

	/**
	 * 获取离本周末凌晨0点的间隔时间
	 * 
	 * @return
	 */
	public static int getWeekendTime() {
		Calendar c = GregorianCalendar.getInstance();
		int datePos = c.get(Calendar.DAY_OF_WEEK) == 1 ? 0 : (8 - c
				.get(Calendar.DAY_OF_WEEK));
		long a = c.getTimeInMillis();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH) + datePos, 23, 59, 59);
		//logger.debug("c:" + c.toString());
		long b = c.getTimeInMillis();
		return (int) ((b - a) / 1000);
	}

	/**
	 * 相隔天数(0点分隔)
	 * 
	 * @param early
	 * @param late
	 * @return
	 */
	public static final int daysBetween(Date early, Date late) {

		java.util.Calendar calst = java.util.Calendar.getInstance();
		java.util.Calendar caled = java.util.Calendar.getInstance();
		calst.setTime(early);
		caled.setTime(late);
		// 设置时间为0时
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
				.getTime().getTime() / 1000)) / 3600 / 24;

		return days;
	}

	/**
	 * 获取指定时间的 零点正
	 */
	public static final Date getDayZeor(Date date) {
		java.util.Calendar caled = java.util.Calendar.getInstance();
		caled.setTime(date);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		return caled.getTime();
	}

	/**
	 * 返回服务器时间戳
	 * 
	 * @return
	 */
	public static final int getTimestamp() {
		Date current = getCurrentUtilDate();
		return (int) (current.getTime() & 0x7FFF);
	}

	/**
	 * 返回32位置的时间戳
	 * 
	 * @return
	 */
	public static final int getTimestamp(Date date) {
		return (int) (date.getTime() & 0x7FFF);
	}

	/**
	 * 返回32位置的时间戳
	 * 
	 * @return
	 */
	public static final int getTimestamp(long date) {
		return (int) (date & 0x7FFF);
	}

	public static final long now() {
		return System.currentTimeMillis();
	}

	public static final boolean isToday(Date date) {
		Date today = new Date();
		return daysBetween(date, today) == 0;
	}

	public static final boolean isToday(long ts) {
		return isToday(new Date(ts));
	}

	/**
	 * 判断两个日期是否是在同一周（周一为一周内的第一天）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		cal2.setFirstDayOfWeek(Calendar.MONDAY);
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	public static int diffWeek(Calendar start, Calendar end) {
		int sumSunday = 0;
		int cmp = start.compareTo(end);
		int p = 1;
		if (cmp < 0) {
			// start 比 over 小
		} else if (cmp > 0) {
			// start 比 over 大
			Calendar tmp = end;
			end = start;
			start = tmp;
			p = -1;
		} else {
			return 0;
		}
		while (start.compareTo(end) != 0) {
			if (start.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				++sumSunday;
			}
			start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
		}
		return sumSunday * p;
	}
	
	public static void main(String[] args) {
		long times = System.currentTimeMillis();
		System.out.println("time="+timesFormatToSeconds(times)); 
	}

}
