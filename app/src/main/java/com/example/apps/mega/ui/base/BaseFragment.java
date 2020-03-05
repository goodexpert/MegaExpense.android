package com.example.apps.mega.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.apps.mega.AppInterface;
import com.example.apps.mega.di.component.AppComponent;
import com.example.apps.mega.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

abstract public class BaseFragment extends Fragment {

    @Inject
    public AppInterface appInterface;
    @Inject
    public ViewModelProviderFactory viewModelProviderFactory;

    private OnFragmentInterface fragmentInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getAppComponent().inject(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentInterface = (OnFragmentInterface) context;
        } catch (ClassCastException ex) {
            throw new RuntimeException("OnFragmentInterface not implemented");
        }
    }

    @Override
    public void onDetach() {
        if (fragmentInterface != null) {
            fragmentInterface = null;
        }
        super.onDetach();
    }

    @Override
    public void onStop() {
        hideSoftKeyboard();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            findNavController().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public AppComponent getAppComponent() {
        return fragmentInterface.getAppComponent();
    }

    public void postAction(Runnable action) {
        fragmentInterface.postAction(action);
    }

    public void postAction(Runnable action, Long delayMillis) {
        fragmentInterface.postAction(action, delayMillis);
    }

    public NavController findNavController() {
        return Navigation.findNavController(getView());
    }

    public void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
