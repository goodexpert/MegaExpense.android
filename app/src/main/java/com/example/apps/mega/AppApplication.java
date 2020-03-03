package com.example.apps.mega;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.example.apps.mega.di.component.AppComponent;
import com.example.apps.mega.di.component.DaggerAppComponent;
import com.example.apps.mega.di.module.AppModule;

public class AppApplication extends Application implements AppInterface {

    public AppComponent appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();

    @Override
    public void onCreate() {
        super.onCreate();
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
}
