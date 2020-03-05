package com.example.apps.mega.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.apps.mega.entity.Category;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> findAll();

    @Delete
    Single<Integer> delete(Category category);

    @Insert
    Single<Long> insert(Category category);

    @Update
    Single<Integer> update(Category category);
}
