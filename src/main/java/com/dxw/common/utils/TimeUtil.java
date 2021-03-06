/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author pronics3
 */
public class TimeUtil {

    public static String formatDate(Date date){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(date);
    }

    public static String formatTime(Date date){
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        return f.format(date);
    }

    public static String formatDateTime(Date date){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    public static Date parseDate(String source){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return f.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }
    public static String getCurrentTime() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        String s = f.format(Calendar.getInstance().getTime());
        return s;
    }
    
    public static String getCurrentDate() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String s = f.format(Calendar.getInstance().getTime());
        return s;
    }
    
    public static String getCurrentDateTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(Calendar.getInstance().getTime());
        return s;
    }


    /**
     * 判断是上午还是下午
     * @return
     */
    public static boolean isAmOrPm(){
        LocalTime time = LocalTime.now();
        if( time.getHour() <=12)
            return true;
        else
            return false;
    }

    public static LocalDate toLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toDate(LocalDate localDate){
        return  Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
