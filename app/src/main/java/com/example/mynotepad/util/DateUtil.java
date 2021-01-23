package com.example.mynotepad.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date时间工具类
 * Created by Administrator on 2020/5/27.
 */

public class DateUtil {

    private static final String TAG = "DateUtil";

    /**
     * 获取格式化的日期字符串
     * @param date 日期时间
     * @return 格式化的日期字符串
     */
    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd EEE hh:mm a");
        return dateFormat.format(date);
    }

    /**
     * 获取字符串日期的Date对象
     * @param dateStr 字符串日期（无周几等具体信息）
     * @return Date对象，转换失败返回null
     */
    public static Date stringToDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            Log.d(TAG, "stringToDate: parse fail");
            return null;
        }
    }

    /**
     * 获取Date对象的long型
     * @param date Date对象
     * @return long型日期
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 获取字符串日期的long型
     * @param dateStr 字符串日期
     * @return long型日期，转换失败返回0
     */
    public static long stringToLong(String dateStr) {
        Date date = stringToDate(dateStr);
        if (date == null) {
            Log.d(TAG, "stringToLong: stringToDate parse fail");
            return 0;
        }
        return dateToLong(date);
    }
}
