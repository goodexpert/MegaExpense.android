package com.example.apps.mega.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apps.mega.AppInterface;

public class SettingViewModel extends ViewModel {
    private MutableLiveData<String> defaultCurrencyCode;
    private AppInterface appInterface;

    public SettingViewModel(AppInterface appInterface) {
        this.defaultCurrencyCode = new MutableLiveData<>(appInterface.getDefaultCurrencyCode());
        this.appInterface = appInterface;
    }

    public LiveData<String> getDefaultCurrencyCode() {
        return this.defaultCurrencyCode;
    }

    public void setDefaultCurrencyCode(String defaultCurrencyCode) {
        this.appInterface.setDefaultCurrencyCode(defaultCurrencyCode);
        this.defaultCurrencyCode.postValue(defaultCurrencyCode);
    }
}
