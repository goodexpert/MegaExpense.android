package com.example.apps.mega.repository;

import androidx.lifecycle.LiveData;

import com.example.apps.mega.database.AppDatabase;
import com.example.apps.mega.database.dao.TransactionDao;
import com.example.apps.mega.entity.TransactionCategory;
import com.example.apps.mega.entity.Transaction;
import com.example.apps.mega.network.WebService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransactionRepository {
    private final AppDatabase appDatabase;
    private final TransactionDao transactionDao;
    private final WebService webService;

    public TransactionRepository(AppDatabase appDatabase, WebService webService) {
        this.appDatabase = appDatabase;
        this.transactionDao = appDatabase.transactionDao();
        this.webService = webService;
    }

    public LiveData<List<TransactionCategory>> getTransactionsOrderByAsc() {
        return transactionDao.getTransactionsOrderByAsc();
    }

    public LiveData<List<TransactionCategory>> getTransactionsOrderByDesc() {
        return transactionDao.getTransactionsOrderByDesc();
    }

    public Observable<Integer> delete(Transaction transaction) {
        return transactionDao.delete(transaction)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Long> insert(Transaction transaction) {
        return transactionDao.insert(transaction)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Integer> update(Transaction transaction) {
        return transactionDao.update(transaction)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
