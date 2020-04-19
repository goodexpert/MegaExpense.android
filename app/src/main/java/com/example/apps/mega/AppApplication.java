package com.example.apps.mega;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apps.mega.di.component.AppComponent;
import com.example.apps.mega.di.component.DaggerAppComponent;
import com.example.apps.mega.di.module.AppModule;
import com.example.apps.mega.utils.PreferenceUtils;

import java.util.Set;

import javax.inject.Inject;

public class AppApplication extends Application implements AppInterface {

    @Inject
    PreferenceUtils preferenceUtils;

    private MutableLiveData<String> defaultCurrencyCode;

    public AppComponent appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent.inject(this);

        defaultCurrencyCode = new MutableLiveData<>(getDefaultCurrencyCode());
    }

    @Override
    public AppApplication getApplication() {
        return this;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    @Override
    public void postAction(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    @Override
    public void postAction(Runnable action, Long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(action, delayMillis);
    }

    @Override
    public LiveData<String> getCurrencyLiveData() {
        return this.defaultCurrencyCode;
    }

    public String getDefaultCurrencyCode() {
        return preferenceUtils.getString(Const.PREF_DEFAULT_CURRENCY_CODE, "USD");
    }

    @Override
    public void setDefaultCurrencyCode(String currencyCode) {
        preferenceUtils.putPreferences(Const.PREF_DEFAULT_CURRENCY_CODE, currencyCode);
        this.defaultCurrencyCode.postValue(currencyCode);
    }

    @Override
    public Set<String> getCurrencyCodes() {
        return preferenceUtils.getStringSet(Const.PREF_CURRENCY_CODES, Const.DEFAULT_CURRENCY_CODES);
    }
}
