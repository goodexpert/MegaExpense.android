package com.example.apps.mega.ui.transaction;

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
import com.example.apps.mega.entity.TransactionCategory;
import com.example.apps.mega.ui.base.BaseFragment;
import com.example.apps.mega.ui.base.OnItemClickListener;
import com.example.apps.mega.viewmodel.TransactionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TransactionFragment extends BaseFragment implements OnItemClickListener<TransactionCategory> {
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private TransactionViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TransactionAdapter(getActivity(), this);

        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(TransactionViewModel.class);
        viewModel.getTransactionsOrderByDesc().observe(this, transactions -> {
            adapter.setItems(transactions);
            updateView(getView());
        });
        viewModel.getCurrencyLiveData().observe(this, currencyCode -> {
            adapter.setCurrencyCode(currencyCode);
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
            findNavController().navigate(R.id.nav_new_transaction);
        });
    }

    @Override
    public void onItemClick(int position, TransactionCategory item) {
        Bundle args = new Bundle();
        args.putSerializable("transaction", item);

        findNavController().navigate(R.id.nav_edit_transaction, args);
    }

    private void updateView(View view) {
        View noData = view.findViewById(R.id.no_data);
        noData.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }
}