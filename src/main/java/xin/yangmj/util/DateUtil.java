package xin.yangmj.util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类。<br>
 * 
 * @author yetaishan
 * @version 1.0 ,2010-05-12
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
    
}
