package com.example.apps.mega.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.apps.mega.R;
import com.example.apps.mega.ui.base.BaseFragment;
import com.example.apps.mega.viewmodel.SettingViewModel;

public class SettingFragment extends BaseFragment {
    private SettingViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(SettingViewModel.class);
        viewModel.getDefaultCurrencyCode().observe(this, defaultCurrencyCode -> {
            updateView(getView(), defaultCurrencyCode);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView(view, "USD");
    }

    private void updateView(View view, String defaultCurrencyCode) {
        TextView currencyCode = view.findViewById(R.id.currencyCode);
        currencyCode.setText(defaultCurrencyCode);

        setupItemView(view.findViewById(R.id.defaultCurrencyCode), () -> {
            final String[] currencyCodes = { "USD", "NZD" };
            new AlertDialog.Builder(getActivity())
                    .setTitle("Currency")
                    .setSingleChoiceItems(currencyCodes, getIndexByCurrencyCode(defaultCurrencyCode), ((dialog, which) -> {
                        viewModel.setDefaultCurrencyCode(currencyCodes[which]);
                        dialog.dismiss();
                    }))
                    .show();
        });
        setupItemView(view.findViewById(R.id.manageCategory), () -> {
            findNavController().navigate(R.id.nav_category);
        });
    }

    private void setupItemView(View view, Runnable action) {
        view.setOnClickListener(v -> {
            action.run();
        });
    }

    private int getIndexByCurrencyCode(String currencyCode) {
        if ("USD".equalsIgnoreCase(currencyCode))
            return 0;
        else if ("NZD".equalsIgnoreCase(currencyCode))
            return 1;
        return -1;
    }
}