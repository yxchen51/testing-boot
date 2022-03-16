package com.open.qa.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * 时间的工具类
 * Created by liang.chen on 2017/3/22.
 */
public class DateUtil {


    /** 秒 */
    public static final long SECONDS = 1000l;
    /** 分钟 */
    public static final long MINUTES = 60 * SECONDS;
    /** 小时 */
    public static final long HOURS = 60 * MINUTES;
    /** 天 */
    public static final long DAYS = 24 * HOURS;

    /**
     * 将传入的毫秒数转为天数。
     *
     * @param timeMillis
     * @return
     */
    public static int timeMillisToDay(long timeMillis) {

        return (int) (timeMillis / 1000 / 60 / 60 / 24);

    }

    /**
     * 将时间精确到秒级——毫秒为全零
     *
     * @param date
     * @return
     */
    public static Date getDateAccurateToSecond(Date date) {

        Date result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String tmp = sdf.format(date);
        try {
            result = sdf.parse(tmp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 将指定的日期提前N天。
     *
     * @param date
     *            指定的日期
     * @param day
     *            提前的天数
     * @return
     */
    public static Date aheadOfTimeInDay(Date date, int day) {

        long timeMillis = date.getTime();
        long ahead = day * 24l * 60l * 60l * 1000l;
        timeMillis -= ahead;
        Date result = new Date();
        result.setTime(timeMillis);

        return result;

    }

    /**
     * days指定不允许出现的日（年月日中的日），若date<br>
     * 中的日为不允许出现的日，则往前移一天。
     *
     * @param date
     * @param days
     * @return
     */
    public static Date avoidDays(Date date, String... days) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String theDay = sdf.format(date);
        boolean flag = false;
        Date result = new Date();
        result.setTime(date.getTime());
        for (String day : days) {
            if (theDay.equals(day)) {
                flag = true;
            }
        }
        if (flag) {
            result = aheadOfTimeInDay(date, 1);
            result = avoidDays(result, days);
        }

        return result;

    }

    /**
     * 将所给出的日期的日（年月日中的日）都设置为同一日。<br>
     * 例：5-13、4-12、3-12设置为：<br>
     * 5-12、4-12、3-12
     *
     * @param dates
     */
    public static void beTheSameDay(Date... dates) {

        int day = 31;
        for (Date date : dates) {
            if (date == null) {
                continue;
            }
            int currentDay = date.getDate();
            if (currentDay < day) {
                day = currentDay;
            }
        }
        for (Date date : dates) {
            if (date == null) {
                continue;
            }
            date.setDate(day);
        }

    }


    public static String dateToStr(Date date, String pattern) {
        return dateToStr(date, pattern, Locale.CHINA);
    }

    public static String dateToStr(Date date, String pattern, Locale locale) {
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        DateFormat ymdhmsFormat = new SimpleDateFormat(pattern, locale);

        return ymdhmsFormat.format(date);
    }

    public static Date strToDate(String str, String pattern)
            throws ParseException {
        return strToDate(str, pattern, Locale.CHINA);
    }

    public static Date strToDate(String str, String pattern, Locale locale)
            throws ParseException {
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        DateFormat ymdhmsFormat = new SimpleDateFormat(pattern, locale);
        return ymdhmsFormat.parse(str);
    }

    public static Date getToday() {
        Calendar ca = Calendar.getInstance();
        return ca.getTime();
    }
    public static Date getTomorrow() {
        Calendar ca = Calendar.getInstance();
        return getTomorrow(ca.getTime());
    }

    public static Date getTomorrow(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE,1);
        return ca.getTime();
    }
    public static Date getYesterday() {
        Calendar ca = Calendar.getInstance();
        return getYesterday(ca.getTime());
    }
    public static Date getYesterday(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE,-1);
        return ca.getTime();
    }

