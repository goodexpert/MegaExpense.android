package com.example.apps.mega.di.module;

import com.example.apps.mega.Const;
import com.example.apps.mega.network.WebService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

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
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient httpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    public WebService provideWebService(Retrofit retrofit) {
        return retrofit.create(WebService.class);
    }
}
