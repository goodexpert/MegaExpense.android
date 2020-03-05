package com.example.apps.mega.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Map;

@Entity(
        tableName = "exchange_rates",
        indices = {
                @Index(value = {"date", "currency_code"}, unique = true)
        }
)
public class ExchangeRate implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;

    @ColumnInfo(name = "currency_code")
    public String currencyCode;

    @Expose
    public Map<String, Double> rates;
}
