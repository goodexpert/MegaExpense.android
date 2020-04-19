package com.example.apps.mega.repository;

import androidx.lifecycle.LiveData;

import com.example.apps.mega.Const;
import com.example.apps.mega.database.AppDatabase;
import com.example.apps.mega.database.dao.ExchangeRateDao;
import com.example.apps.mega.entity.ExchangeRate;
import com.example.apps.mega.network.WebService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExchangeRateRepository {
    private AppDatabase appDatabase;
    private ExchangeRateDao exchangeRateDao;
    private WebService webService;

    public ExchangeRateRepository(AppDatabase appDatabase, WebService webService) {
        this.appDatabase = appDatabase;
        this.exchangeRateDao = appDatabase.exchangeRateDao();
        this.webService = webService;
    }

    public LiveData<List<ExchangeRate>> getExchangeRates() {
        return exchangeRateDao.findAll();
    }

    public Observable<ExchangeRate> updateExchangeRate(Date datetime) {
        String date = formatDate(datetime);
        return exchangeRateDao.findByDate(date)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(updateExchangeRate(date, "USD", "NZD"));
    }

    private Observable<ExchangeRate> updateExchangeRate(String date, String source, String currencies) {
        return Observable.just(date)
                .switchMap(it -> {
                    String today = formatDate(new Date());
                    if (today.equals(it))
                        return webService.getLiveList(Const.ACCESS_KEY, source, currencies, date, 1);
                    else
                        return webService.getHistoricalList(Const.ACCESS_KEY, source, currencies, date, 1);
                })
                .retry(3)
                .flatMap(it -> {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.date = date;
                    exchangeRate.currencyCode = source;
                    exchangeRate.rates = new HashMap<>(it.quotes);
                    return exchangeRateDao.insert(exchangeRate).toObservable();
                })
                .flatMap(it -> exchangeRateDao.findById(it).toObservable());
    }

    public Observable<Integer> deleteExchangeRate(ExchangeRate exchangeRate) {
        return exchangeRateDao.delete(exchangeRate)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Long> insertExchangeRate(ExchangeRate exchangeRate) {
        return exchangeRateDao.insert(exchangeRate)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Integer> updateExchangeRate(ExchangeRate exchangeRate) {
        return exchangeRateDao.update(exchangeRate)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return date != null ? df.format(date) : "";
    }
}
