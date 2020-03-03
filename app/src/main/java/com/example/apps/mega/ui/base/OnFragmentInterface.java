package com.example.apps.mega.ui.base;

import com.example.apps.mega.di.component.AppComponent;

public interface OnFragmentInterface {
    AppComponent getAppComponent();

    void postAction(Runnable action);
    void postAction(Runnable action, Long delayMillis);
}
