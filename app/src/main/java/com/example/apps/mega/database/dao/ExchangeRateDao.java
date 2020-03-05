package com.example.apps.mega.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.apps.mega.entity.ExchangeRate;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rates")
    LiveData<List<ExchangeRate>> findAll();

    @Query("SELECT * FROM exchange_rates WHERE id = :id")
    Single<ExchangeRate> findById(Long id);

    @Query("SELECT * FROM exchange_rates WHERE date = :date")
    Single<ExchangeRate> findByDate(String date);

    @Delete
    Single<Integer> delete(ExchangeRate exchangeRate);

    @Insert
    Single<Long> insert(ExchangeRate exchangeRate);

    @Update
    Single<Integer> update(ExchangeRate exchangeRate);
}
