package com.example.apps.mega.di.module;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.apps.mega.AppApplication;
import com.example.apps.mega.AppInterface;
import com.example.apps.mega.database.AppDatabase;
import com.example.apps.mega.utils.JsonUtils;
import com.example.apps.mega.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @Singleton
    @Provides
    AppDatabase provideAppDatabase() {
        return Room.databaseBuilder(application.getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        db.execSQL("INSERT INTO categories VALUES(1, 'Earnings', " + Color.BLUE + ")");
                        db.execSQL("INSERT INTO categories VALUES(2, 'Expenditure', " + Color.RED + ")");
                    }
                })
                .build();
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    @Singleton
    @Provides
    JsonUtils provideJsonUtils(Gson defaultGson) {
        return new JsonUtils(defaultGson);
    }

    @Singleton
    @Provides
    PreferenceUtils providePreferenceUtils(AppInterface appInterface, JsonUtils jsonUtils) {
        return new PreferenceUtils(appInterface.getSharedPreferences(), jsonUtils);
    }
}
