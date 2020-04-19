package com.example.apps.mega.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apps.mega.entity.Category;
import com.example.apps.mega.exception.InvalidFieldException;
import com.example.apps.mega.repository.CategoryRepository;

import java.util.List;

import io.reactivex.Observable;

public class CategoryViewModel extends ViewModel {
    private MutableLiveData<Category> categoryLiveData;
    private LiveData<List<Category>> categories;
    private CategoryRepository categoryRepository;

    public CategoryViewModel(CategoryRepository categoryRepository) {
        this.categoryLiveData = new MutableLiveData<>();
        this.categories = categoryRepository.getCategories();
        this.categoryRepository = categoryRepository;
    }

    public LiveData<List<Category>> getCategories() {
        return this.categories;
    }

    public LiveData<Category> getCategoryLiveData() {
        return this.categoryLiveData;
    }

    public void setCategoryLiveData(Category category) {
        this.categoryLiveData.postValue(category);
    }

    public Observable<Long> insertCategory(Category category) {
        return Observable.just(category)
                .flatMap(it -> {
                    if (TextUtils.isEmpty(it.categoryName)) {
                        throw new InvalidFieldException("name", "Field 'name' can't be null");
                    }
                    return this.categoryRepository.insert(category);
                });
    }

    public Observable<Integer> updateCategory(Category category) {
        return Observable.just(category)
                .flatMap(it -> {
                    if (TextUtils.isEmpty(it.categoryName)) {
                        throw new InvalidFieldException("name", "Field 'name' can't be null");
                    }
                    return this.categoryRepository.update(category);
                });
    }
}
