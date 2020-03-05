package com.example.apps.mega.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.apps.mega.entity.TransactionCategory;
import com.example.apps.mega.entity.Transaction;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY datetime DESC")
    LiveData<List<Transaction>> findAll();

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions t LEFT JOIN  exchange_rates e ON (t.date = e.date AND t.currency_code = e.currency_code) INNER JOIN categories c ON (t.category_id = c.category_id) ORDER BY t.datetime ASC")
    LiveData<List<TransactionCategory>> getTransactionsOrderByAsc();

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions t LEFT JOIN  exchange_rates e ON (t.date = e.date AND t.currency_code = e.currency_code) INNER JOIN categories c ON (t.category_id = c.category_id) ORDER BY t.datetime DESC")
    LiveData<List<TransactionCategory>> getTransactionsOrderByDesc();

    @Delete
    Single<Integer> delete(Transaction transaction);

    @Insert
    Single<Long> insert(Transaction transaction);

    @Update
    Single<Integer> update(Transaction transaction);
}
