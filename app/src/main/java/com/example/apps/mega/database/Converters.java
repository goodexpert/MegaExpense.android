package com.example.apps.mega.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Map<String, Double> jsonToMapRate(String value) {
        Type mapTYpe = new TypeToken<Map<String, Double>>(){}.getType();
        return new Gson().fromJson(value, mapTYpe);
    }

    @TypeConverter
    public static String mapRateToString(Map<String, Double> map) {
        return new Gson().toJson(map);
    }
}
