package com.example.apps.mega;

import android.content.SharedPreferences;

public interface AppInterface {
    AppApplication getApplication();
    SharedPreferences getSharedPreferences();

    void postAction(Runnable action);
    void postAction(Runnable action, Long delayMillis);
}
