package com.example.test_information.userlogin;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateUtil {
    @SuppressLint("SimpleDateFormat")
    public static String getNowDateTime() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat s_format = new SimpleDateFormat("\n" + "yyyy-MM-dd HH:mm");
        return s_format.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss");
        return s_format.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean getDiffTime(String time,String nowTime) throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat s_format = new SimpleDateFormat("\n" + "yyyy-MM-dd HH:mm");
        Date fromDate2 = s_format.parse(time);
        Date toDate2 = s_format.parse(nowTime);
        long from2 = fromDate2.getTime();
        long to2 = toDate2.getTime();
        int minutes = (int) ((to2 - from2) / (1000 * 60));
        System.out.println( minutes);
        if( minutes>60) return false;  //60分钟换一次
        return true;
    }

}
