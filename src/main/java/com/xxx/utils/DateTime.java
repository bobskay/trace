package com.xxx.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期对象,继承java.util.Date
 */
@Slf4j
public class DateTime extends Date {

    public static final int YEAR_TO_YEAR = 11;
    public static final int YEAR_TO_MONTH = 12;
    public static final int YEAR_TO_DAY = 13;
    public static final int YEAR_TO_HOUR = 14;
    public static final int YEAR_TO_MINUTE = 15;
    public static final int YEAR_TO_SECOND = 16;
    public static final int YEAR_TO_MILLISECOND = 17;
    public static final int MONTH_TO_MONTH = 22;
    public static final int MONTH_TO_DAY = 23;
    public static final int MONTH_TO_HOUR = 24;
    public static final int MONTH_TO_MINUTE = 25;
    public static final int MONTH_TO_SECOND = 26;
    public static final int MONTH_TO_MILLISECOND = 27;
    public static final int DAY_TO_DAY = 33;
    public static final int DAY_TO_HOUR = 34;
    public static final int DAY_TO_MINUTE = 35;
    public static final int DAY_TO_SECOND = 36;
    public static final int DAY_TO_MILLISECOND = 37;
    public static final int HOUR_TO_HOUR = 44;
    public static final int HOUR_TO_MINUTE = 45;
    public static final int HOUR_TO_SECOND = 46;
    public static final int HOUR_TO_MILLISECOND = 47;
    public static final int MINUTE_TO_MINUTE = 55;
    public static final int MINUTE_TO_SECOND = 56;
    public static final int MINUTE_TO_MILLISECOND = 57;
    public static final int SECOND_TO_SECOND = 66;
    public static final int SECOND_TO_MILLISECOND = 67;
    public static final int MILLISECOND_TO_MILLISECOND = 77;
    public static final int YEAR_TO_MILLISECOND_STRING = 78;
    public static final int YEAR_TO_DAY_STRING = 79;
    public static final int YEAR_TO_SECOND_STRING = 80;

    private static final String delimiter = "-";

    private int type;

    public boolean isLeap() {
        return isLeap(this.getYear());
    }

    /**
     * 判断某年是否闰年
     */
    public static boolean isLeap(int year) {
        if (year % 4 == 0 && year % 100 != 0) {
            return true;
        }
        if (year % 400 == 0) {
            return true;
        }
        return false;
    }

    public DateTime(Date date) {
        this(date.getTime());
    }

    public DateTime(long time) {
        if (time == new DateTime(time, DateTime.YEAR_TO_DAY).getTime()) {
            init(new Date(time), DateTime.YEAR_TO_DAY);
        } else {
            init(new Date(time), DateTime.YEAR_TO_SECOND);
        }
    }

    public DateTime(long time, int type) {
        this(new Date(time), type);
    }

    /**
     * 通过默认格式创建时间
     *
     * @param dateString 时间串,格式必须为yyyy-MM-dd HH:mm:SS或yyyy-mm-dd
     */
    public DateTime(String dateString) {
        if (dateString.indexOf(" ") == -1) {
            init(dateString, DateTime.YEAR_TO_DAY);
        } else {
            init(dateString, DateTime.YEAR_TO_SECOND);
        }

    }

    /**
     * 通过字符串创建时间对象
     *
     * @param dateString
     * @param type       日期格式,详见DateTime.getDateFormat
     *                   DateTime.YEAR_TO_YEAR yy
     *                   DateTime.YEAR_TO_MONTH yy-mm
     *                   DateTime.YEAR_TO_DAY yy-mm-dd
     *                   ....
     */
    public DateTime(String dateString, int type) {
        init(dateString, type);
    }

    // 初始化当前对象,所有创建方法最终都得调到这里
    private void init(String dateTimeString, int type) {
        try {
            SimpleDateFormat dateFormat = getDateFormat(type);
            Date date = dateFormat.parse(dateTimeString);
            this.type = type;
            super.setTime(date.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("unable to parse " + dateTimeString, e);
        }
    }

    public DateTime(Date date, int type) {
        SimpleDateFormat dateFormat = getDateFormat(type);
        this.init(dateFormat.format(date), type);
    }

    private void init(Date date, int type) {
        SimpleDateFormat dateFormat = getDateFormat(type);
        this.init(dateFormat.format(date), type);
    }

    public String toString() {
        return toString(type);
    }

    @SuppressWarnings("deprecation")
    public int getYear() {
        return Integer.parseInt(getDateFormat(YEAR_TO_YEAR).format(this));
    }

    @SuppressWarnings("deprecation")
    public int getMonth() {
        return Integer.parseInt(getDateFormat(MONTH_TO_MONTH).format(this));
    }

    @SuppressWarnings("deprecation")
    public int getDay() {
        return Integer.parseInt(getDateFormat(DAY_TO_DAY).format(this));
    }

    // 周几
    public int getWeek() {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(this);
        int week = aCalendar.get(Calendar.DAY_OF_WEEK);
        if (week == 1) {
            return 7;
        } else {
            return week - 1;
        }
    }

    // 当前月份的天数
    public int getDaysOfMonth() {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, this.getYear());
        // year年
        time.set(Calendar.MONTH, this.getMonth() - 1);
        // Calendar对象默认一月为0,month月
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day;
    }

