package com.maulanakurnia.salesrecord.utils;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * source : https://developer.android.com/training/data-storage/room/referencing-data#type-converters
 */
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
