package com.example.apps.mega.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "categories",
        indices = {
                @Index(value = {"category_name"}, unique = true)
        }
)
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    public int id;

    @ColumnInfo(name = "category_name")
    public String categoryName;

    @ColumnInfo(name = "category_color")
    public Integer categoryColor;
}
