package com.example.apps.mega.ui.base;

import com.example.apps.mega.entity.Category;

public interface OnItemClickListener<T extends Object> {
    void onItemClick(int position, T item);
}
