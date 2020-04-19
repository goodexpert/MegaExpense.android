package com.example.apps.mega.ui.transaction;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apps.mega.R;
import com.example.apps.mega.entity.TransactionCategory;
import com.example.apps.mega.ui.base.BaseViewHolder;
import com.example.apps.mega.ui.base.OnItemClickListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<TransactionCategory> items = new ArrayList<>();
    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private final OnItemClickListener<TransactionCategory> onItemClickListener;
    private String currencyCode = "USD";

    public TransactionAdapter(Activity activity, OnItemClickListener<TransactionCategory> onItemClickListener) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.view_list_item_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public TransactionCategory getItem(int position) {
        if (position < 0 || position >= getItemCount()) return null;
        return items.get(position);
    }

    public void setItems(List<TransactionCategory> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        notifyDataSetChanged();
    }

    class ViewHolder extends BaseViewHolder {
        private final TextView datetime;
        private final TextView value;
        private final TextView currency;
        private final TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datetime = itemView.findViewById(R.id.datetime);
            value = itemView.findViewById(R.id.value);
            currency = itemView.findViewById(R.id.currency);
            category = itemView.findViewById(R.id.category);
        }

        @Override
        public void onBind(int position, OnItemClickListener listener) {
            TransactionCategory item = getItem(position);
            if (item == null) return;

            datetime.setText(formatDate(item.datetime));
            datetime.setTextColor(item.categoryColor);

            value.setText(formatValue(getValueByCurrencyCode(item, currencyCode)));
            value.setTextColor(item.categoryColor);

            currency.setText(currencyCode);
            currency.setTextColor(item.categoryColor);

            category.setText(item.categoryName);
            category.setTextColor(item.categoryColor);

            itemView.setOnClickListener(view -> {
                listener.onItemClick(position, item);
            });
        }

        private String formatDate(Date date) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return date != null ? df.format(date) : "";
        }

        private String formatValue(Float value) {
            DecimalFormat df = new DecimalFormat("###,###,###,###.##");
            return (value != null ? "$" + df.format(value) : "");
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
    }
}
