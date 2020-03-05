package com.example.apps.mega.utils.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.apps.mega.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {
    private final TextView tvContent;
    private final String prefix;

    public MyMarkerView(Context context, int layoutResource, String prefix) {
        super(context, layoutResource);
        this.tvContent = findViewById(R.id.tvContent);
        this.prefix = prefix;
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        float value = 0f;
        if (e instanceof CandleEntry) {
            value = ((CandleEntry) e).getHigh();
        } else {
            value = e.getY();
        }
        tvContent.setText(prefix + Utils.formatNumber(value, 0, true));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
