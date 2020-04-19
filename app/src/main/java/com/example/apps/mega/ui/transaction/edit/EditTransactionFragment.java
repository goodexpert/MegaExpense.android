package com.example.apps.mega.ui.transaction.edit;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.apps.mega.R;
import com.example.apps.mega.entity.Category;
import com.example.apps.mega.entity.Transaction;
import com.example.apps.mega.entity.TransactionCategory;
import com.example.apps.mega.exception.InvalidFieldException;
import com.example.apps.mega.ui.base.BaseFragment;
import com.example.apps.mega.viewmodel.TransactionViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class EditTransactionFragment extends BaseFragment {
    private TransactionViewModel viewModel;
    private String[] currencyCodes = { "USD", "NZD" };

    private Boolean isEditMode = false;
    private Transaction transaction;
    private List<Category> categories = new ArrayList<>();
    private List<String> categoryNames = new ArrayList<>();

    private TextInputEditText txtDate;
    private TextInputEditText txtCurrency;
    private TextInputEditText txtCategory;
    private TextInputEditText txtValue;

    private final Subject<View> clickDate = PublishSubject.create();
    private final Subject<View> clickCurrency = PublishSubject.create();
    private final Subject<View> clickCategory = PublishSubject.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.transaction = new Transaction();
        try {
            TransactionCategory transactionCategory = (TransactionCategory) getArguments().getSerializable("transaction");
            this.transaction.id = transactionCategory.id;
            this.transaction.datetime = transactionCategory.datetime;
            this.transaction.date = transactionCategory.date;
            this.transaction.value = transactionCategory.value;
            this.transaction.currencyCode = transactionCategory.currencyCode;
            this.transaction.categoryId = transactionCategory.categoryId;
            this.isEditMode = true;
        } catch (Exception ex) {
            this.transaction.datetime = new Date();
            this.transaction.date = formatDate(this.transaction.datetime);
            this.transaction.currencyCode = appInterface.getDefaultCurrencyCode();
        }

        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(TransactionViewModel.class);
        viewModel.setTransactionLiveData(this.transaction);
        viewModel.getTransactionLiveData().observe(this, transaction -> {
            updateData(transaction, this.categories);
        });
        viewModel.getCategories().observe(this, categories -> {
            updateData(this.transaction, categories);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.txtDate = view.findViewById(R.id.txtDate);
        txtDate.setOnClickListener(v -> {
            clickDate.onNext(v);
        });
        clickDate.debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> onClickedDate(v));

        this.txtCurrency = view.findViewById(R.id.txtCurrency);
        txtCurrency.setOnClickListener(v -> {
            clickCurrency.onNext(v);
        });
        clickCurrency.debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> onClickedCurrency(v));

        this.txtCategory = view.findViewById(R.id.txtCategory);
        txtCategory.setOnClickListener(v -> {
            clickCategory.onNext(v);
        });
        clickCategory.debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> onClickedCategory(v));

        this.txtValue = view.findViewById(R.id.txtValue);
        txtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    transaction.value = Float.valueOf(s.toString());
                } catch (NumberFormatException ex) {
                    transaction.value = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            onSave();
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            findNavController().popBackStack();
        });

        updateView(view);
    }

    private void updateData(Transaction transaction, List<Category> categories) {
        this.transaction = transaction;
        this.categories = categories;
        this.categoryNames.clear();
        for (Category category : categories) {
            this.categoryNames.add(category.categoryName);
        }
        updateView(getView());
    }

    private void updateView(View view) {
        txtDate.setText(formatDate(transaction.datetime));
        txtCurrency.setText(transaction.currencyCode);
        txtCategory.setText(getNameByCategoryId(transaction.categoryId));
        txtValue.setText(formatValue(transaction.value));
    }

    private Boolean isEditMode() {
        return this.isEditMode;
    }

    private void onClickedDate(View view) {
        DatePickerFragment picker = new DatePickerFragment(transaction.datetime);
        picker.setOnChangedDateListener(datetime -> {
            transaction.datetime = datetime;
            transaction.date = formatDate(datetime);
            viewModel.setTransactionLiveData(transaction);
        });
        picker.show(getChildFragmentManager(), "picker");
    }

    private void onClickedCurrency(View view) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Currency")
                .setSingleChoiceItems(currencyCodes, getIndexByCurrencyCode(transaction.currencyCode), ((dialog, which) -> {
                    transaction.currencyCode = currencyCodes[which];
                    viewModel.setTransactionLiveData(transaction);
                    dialog.dismiss();
                }))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void onClickedCategory(View view) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Category")
                .setSingleChoiceItems(categoryNames.toArray(new String[0]), getIndexByCategoryId(transaction.categoryId), ((dialog, which) -> {
                    transaction.categoryId = this.categories.get(which).id;
                    viewModel.setTransactionLiveData(transaction);
                    dialog.dismiss();
                }))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void onSave() {
        if (isEditMode()) {
            viewModel.updateTransaction(transaction)
                    .subscribe(it -> {
                        findNavController().popBackStack();
                    }, throwable -> {
                        if (throwable instanceof InvalidFieldException) {
                            showMessage("Error", throwable.getMessage());
                        }
                    });
        } else {
            viewModel.insertTransaction(transaction)
                    .subscribe(it -> {
                        findNavController().popBackStack();
                    }, throwable -> {
                        if (throwable instanceof InvalidFieldException) {
                            showMessage("Error", throwable.getMessage());
                        } else if (throwable instanceof SQLiteConstraintException) {
                            showMessage("Error", "Already have the category item on the date");
                        }
                    });
        }
        hideSoftKeyboard();
    }

    private String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return date != null ? df.format(date) : "";
    }

    private String formatValue(Float value) {
        return value != null ? String.format("%.2f", value) : null;
    }

    private int getIndexByCurrencyCode(String currencyCode) {
        if ("USD".equalsIgnoreCase(currencyCode))
            return 0;
        else if ("NZD".equalsIgnoreCase(currencyCode))
            return 1;

        return -1;
    }

    private int getIndexByCategoryId(int categoryId) {
        for (int i = 0; i < this.categories.size(); i++) {
            if (categoryId == this.categories.get(i).id)
                return i;
        }
        return -1;
    }

    private String getNameByCategoryId(int categoryId) {
        for (int i = 0; i < this.categories.size(); i++) {
            if (categoryId == this.categories.get(i).id)
                return this.categories.get(i).categoryName;
        }
        return null;
    }
}
