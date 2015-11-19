package com.bison.app.commons.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class is used for conversion between Date and String;
 * @author oeager
 */
public class DateUtil {

	/**
	 * <P>yyyy-MM-dd HH:mm:ss</P>
	 */
	public final static ThreadLocal<SimpleDateFormat> ymdhmsFormater = new ThreadLocal<SimpleDateFormat>() {
		@SuppressLint("SimpleDateFormat")
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	/**
	 * <P>yyyy-MM-dd</P>
	 */
	public final static ThreadLocal<SimpleDateFormat> ymdFormater = new ThreadLocal<SimpleDateFormat>() {
		@SuppressLint("SimpleDateFormat")
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	/**
	 * Description: * gets a string that represents the current date and time.
	 * @param format format string, such as: "yyyy-MM-dd HH:mm:ss"
	 * the current date and time of type String @return String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate(String format) {
	
		String curDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curDateTime;

	}
	/**
	 * convert the String str to Date type;
	 *
	 * @param sdate
	 * @return format date <P>just like: 2014-12-12:12:12:56</P>
	 */
	public static Date toDate(String sdate) {
		try {
			return ymdhmsFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	
	/**
	 * determine time is today for the given string;
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate){
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if(time != null){
			String nowDate = ymdFormater.get().format(today);
			String timeDate = ymdFormater.get().format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	/**
	 * Number of days between two dates calculated difference
	 */
	public static int getOffectDay(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0) {
			maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 + maxDays;
		} else if (y1 - y2 < 0) {
			maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 - maxDays;
		} else {
			day = d1 - d2;
		}
		return day;
	}
	
	/**
	 *	calculate the number of hours between two difference dates;
	 */
	public static int getOffectHour(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
		int h = 0;
		int day = getOffectDay(date1, date2);
		h = h1-h2+day*24;
		return h;
	}
	
	/**
	 * the offset minutes between two dates
	 */
	public static int getOffectMinutes(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int m1 = calendar1.get(Calendar.MINUTE);
		int m2 = calendar2.get(Calendar.MINUTE);
		int h = getOffectHour(date1, date2);
		int m = 0;
		m = m1-m2+h*60;
		return m;
	}/**
	 * to determine a year is Leap or not
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * determine whether the date is greater than today
	 * @param time:the string str must formated {"yyyy-MM-dd HH:mm:ss"}
	 * @return
	 */
	public static boolean isEndofToday(String time){
		Date times = toDate(time);
		Date today = new Date();
		return today.before(times);
		
	}

	public static String  getCurrentDate(SimpleDateFormat format){
		Date date = new Date();
		return format.format(date);
	}

}