    public static Date mkDate(int year, int month, int date) {
        Calendar ca = Calendar.getInstance();
        ca.set(year, month - 1, date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.format(ca.getTime());
        return ca.getTime();
    }

    public static String getSpecifyDate(int interval, String format) {
        return getSpecifyDate(interval, format, Locale.CHINA);
    }

    public static String getSpecifyDate(int interval, String format,
                                        Locale locale) {
        Calendar cal = new GregorianCalendar();
        cal.add(5, interval);
        return dateToStr(cal.getTime(), format, locale);
    }

    public static String getSpecifyMonth(int interval, String format) {
        return getSpecifyMonth(interval, format, Locale.CHINA);
    }

    public static String getSpecifyMonth(int interval, String format,
                                         Locale locale) {
        Calendar cal = new GregorianCalendar();
        cal.add(2, interval);
        return dateToStr(cal.getTime(), format, locale);
    }

    public static String getSpecifyYear(int interval, String format) {
        return getSpecifyYear(interval, format, Locale.CHINA);
    }

    public static String getSpecifyYear(int interval, String format,
                                        Locale locale) {
        Calendar cal = new GregorianCalendar();
        cal.add(1, interval);
        return dateToStr(cal.getTime(), format, locale);
    }

    public static String getSpecifyDate(String date, int interval, String format) {
        return getSpecifyDate(date, interval, format, Locale.CHINA);
    }

    public static String getSpecifyDate(String date, int interval,
                                        String format, Locale locale) {
        Date d = null;
        try {
            d = strToDate(date, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.add(5, interval);
        return dateToStr(cal.getTime(), format, locale);
    }

    public Date getGmtDate(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.longValue());
        int offset = calendar.get(15) / 3600000 + calendar.get(16) / 3600000;
        calendar.add(10, -offset);
        Date date = calendar.getTime();
        return date;
    }

    /**
	 * 计算两个日期之间相隔天数
	 * @param from
	 * @param to
	 * @return to-from
	 */
	public static int getIntervalDays(Date from,Date to){
		long interval = DateUtils.truncate(to, Calendar.DATE).getTime()-DateUtils.truncate(from, Calendar.DATE).getTime();
		int result = (int)(interval/(1000*3600*24));
		return result;
	}
	
	/**
	 * @category 将Date类型日期格式化
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date dateFormat(Date date, String format){
		String dateTmp = date2String(date, format);
		return string2Date(dateTmp, format);
	}
	
	
	
	/**
	 * @category StringDateTime 2 long
	 * @param dateStr
	 * @return
	 */
	public static Long string2Long(String dateStr){
		return string2Long(dateStr,"yyyy-MM-dd HH:mm:ss");
	}
	
	
	
	/**
	 * @category StringDateTime 2 long
	 * @param dateStr
	 * @return
	 */
	public static Long string2Long(String dateStr, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Long longDate=0L;
		try {
			longDate  =  sdf.parse(dateStr).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return longDate;
	}
	
	/**
	 * @category String 2 date
     * 会根据长度尝试解析格式，如果都失败，就采用yyyy-MM-dd HH:mm:ss格式
	 * @param dateStr
	 * @return
	 */
	public static Date string2Date(String dateStr){
        if(dateStr.length() == 8){
            return string2Date(dateStr,"yyyyMMdd");
        }
        if(dateStr.length() == 10){
            return string2Date(dateStr,"yyyy-MM-dd");
        }
        if(dateStr.length() == 14){
            return string2Date(dateStr,"yyyyMMddHHmmss");
        }
		return string2Date(dateStr,"yyyy-MM-dd HH:mm:ss");
	}
	
	
	/**
	 * @category date 2 String
	 * @param dateStr
	 * @return
	 */
	public static Date string2Date(String dateStr, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try{
			date = sdf.parse(dateStr);
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * @category date 转 String
	 * @param date
	 * @return 返回格式为 yyyy-MM-dd HH:mm:ss日期串
	 */
	public static String date2String(Date date) {

		return date2String(date,"yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * @category date 转 String
	 * @param date
	 * @return 返回格式为 yyyy-MM-dd HH:mm:ss日期串
	 */
	public static String date2String(Date date, String format) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = "";
		try {
			dateStr = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}
    
    public static void main(String[] args) {
        try {
            System.out.println(strToDate("15OCT2013", "ddMMMyyy",
                    Locale.ENGLISH));
            System.out.println(getToday());
            System.out.println(dateToStr(new Date(), "ddMMM", Locale.ENGLISH));
            System.out.println(dateToStr(new Date(), "yyyy/dd/MM"));
            System.out.println(getSpecifyDate(2, "ddMMM", Locale.ENGLISH));
            System.out.println(getSpecifyDate(-2, "ddMMM", Locale.ENGLISH));
            System.out.println(getSpecifyMonth(2, "ddMMM", Locale.ENGLISH));
            System.out.println(getSpecifyMonth(-2, "ddMMM", Locale.ENGLISH));
            System.out.println(getSpecifyYear(2, "ddMMMyyyy", Locale.ENGLISH));
            System.out.println(getSpecifyYear(-2, "ddMMMyyyy", Locale.ENGLISH));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
