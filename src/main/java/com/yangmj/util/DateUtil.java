package com.yangmj.util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类。<br>
 * 
 * @author liufei
 * @version
 */
/*
 * 修订历史 版本|日期|修改人|修改信息 1.0  增加doc......
 */
public class DateUtil {

    /**
     * 根据指定的格式字符串，将日期对象转化为字符串
     * 
     * @param date
     *            日期对象
     * @param pattern
     *            格式字符串，默认格式为：yyyy-MM-dd
     * @return
     */
    public static String formatDate(Date date, String pattern) {
	if (date == null)
	    return "";
	if (pattern == null)
	    pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	return (sdf.format(date));
    }

    /**
     * 将日期对象转化为指定格式（yyyy-MM-dd HH:mm:ss）的字符串
     * 
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
	return (formatDate(date, "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将当前系统时间转化为指定格式（yyyy-MM-dd HH:mm:ss）的字符串
     * 
     * @return
     */
    public static String formatDateTime() {
	return (formatDate(now(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将当前系统时间转化为指定格式（yyyyMMddHHmmss）的字符串
     * 
     * @return
     */
    public static String formatDateTime2() {
	return (formatDate(now(), "yyyyMMddHHmmss"));
    }

    /**
     * 将日期对象转化为指定格式（yyyyMMddHHmmss）的字符串
     * 
     * @return
     */
    public static String formatDateTime2(Date date) {
	return (formatDate(date, "yyyyMMddHHmmss"));
    }

    /**
     * 将指定的日期对象转化为默认格式的（yyyy-MM-dd）字符串
     * 
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
	return (formatDate(date, "yyyy-MM-dd"));
    }

    /**
     * 将当前时间转化为指定格式（yyyy-MM-dd）的字符串
     * 
     * @return
     */
    public static String formatDate() {
	return (formatDate(now(), "yyyy-MM-dd"));
    }

    /**
     * 从指定的日期对象中，获取指定格式（HH:mm:ss）的时间字符串
     * 
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
	return (formatDate(date, "HH:mm:ss"));
    }

    /**
     * 从系统当前时间获取时间信息，转化为指定格式（HH:mm:ss）的字符串
     * 
     * @return
     */
    public static String formatTime() {
	return (formatDate(now(), "HH:mm:ss"));
    }

    /**
     * 从当前系统时间中获取时间信息，转化为指定格式（HHmmss）的字符串
     * 
     * @return
     */
    public static String formatTime2() {
	return (formatDate(now(), "HHmmss"));
    }

    /**
     * 获取系统当前日期对象
     * 
     * @return
     */
    public static Date now() {
	return (new Date());
    }

    /**
     * 将字符串转化为默认格式（yyyy-MM-dd HH:mm:ss）的时间对象
     * 
     * @param datetime
     * @return
     */
    public static Date parseDateTime(String datetime) {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	if ((datetime == null) || (datetime.equals(""))) {
	    return null;
	} else {
	    try {
		return formatter.parse(datetime);
	    } catch (ParseException e) {
		return parseDate(datetime);
	    }
	}
    }

    /**
     * 将字符串转化为指定格式（yyyy-MM-dd）的日期日期对象
     * 
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	if ((date == null) || (date.equals(""))) {
	    return null;
	} else {
	    try {
		return formatter.parse(date);
	    } catch (ParseException e) {
		return null;
	    }
	}
    }

    /**
     * 将日期对象，转化为指定格式（yyyy-MM-dd）的日期对象
     * 
     * @param datetime
     * @return
     */
    public static Date parseDate(Date datetime) {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	if (datetime == null) {
	    return null;
	} else {
	    try {
		return formatter.parse(formatter.format(datetime));
	    } catch (ParseException e) {
		return null;
	    }
	}
    }

    /**
     * 将字符串转化为指定格式（yyyyMMdd）的日期对象
     * 
     * @param date
     * @return
     */
    public static Date parseDate2(String date) {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

	if ((date == null) || (date.equals(""))) {
	    return null;
	} else {
	    try {
		return formatter.parse(date);
	    } catch (ParseException e) {
		return null;
	    }
	}
    }

    /**
     * 将日期对象格式化为指定格式（yyyy-MM-dd）的日期字符串
     * 
     * @param o
     * @return
     */
    public static String formatDate(Object o) {
	if (o == null)
	    return "";
	if (o.getClass() == String.class)
	    return formatDate(o);
	else if (o.getClass() == Date.class)
	    return formatDate((Date) o);
	else if (o.getClass() == Timestamp.class) {
	    return formatDate(new Date(((Timestamp) o).getTime()));
	} else
	    return o.toString();
    }

    /**
     * 将日期对象格式化为指定格式（yyyy-MM-dd HH:mm:ss）的日期字符串
     * 
     * @param o
     * @return
     */
    public static String formatDateTime(Object o) {
	if (o.getClass() == String.class)
	    return formatDateTime(o);
	else if (o.getClass() == Date.class)
	    return formatDateTime((Date) o);
	else if (o.getClass() == Timestamp.class) {
	    return formatDateTime(new Date(((Timestamp) o).getTime()));
	} else
	    return o.toString();
    }

    /**
     * 给时间加上或减去指定毫秒，秒，分，时，天、月或年等，返回变动后的时间
     * 
     * @param date
     *            要加减前的时间，如果不传，则为当前日期
     * @param field
     *            时间域，有Calendar.MILLISECOND,Calendar.SECOND,Calendar.MINUTE,<br>
     *            Calendar.HOUR,Calendar.DATE, Calendar.MONTH,Calendar.YEAR
     * @param amount
     *            按指定时间域加减的时间数量，正数为加，负数为减。
     * @return 变动后的时间
     */
    public static Date add(Date date, int field, int amount) {
	if (date == null) {
	    date = new Date();
	}

	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(field, amount);

	return cal.getTime();
    }

    /**
     * 对指定的日期对象，增加（减少）指定的毫秒数
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addMilliSecond(Date date, int amount) {
	return add(date, Calendar.MILLISECOND, amount);
    }

    /**
     * 对指定的日期对象，增加（减少）指定的秒数
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addSecond(Date date, int amount) {
	return add(date, Calendar.SECOND, amount);
    }

    /**
     * 对指定的日期对象，增加（减少）指定的分数
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addMiunte(Date date, int amount) {
	return add(date, Calendar.MINUTE, amount);
    }

    /**
     * 对指定的日期对象，增加（减少）指定的小时数
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addHour(Date date, int amount) {
	return add(date, Calendar.HOUR, amount);
    }

    /**
     * 对指定的日期对象，增加（减少）指定的日期数
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addDay(Date date, int amount) {
	return add(date, Calendar.DATE, amount);
    }

    /**
     * 对指定的日期对象，增加（减少）指定的月数
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addMonth(Date date, int amount) {
	return add(date, Calendar.MONTH, amount);
    }

    /**
     * 对指定的日期对象，增加（减少）指定的年数
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addYear(Date date, int amount) {
	return add(date, Calendar.YEAR, amount);
    }

    /**
     * 格式化（yyyy-MM-dd）当前时间为时间对象
     * 
     * @return
     */
    public static Date getDate() {
	return parseDate(formatDate());
    }

    /**
     * 将当前日期对象格式化为：yyyy-MM-dd HH:mm:ss 格式的日期对象
     * 
     * @return
     */
    public static Date getDateTime() {
	return parseDateTime(formatDateTime());
    }

    /**
     * 根据毫秒值，获取日期字符串
     * 
     * @param time
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getDateStrByTime(long time) {
	Date date = new Date(time);
	return date.toLocaleString().substring(0, date.toLocaleString().indexOf(" "));
    }

    /**
     * 按指定格式生成指定日期字符。
     * 
     * @param date
     * @param format
     * @return
     */
    public static String toDateString(Date date, String format) {
	if (date == null) {
	    return "";
	}
	SimpleDateFormat dateFormat = format != null ? new SimpleDateFormat(format) : new SimpleDateFormat();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return dateFormat.format(date);
    }
    
	/**
	 * 获得当前年月日用于人员编号生成策略
	 * SimpleDateFormat("yyyyMMdd")
	 * @return SimpleDateFormat("yyyyMMdd") 字符串类型
	 */
	public static String getIDGenerationRules() {
		return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 获得当前日期与本周一相差的天数
	 * 
	 * @return
	 */
	public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    } 
	
	/**
	 * 获得当前周--周一的日期
	 * 
	 * @return
	 */
	public static  String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
	/**
	 * 获得当前周--周日的日期
	 * 
	 * @return
	 */
	public static String getCurrentSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus +6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

	/**
	 * 获得当前月--月初日期
	 * 
	 * @return
	 */
    public static String getMinMonthDate() {   
             Calendar calendar = Calendar.getInstance();   
              try {
                 calendar.setTime(parseDate(new Date()));
                 calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
                 return formatDate(calendar.getTime());
               } catch (Exception e) {
               e.printStackTrace();
              }
            return null;
    }

    /**
	 * 获得当前月--月末日期
	 * 
	 * @return
	 */
    public static String getMaxMonthDate(){   
         Calendar calendar = Calendar.getInstance();   
         try {
                calendar.setTime(parseDate(new Date()));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                return formatDate(calendar.getTime());
         }  catch (Exception e) {
                e.printStackTrace();
          }
        return null;
    }
    
    
    /**
	 * 日期天数加减
	 * 
	 * @param dateStr
	 *            日期 yyyyMMdd
	 * @param dayNums
	 *            天数
	 * @return
	 */
	public static String addDays(String dateStr, int dayNums) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			// 加天
			cal.add(Calendar.DATE, dayNums);
			return sdf.format(cal.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 日期天数减
	 * 
	 * @param dateStr
	 *            日期 yyyyMMdd
	 * @param dayNums
	 *            天数
	 * @return
	 */
	public static String reduceDays(String dateStr, int dayNums) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			//减天
			cal.set(Calendar.DATE, cal.get(Calendar.DATE)-dayNums);
			return sdf.format(cal.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * 将当前系统时间转化为指定格式（yyyyMMdd）的字符串
     * 
     * @return
     */
    public static String formatDateTime3() {
	return (formatDate(now(), "yyyyMMdd"));
    }
    /**
     * 将当前系统时间转化为指定格式（MMDDHHMMSS）的字符串
     * 
     * @return
     */
    public static String formatDateTime4() {
    	return (formatDate(now(), "MMddHHmmss"));
    }

	public static String yyyyToYYYY(String date) {

		SimpleDateFormat sdfx = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd");
		String nowTime2 = "";
		try {
			nowTime2 = sdfh.format(sdfx.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return nowTime2;
	}

	//获取当天的开始时间
	public static java.util.Date getDayBegin() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	//获取当天的结束时间
	public static java.util.Date getDayEnd() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	//获取昨天的开始时间
	public static Date getBeginDayOfYesterday() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayBegin());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	//获取昨天的结束时间
	public static Date getEndDayOfYesterDay() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayEnd());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	//获取明天的开始时间
	public static Date getBeginDayOfTomorrow() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayBegin());
		cal.add(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}
	//获取明天的结束时间
	public static Date getEndDayOfTomorrow() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayEnd());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	//获取本周的开始时间
	@SuppressWarnings("unused")
	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}
	//获取本周的结束时间
	public static Date getEndDayOfWeek(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}
	//获取上周的开始时间
	@SuppressWarnings("unused")
	public static Date getBeginDayOfLastWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek - 7);
		return getDayStartTime(cal.getTime());
	}
	//获取上周的结束时间
	public static Date getEndDayOfLastWeek(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfLastWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}
	//获取本月的开始时间
	public static Date getBeginDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 1, 1);
		return getDayStartTime(calendar.getTime());
	}
	//获取本月的结束时间
	public static Date getEndDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 1, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getNowYear(), getNowMonth() - 1, day);
		return getDayEndTime(calendar.getTime());
	}
	//获取上月的开始时间
	public static Date getBeginDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 2, 1);
		return getDayStartTime(calendar.getTime());
	}
	//获取上月的结束时间
	public static Date getEndDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 2, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getNowYear(), getNowMonth() - 2, day);
		return getDayEndTime(calendar.getTime());
	}
	//获取本年的开始时间
	public static java.util.Date getBeginDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getNowYear());
		// cal.set
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);

		return getDayStartTime(cal.getTime());
	}
	//获取本年的结束时间
	public static java.util.Date getEndDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getNowYear());
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DATE, 31);
		return getDayEndTime(cal.getTime());
	}
	//获取某个日期的开始时间
	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if(null != d) calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}
	//获取某个日期的结束时间
	public static Timestamp getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if(null != d) calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Timestamp(calendar.getTimeInMillis());
	}
	//获取今年是哪一年
	public static Integer getNowYear() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return Integer.valueOf(gc.get(1));
	}
	//获取本月是哪一月
	public static int getNowMonth() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return gc.get(2) + 1;
	}
	//两个日期相减得到的天数
	public static int getDiffDays(Date beginDate, Date endDate) {

		if (beginDate == null || endDate == null) {
			throw new IllegalArgumentException("getDiffDays param is null!");
		}

		long diff = (endDate.getTime() - beginDate.getTime())
				/ (1000 * 60 * 60 * 24);

		int days = new Long(diff).intValue();

		return days;
	}
	//两个日期相减得到的毫秒数
	public static long dateDiff(Date beginDate, Date endDate) {
		long date1ms = beginDate.getTime();
		long date2ms = endDate.getTime();
		return date2ms - date1ms;
	}
	//获取两个日期中的最大日期
	public static Date max(Date beginDate, Date endDate) {
		if (beginDate == null) {
			return endDate;
		}
		if (endDate == null) {
			return beginDate;
		}
		if (beginDate.after(endDate)) {
			return beginDate;
		}
		return endDate;
	}
	//获取两个日期中的最小日期
	public static Date min(Date beginDate, Date endDate) {
		if (beginDate == null) {
			return endDate;
		}
		if (endDate == null) {
			return beginDate;
		}
		if (beginDate.after(endDate)) {
			return endDate;
		}
		return beginDate;
	}
	//返回某月该季度的第一个月
	public static Date getFirstSeasonDate(Date date) {
		final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int sean = SEASON[cal.get(Calendar.MONTH)];
		cal.set(Calendar.MONTH, sean * 3 - 3);
		return cal.getTime();
	}
	//返回某个日期下几天的日期
	public static Date getNextDay(Date date, int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
		return cal.getTime();
	}
	//返回某个日期前几天的日期
	public static Date getFrontDay(Date date, int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
		return cal.getTime();
	}
	//获取某年某月到某年某月按天的切片日期集合（间隔天数的集合）
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getTimeList(int beginYear, int beginMonth, int endYear,
								   int endMonth, int k) {
		List list = new ArrayList();
		if (beginYear == endYear) {
			for (int j = beginMonth; j <= endMonth; j++) {
				list.add(getTimeList(beginYear, j, k));

			}
		} else {
			{
				for (int j = beginMonth; j < 12; j++) {
					list.add(getTimeList(beginYear, j, k));
				}

				for (int i = beginYear + 1; i < endYear; i++) {
					for (int j = 0; j < 12; j++) {
						list.add(getTimeList(i, j, k));
					}
				}
				for (int j = 0; j <= endMonth; j++) {
					list.add(getTimeList(endYear, j, k));
				}
			}
		}
		return list;
	}
	//获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getTimeList(int beginYear, int beginMonth, int k) {
		List list = new ArrayList();
		Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
		int max = begincal.getActualMaximum(Calendar.DATE);
		for (int i = 1; i < max; i = i + k) {
			list.add(begincal.getTime());
			begincal.add(Calendar.DATE, k);
		}
		begincal = new GregorianCalendar(beginYear, beginMonth, max);
		list.add(begincal.getTime());
		return list;
	}

}
