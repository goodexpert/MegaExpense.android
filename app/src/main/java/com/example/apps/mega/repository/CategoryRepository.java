package com.example.apps.mega.repository;

import androidx.lifecycle.LiveData;

import com.example.apps.mega.database.AppDatabase;
import com.example.apps.mega.database.dao.CategoryDao;
import com.example.apps.mega.entity.Category;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CategoryRepository {
    private final AppDatabase appDatabase;
    private final CategoryDao categoryDao;

    public CategoryRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.categoryDao = appDatabase.categoryDao();
    }

    public LiveData<List<Category>> getCategories() {
        return categoryDao.findAll();
    }

    public Observable<Integer> delete(Category category) {
        return categoryDao.delete(category)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Long> insert(Category category) {
        return categoryDao.insert(category)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Integer> update(Category category) {
        return categoryDao.update(category)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
