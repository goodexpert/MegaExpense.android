package com.example.apps.mega.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class TransactionCategory implements Serializable {

    public int id;

    public Date datetime;

    public String date;

    public Float value;

    @ColumnInfo(name = "currency_code")
    public String currencyCode;

    @ColumnInfo(name = "category_id")
    public int categoryId;

    @ColumnInfo(name = "category_name")
    public String categoryName;

    @ColumnInfo(name = "category_color")
    public Integer categoryColor;

    @Expose
    public Map<String, Double> rates;
}
