package com.example.apps.mega.ui.setting.category;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apps.mega.R;
import com.example.apps.mega.entity.Category;
import com.example.apps.mega.ui.base.BaseViewHolder;
import com.example.apps.mega.ui.base.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<Category> items = new ArrayList<>();
    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private final OnItemClickListener<Category> onItemClickListener;

    public CategoryAdapter(Activity activity, OnItemClickListener<Category> onItemClickListener) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.view_list_item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Category getItem(int position) {
        if (position < 0 || position >= getItemCount()) return null;
        return items.get(position);
    }

    public void setItems(List<Category> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends BaseViewHolder {
        private final TextView name;
        private final TextView color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            color = itemView.findViewById(R.id.color);
        }

        @Override
        public void onBind(int position, OnItemClickListener listener) {
            final Category category = getItem(position);
            if (category == null) return;

            name.setText(category.categoryName);
            color.setBackgroundColor(category.categoryColor);
            itemView.setOnClickListener(view -> {
                listener.onItemClick(position, category);
            });
        }
    }
}
