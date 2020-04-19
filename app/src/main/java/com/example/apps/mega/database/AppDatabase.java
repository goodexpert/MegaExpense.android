package com.example.apps.mega.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.apps.mega.database.dao.ExchangeRateDao;
import com.example.apps.mega.database.dao.TransactionDao;
import com.example.apps.mega.database.dao.CategoryDao;
import com.example.apps.mega.entity.Category;
import com.example.apps.mega.entity.ExchangeRate;
import com.example.apps.mega.entity.Transaction;

@Database(entities = {Category.class, ExchangeRate.class, Transaction.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "mega_expense";

    public abstract CategoryDao categoryDao();
    public abstract ExchangeRateDao exchangeRateDao();
    public abstract TransactionDao transactionDao();
}
