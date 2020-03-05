package com.example.apps.mega.utils.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class NumberValueFormatter extends ValueFormatter {
    private final DecimalFormat mFormat;
    private final String prefix;

    public NumberValueFormatter(String prefix) {
        this.mFormat = new DecimalFormat("###,###,###,##0");
        this.prefix = prefix;
    }

    @Override
    public String getFormattedValue(float value) {
        return value > 0 ? prefix + mFormat.format(value) : "";
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (value > 0) {
            return prefix + mFormat.format(value);
        } else {
            return mFormat.format(value);
        }
    }
}
