package com.example.apps.mega.ui.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apps.mega.AppInterface;
import com.example.apps.mega.di.component.AppComponent;
import com.example.apps.mega.network.WebService;

import javax.inject.Inject;

abstract public class BaseFragment extends Fragment {

    @Inject
    AppInterface appInterface;
    @Inject
    WebService webService;

    private OnFragmentInterface fragmentInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public AppComponent getAppComponent() {
        return fragmentInterface.getAppComponent();
    }

    public void postAction(Runnable action) {
        fragmentInterface.postAction(action);
    }

    public void postAction(Runnable action, Long delayMillis) {
        fragmentInterface.postAction(action, delayMillis);
    }
}
