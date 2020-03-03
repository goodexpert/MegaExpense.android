package com.example.apps.mega.di.component;

import com.example.apps.mega.AppApplication;
import com.example.apps.mega.di.module.AppModule;
import com.example.apps.mega.di.module.NetworkModule;
import com.example.apps.mega.ui.base.BaseActivity;
import com.example.apps.mega.ui.base.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(AppApplication target);
    void inject(BaseActivity target);
    void inject(BaseFragment target);
}
