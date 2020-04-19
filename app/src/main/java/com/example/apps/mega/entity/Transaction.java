package com.example.apps.mega.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(
        tableName = "transactions",
        foreignKeys = {
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = {"category_id"},
                        childColumns = {"category_id"},
                        onDelete = ForeignKey.SET_DEFAULT
                )
        },
        indices = {
                @Index(value = {"date", "category_id"}, unique = true)
        }
)
public class Transaction implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date datetime;

    public String date;

    public Float value;

    @ColumnInfo(name = "currency_code")
    public String currencyCode;

    @ColumnInfo(name = "category_id")
    public int categoryId;
}
