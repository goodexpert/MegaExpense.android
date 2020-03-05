package com.example.apps.mega.ui.setting.category.edit;

import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.apps.mega.R;
import com.example.apps.mega.entity.Category;
import com.example.apps.mega.exception.InvalidFieldException;
import com.example.apps.mega.ui.base.BaseFragment;
import com.example.apps.mega.viewmodel.CategoryViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class EditCategoryFragment extends BaseFragment {
    private CategoryViewModel viewModel;
    private Category category;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.category = new Category();
        try {
            Category category = (Category) getArguments().getSerializable("category");
            this.category.id = category.id;
            this.category.categoryName = category.categoryName;
            this.category.categoryColor = category.categoryColor;
        } catch (Exception ex) {
            this.category.categoryColor = Color.BLACK;
        }

        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(CategoryViewModel.class);
        viewModel.setCategoryLiveData(this.category);
        viewModel.getCategoryLiveData().observe(this, category -> {
            updateData(getView(), category);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateData(view, this.category);
    }

    private Boolean isEditMode() {
        return getArguments() != null &&
                getArguments().getSerializable("category") instanceof Category;
    }

    private void updateData(View view, Category category) {
        final TextInputEditText txtName = view.findViewById(R.id.txtName);
        txtName.setText(category.categoryName);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                category.categoryName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final View palette = view.findViewById(R.id.palette);
        palette.setBackgroundColor(category.categoryColor);
        palette.setOnClickListener(v -> {
            ColorPickerFragment picker = new ColorPickerFragment();
            picker.setOnChangedColor(color -> {
                category.categoryColor = color;
                viewModel.setCategoryLiveData(category);
            });
            picker.show(getChildFragmentManager(), "");
        });

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            onSave(category);
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            findNavController().popBackStack();
        });
    }

    private void onSave(Category category) {
        if (isEditMode()) {
            viewModel.updateCategory(category)
                    .subscribe(it -> {
                        findNavController().popBackStack();
                    }, throwable -> {
                        if (throwable instanceof InvalidFieldException) {
                            showMessage("Error", throwable.getMessage());
                        }
                    });
        } else {
            viewModel.insertCategory(category)
                    .subscribe(it -> {
                        findNavController().popBackStack();
                    }, throwable -> {
                        if (throwable instanceof InvalidFieldException) {
                            showMessage("Error", throwable.getMessage());
                        } else if (throwable instanceof SQLiteConstraintException) {
                            showMessage("Error", "Already exists the category name");
                        }
                    });
        }
        hideSoftKeyboard();
    }
}