    public int getHour() {
        return Integer.parseInt(getDateFormat(HOUR_TO_HOUR).format(this));
    }

    public int getMinute() {
        return Integer.parseInt(getDateFormat(MINUTE_TO_MINUTE).format(this));
    }

    public int getSecond() {
        return Integer.parseInt(getDateFormat(SECOND_TO_SECOND).format(this));
    }

    public static SimpleDateFormat getDateFormat(int type) {
        String pattern = "";
        switch (type) {
            case YEAR_TO_YEAR:
                return getFormat("yyyy");

            case YEAR_TO_MONTH:
                pattern = "yyyy" + delimiter + "MM";
                return getFormat(pattern);

            case YEAR_TO_DAY:
                pattern = "yyyy" + delimiter + "MM" + delimiter + "dd";
                return getFormat(pattern);

            case YEAR_TO_HOUR:
                pattern = "yyyy" + delimiter + "MM" + delimiter + "dd HH";
                return getFormat(pattern);

            case YEAR_TO_MINUTE:
                pattern = "yyyy" + delimiter + "MM" + delimiter + "dd HH:mm";
                return getFormat(pattern);

            case YEAR_TO_SECOND:
                pattern = "yyyy" + delimiter + "MM" + delimiter + "dd HH:mm:ss";
                return getFormat(pattern);

            case YEAR_TO_MILLISECOND:
                pattern = "yyyy" + delimiter + "MM" + delimiter + "dd HH:mm:ss.SSS";
                return getFormat(pattern);

            case MONTH_TO_MONTH:
                pattern = "MM";
                return getFormat(pattern);

            case MONTH_TO_DAY:
                pattern = "MM" + delimiter + "dd";
                break;

            case MONTH_TO_HOUR:
                pattern = "MM" + delimiter + "dd HH";
                return getFormat(pattern);

            case MONTH_TO_MINUTE:
                pattern = "MM" + delimiter + "dd HH:mm";
                return getFormat(pattern);

            case MONTH_TO_SECOND:
                pattern = "MM" + delimiter + "dd HH:mm:ss";
                return getFormat(pattern);

            case MONTH_TO_MILLISECOND:
                pattern = "MM" + delimiter + "dd HH:mm:ss.SSS";
                return getFormat(pattern);

            case DAY_TO_DAY:
                pattern = "dd";
                return getFormat(pattern);

            case DAY_TO_HOUR:
                pattern = "dd HH";
                return getFormat(pattern);

            case DAY_TO_MINUTE:
                pattern = "dd HH:mm";
                return getFormat(pattern);

            case DAY_TO_SECOND:
                pattern = "dd HH:mm:ss";
                return getFormat(pattern);

            case DAY_TO_MILLISECOND:
                pattern = "dd HH:mm:ss.SSS";
                return getFormat(pattern);

            case HOUR_TO_HOUR:
                pattern = "HH";
                return getFormat(pattern);

            case HOUR_TO_MINUTE:
                pattern = "HH:mm";
                return getFormat(pattern);

            case HOUR_TO_SECOND:
                pattern = "HH:mm:ss";
                return getFormat(pattern);

            case HOUR_TO_MILLISECOND:
                pattern = "HH:mm:ss.SSS";
                return getFormat(pattern);

            case MINUTE_TO_MINUTE:
                pattern = "mm";
                return getFormat(pattern);

            case MINUTE_TO_SECOND:
                pattern = "mm:ss";
                return getFormat(pattern);

            case MINUTE_TO_MILLISECOND:
                pattern = "mm:ss.SSS";
                return getFormat(pattern);

            case SECOND_TO_SECOND:
                pattern = "ss";
                return getFormat(pattern);

            case SECOND_TO_MILLISECOND:
                pattern = "ss.SSS";
                return getFormat(pattern);

            case MILLISECOND_TO_MILLISECOND:
                pattern = "SSS";
                return getFormat(pattern);

            case YEAR_TO_DAY_STRING:
                pattern = "yyyyMMdd";
                return getFormat(pattern);
            case YEAR_TO_MILLISECOND_STRING:
                pattern = "yyyyMMddHHmmssSSS";
                return getFormat(pattern);
            case YEAR_TO_SECOND_STRING:
                pattern = "yyyyMMddHHmmss";
                return getFormat(pattern);
            default:
                throw new RuntimeException("暂不支持类型" + type);
        }
        throw new RuntimeException("暂不支持类型" + type);
    }

