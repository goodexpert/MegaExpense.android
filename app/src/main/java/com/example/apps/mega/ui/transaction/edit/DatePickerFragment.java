package com.example.apps.mega.ui.transaction.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.apps.mega.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    private final Date datetime;
    private OnChangedDateListener listener;

    public DatePickerFragment(Date datetime) {
        this.datetime = datetime;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDate(datetime.getTime());
        calendarView.setMaxDate(new Date().getTime());
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, 0, 0, 0);

            if (listener != null) {
                listener.OnChangedDate(calendar.getTime());
            }
            this.dismiss();
        });

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            this.dismiss();
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            this.dismiss();
        });
    }

    public void setOnChangedDateListener(OnChangedDateListener listener) {
        this.listener = listener;
    }

    public interface OnChangedDateListener {
        void OnChangedDate(Date datetime);
    }
}
