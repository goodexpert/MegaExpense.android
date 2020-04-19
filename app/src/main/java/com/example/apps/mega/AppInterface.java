package com.example.apps.mega;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import java.util.Set;

public interface AppInterface {
    AppApplication getApplication();
    SharedPreferences getSharedPreferences();

    void postAction(Runnable action);
    void postAction(Runnable action, Long delayMillis);

    LiveData<String> getCurrencyLiveData();
    String getDefaultCurrencyCode();
    void setDefaultCurrencyCode(String currencyCode);

    Set<String> getCurrencyCodes();
}
