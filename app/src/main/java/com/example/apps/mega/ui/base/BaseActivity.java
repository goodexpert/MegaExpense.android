package com.example.apps.mega.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apps.mega.AppApplication;
import com.example.apps.mega.AppInterface;
import com.example.apps.mega.di.component.AppComponent;

import javax.inject.Inject;

abstract public class BaseActivity extends AppCompatActivity implements OnFragmentInterface {

    @Inject
    public AppInterface appInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);
    }

    @Override
    public AppComponent getAppComponent() {
        return ((AppApplication) getApplication()).appComponent;
    }

    @Override
    public void postAction(Runnable action) {
        appInterface.postAction(action);
    }

    @Override
    public void postAction(Runnable action, Long delayMillis) {
        appInterface.postAction(action, delayMillis);
    }

}
