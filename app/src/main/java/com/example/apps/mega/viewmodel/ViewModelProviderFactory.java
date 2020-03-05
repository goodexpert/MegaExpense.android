package com.example.apps.mega.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.apps.mega.AppInterface;
import com.example.apps.mega.database.AppDatabase;
import com.example.apps.mega.network.WebService;
import com.example.apps.mega.repository.CategoryRepository;
import com.example.apps.mega.repository.ExchangeRateRepository;
import com.example.apps.mega.repository.TransactionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    private AppDatabase appDatabase;
    private AppInterface appInterface;
    private WebService webService;

    private CategoryRepository categoryRepository;
    private ExchangeRateRepository exchangeRateRepository;
    private TransactionRepository transactionRepository;

    @Inject
    public ViewModelProviderFactory(AppDatabase appDatabase, AppInterface appInterface, WebService webService) {
        this.appDatabase = appDatabase;
        this.appInterface = appInterface;
        this.webService = webService;

        this.categoryRepository = new CategoryRepository(appDatabase);
        this.exchangeRateRepository = new ExchangeRateRepository(appDatabase, webService);
        this.transactionRepository = new TransactionRepository(appDatabase, webService);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(this.categoryRepository);
        } else if (modelClass.isAssignableFrom(SettingViewModel.class)) {
            return (T) new SettingViewModel(this.appInterface);
        } else if (modelClass.isAssignableFrom(TransactionViewModel.class)) {
            return (T) new TransactionViewModel(this.appInterface, this.categoryRepository, this.exchangeRateRepository, this.transactionRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
