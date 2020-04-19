package com.example.apps.mega.ui.setting.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apps.mega.R;
import com.example.apps.mega.entity.Category;
import com.example.apps.mega.ui.base.BaseFragment;
import com.example.apps.mega.ui.base.OnItemClickListener;
import com.example.apps.mega.viewmodel.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryListFragment extends BaseFragment implements OnItemClickListener<Category> {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private CategoryViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CategoryAdapter(getActivity(), this);

        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(CategoryViewModel.class);
        viewModel.getCategories().observe(this, categories -> {
            adapter.setItems(categories);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_divider_line));
        recyclerView.addItemDecoration(itemDecoration);

        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            findNavController().navigate(R.id.nav_new_category);
        });
    }

    @Override
    public void onItemClick(int position, Category item) {
        Bundle args = new Bundle();
        args.putSerializable("category", item);

        findNavController().navigate(R.id.nav_edit_category, args);
    }
}