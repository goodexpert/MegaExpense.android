package com.example.apps.mega.di.module;

import com.example.apps.mega.AppApplication;
import com.example.apps.mega.AppInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private AppApplication application;

    public AppModule(AppApplication application) {
        this.application = application;
    }

    @Singleton
    @Provides
    AppInterface provideAppInterface() {
        return application;
    }

}
