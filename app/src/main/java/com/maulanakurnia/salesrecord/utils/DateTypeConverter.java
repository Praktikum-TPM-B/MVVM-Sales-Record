package com.maulanakurnia.salesrecord.utils;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Created by Maulana Kurnia on 5/26/2021
 * Keep Coding & Stay Awesome!
 **/
public class DateTypeConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