    public String toString(int type) {
        SimpleDateFormat dateFormat = getDateFormat(type);
        return dateFormat.format(this);
    }

    public static String toString(long time, int type) {
        SimpleDateFormat dateFormat = getDateFormat(type);
        return dateFormat.format(time);
    }

    public static DateTime current() {
        SimpleDateFormat dateFormat = getDateFormat(YEAR_TO_MILLISECOND);
        return new DateTime(dateFormat.format(new Date()), YEAR_TO_MILLISECOND);
    }

    public static DateTime current(int type) {
        SimpleDateFormat dateFormat = getDateFormat(type);
        return new DateTime(dateFormat.format(new Date()), type);
    }

    public DateTime addDay(int day) {
        DateTime dt = new DateTime(toString());
        dt.setTime(getTime() + (long) day * 0x5265c00L);
        return dt;
    }

    public DateTime addMonth(int iMonth) {
        DateTime dt = (DateTime) clone();
        GregorianCalendar gval = new GregorianCalendar();
        gval.setTime(dt);
        gval.add(2, iMonth);
        dt.setTime(gval.getTime().getTime());
        return dt;
    }

    public DateTime addYear(int iYear) {
        DateTime dt = (DateTime) clone();
        GregorianCalendar gval = new GregorianCalendar();
        gval.setTime(dt);
        gval.add(1, iYear);
        dt.setTime(gval.getTime().getTime());
        return dt;
    }

    public DateTime addHour(int hour) {
        DateTime dt = (DateTime) clone();
        dt.setTime(getTime() + (long) hour * 3600000L);
        return dt;
    }

    public DateTime addMinute(int minute) {
        DateTime dt = (DateTime) clone();
        dt.setTime(getTime() + (long) minute * 60000L);
        return dt;
    }

    public DateTime addSecond(int second) {
        DateTime dt = (DateTime) clone();
        dt.setTime(getTime() + (long) second * 1000L);
        return dt;
    }

    public DateTime addMilliSecond(long mSecond) {
        DateTime dt = (DateTime) clone();
        dt.setTime(getTime() + mSecond);
        return dt;
    }

    public static String show(Date time) {
        return new DateTime(time, DateTime.YEAR_TO_MILLISECOND).toString();
    }

    /**
     * 将一个毫秒值转换为**小时**分钟**秒的形式
     */
    public static String showTime(long time) {
        if (time < 1000) {
            return time + "毫秒";
        }
        StringBuilder sb = new StringBuilder();

        long year = time / 24 / 60 / 60 / 1000 / 365;
        if (year != 0) {
            sb.append(year + "年");
        }
        time = time % (24 * 60 * 60 * 1000 * 365);

        long day = time / 24 / 60 / 60 / 1000;
        if (day != 0) {
            sb.append(day + "天");
        }

        time = time % (24 * 60 * 60 * 1000);
        long hour = time / 60 / 60 / 1000;
        if (day != 0 || hour != 0) {
            sb.append(hour + "小时");
        }

        time = time % (60 * 60 * 1000);
        long min = time / 60 / 1000;
        if (day != 0 || hour != 0 || min != 0) {
            sb.append(min + "分钟");
        }

        time = time % (60 * 1000);
        long second = time / 1000;
        if (day != 0 || hour != 0 || min != 0 || second != 0) {
            sb.append(second + "秒");
        }

        time = time % (1000);
        long ms = time;
        if ((day != 0 || hour != 0 || min != 0 || second != 0) && ms != 0) {
            sb.append(ms + "毫秒");
        }
        return sb.toString();
    }

    public Date toDate() {
        return new Date(this.getTime());
    }

    //每次都new一个SimpleDateFormat会占用过多内存,未每个线程造一个SimpleDateFormat
    private static Map<String, ThreadLocal<SimpleDateFormat>> map = new HashMap();

    public static SimpleDateFormat getFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> t = map.get(pattern);
        if (t != null) {
            return t.get();
        }
        synchronized (DateTime.class) {
            t = map.get(pattern);
            if (t != null) {
                return t.get();
            }

            t = new ThreadLocal<SimpleDateFormat>() {
                public SimpleDateFormat initialValue() {
                    log.debug(Thread.currentThread().getName() + "创建dateformat:" + pattern);
                    return new SimpleDateFormat(pattern);
                }
            };
            map.put(pattern, t);
            return t.get();
        }
    }
}
