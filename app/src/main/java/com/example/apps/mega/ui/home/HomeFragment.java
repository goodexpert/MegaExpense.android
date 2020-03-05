package com.example.apps.mega.ui.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.apps.mega.R;
import com.example.apps.mega.entity.Category;
import com.example.apps.mega.entity.TransactionCategory;
import com.example.apps.mega.ui.base.BaseFragment;
import com.example.apps.mega.utils.chart.MyMarkerView;
import com.example.apps.mega.utils.chart.NumberValueFormatter;
import com.example.apps.mega.viewmodel.TransactionViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragment implements OnChartValueSelectedListener {
    private TransactionViewModel viewModel;
    private List<Category> categories;
    private List<TransactionCategory> transactions;
    private String currencyCode = "USD";

    private BarChart chart;
    private Date startDate;
    private Date endDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(TransactionViewModel.class);
        viewModel.getTransactionsOrderByAsc().observe(this, transactions -> {
            updateData(categories, transactions, currencyCode);

            // update the transaction list
            this.transactions = transactions;
        });
        viewModel.getCategories().observe(this, categories -> {
            updateData(categories, transactions, currencyCode);

            // update the category list
            this.categories = categories;
        });
        viewModel.getCurrencyLiveData().observe(this, currencyCode -> {
            updateData(categories, transactions, currencyCode);

            // update the currency code
            this.currencyCode = currencyCode;
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView(view);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void updateView(View view) {
        chart = view.findViewById(R.id.chartView);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        // chart.setMaxVisibleValueCount(60);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return formatDate(startDate, (int) value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(new NumberValueFormatter("$"));
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view, "$");
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
//
//        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);
//        mv.setChartView(chart); // For bounds control
//        chart.setMarker(mv); // Set the marker to the chart
    }

    private void updateData(List<Category> categories, List<TransactionCategory> transactions, String currencyCode) {
        if (categories == null || categories.size() == 0 || transactions == null || transactions.size() == 0) return;

        Map<Integer, DataItem> dataItemMap = new HashMap<>();
        for (Category category : categories) {
            DataItem dataItem = new DataItem();
            dataItem.categoryName = category.categoryName;
            dataItem.categoryColor = category.categoryColor;
            dataItemMap.put(category.id, dataItem);
        }
        startDate = transactions.get(0).datetime;
        endDate = transactions.get(transactions.size() - 1).datetime;

        for (int i = 0; i < transactions.size(); i++) {
            TransactionCategory item = transactions.get(i);
            int categoryId = item.categoryId;
            int index = getDiffDate(startDate, item.datetime);

            DataItem dataItem = dataItemMap.get(categoryId);
            dataItem.addBarEntry(index, new BarEntry(index, getValueByCurrencyCode(item, currencyCode)));
        }

        List<IBarDataSet> dataSets = new ArrayList<>();
        int count = getDurationDays(startDate, endDate);

        for (int categoryId : dataItemMap.keySet()) {
            DataItem dataItem = dataItemMap.get(categoryId);
            dataSets.add(dataItem.getBarDataSet(count));
        }

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new NumberValueFormatter("$"));
        data.setValueTypeface(Typeface.DEFAULT);

        chart.setData(data);

        long groupCount = dataItemMap.size();
        long startX = 0;

        float groupSpace = 0.08f;
        float barSpace = 0.12f / groupCount; // x4 DataSet
        float barWidth = 0.8f / groupCount; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startX);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startX + chart.getBarData().getGroupWidth(groupSpace, barSpace) * count);
        chart.groupBars(startX, groupSpace, barSpace);
        chart.invalidate();
    }

    private String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("MM-dd");
        return df.format(date != null ? date : new Date());
    }

    private String formatDate(Date date, int diff) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, diff);
        return formatDate(calendar.getTime());
    }

    private float getValueByCurrencyCode(TransactionCategory transaction, String currencyCode) {
        if (transaction.currencyCode.equals(currencyCode))
            return transaction.value;
        else if (currencyCode.equals("USD")) {
            return (float) (transaction.value / transaction.rates.get("USDNZD"));
        } else {
            return (float) (transaction.value * transaction.rates.get("USDNZD"));
        }
    }

    private int getDiffDate(Date start, Date end) {
        return (int) (end.getTime() - start.getTime()) / 86400000;
    }

    private int getDurationDays(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / 86400000) + 1;
    }

    class DataItem implements Serializable {
        private String categoryName;
        private Integer categoryColor;
        private Map<Integer, BarEntry> entryMap;

        public DataItem() {
            this.entryMap = new HashMap<>();
        }

        public void addBarEntry(int index, BarEntry entry) {
            this.entryMap.put(index, entry);
        }

        public BarDataSet getBarDataSet(int count) {
            List<BarEntry> entries = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                if (entryMap.containsKey(i)) {
                    entries.add(entryMap.get(i));
                } else {
                    entries.add(new BarEntry(i, 0.0f));
                }
            }

            BarDataSet dataSet = new BarDataSet(entries, categoryName);
            dataSet.setColor(categoryColor);
            return dataSet;
        }
    }
}