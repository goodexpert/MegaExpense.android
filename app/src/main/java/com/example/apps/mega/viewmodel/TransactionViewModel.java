package com.example.apps.mega.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apps.mega.AppInterface;
import com.example.apps.mega.entity.Category;
import com.example.apps.mega.entity.TransactionCategory;
import com.example.apps.mega.entity.Transaction;
import com.example.apps.mega.exception.InvalidFieldException;
import com.example.apps.mega.repository.CategoryRepository;
import com.example.apps.mega.repository.ExchangeRateRepository;
import com.example.apps.mega.repository.TransactionRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransactionViewModel extends ViewModel {
    private LiveData<String> currencyLiveData;
    private LiveData<List<Category>> categories;
    private LiveData<List<TransactionCategory>> transactionsOrderByAsc;
    private LiveData<List<TransactionCategory>> transactionsOrderByDesc;
    private MutableLiveData<Transaction> transactionLiveData;

    private AppInterface appInterface;
    private ExchangeRateRepository exchangeRateRepository;
    private TransactionRepository transactionRepository;

    public TransactionViewModel(AppInterface appInterface, CategoryRepository categoryRepository, ExchangeRateRepository exchangeRateRepository, TransactionRepository transactionRepository) {
        this.categories = categoryRepository.getCategories();
        this.transactionsOrderByAsc = transactionRepository.getTransactionsOrderByAsc();
        this.transactionsOrderByDesc = transactionRepository.getTransactionsOrderByDesc();
        this.transactionLiveData = new MutableLiveData<>();
        this.currencyLiveData = appInterface.getCurrencyLiveData();

        this.appInterface = appInterface;
        this.exchangeRateRepository = exchangeRateRepository;
        this.transactionRepository = transactionRepository;
    }

    public LiveData<List<Category>> getCategories() {
        return this.categories;
    }

    public LiveData<List<TransactionCategory>> getTransactionsOrderByAsc() {
        return this.transactionsOrderByAsc;
    }

    public LiveData<List<TransactionCategory>> getTransactionsOrderByDesc() {
        return this.transactionsOrderByDesc;
    }

    public LiveData<String> getCurrencyLiveData() {
        return currencyLiveData;
    }

    public void setDefaultCurrencyCode(String currencyCode) {
        this.appInterface.setDefaultCurrencyCode(currencyCode);
    }

    public Observable<Long> insertTransaction(Transaction transaction) {
        return Observable.just(transaction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(it -> {
                    if (it.datetime == null) {
                        throw new InvalidFieldException("datetime", "Field 'datetime' can't be null");
                    } else if (it.value == null) {
                        throw new InvalidFieldException("value", "Field 'value' can't be null");
                    } else if (TextUtils.isEmpty(it.currencyCode)) {
                        throw new InvalidFieldException("currency", "Field 'currency' can't be null");
                    } else if (it.categoryId == 0) {
                        throw new InvalidFieldException("categoryId", "Field 'categoryId' is invalid value");
                    }
                    return this.exchangeRateRepository.updateExchangeRate(transaction.datetime);
                })
                .switchMap(it -> this.transactionRepository.insert(transaction));
    }

    public Observable<Integer> updateTransaction(Transaction transaction) {
        return Observable.just(transaction)
                .flatMap(it -> {
                    if (it.datetime == null) {
                        throw new InvalidFieldException("datetime", "Field 'datetime' can't be null");
                    } else if (it.value == null) {
                        throw new InvalidFieldException("value", "Field 'value' can't be null");
                    } else if (TextUtils.isEmpty(it.currencyCode)) {
                        throw new InvalidFieldException("currency", "Field 'currency' can't be null");
                    } else if (it.categoryId == 0) {
                        throw new InvalidFieldException("categoryId", "Field 'categoryId' is invalid value");
                    }
                    return this.exchangeRateRepository.updateExchangeRate(transaction.datetime);
                })
                .switchMap(it -> this.transactionRepository.update(transaction));
    }

    public LiveData<Transaction> getTransactionLiveData() {
        return this.transactionLiveData;
    }

    public void setTransactionLiveData(Transaction transaction) {
        this.transactionLiveData.postValue(transaction);
    }

}
