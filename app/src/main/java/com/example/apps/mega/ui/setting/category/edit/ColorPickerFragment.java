package com.example.apps.mega.ui.setting.category.edit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.apps.mega.R;

public class ColorPickerFragment extends DialogFragment {
    private OnChangedColor onChangedColor;
    private ColorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new ColorAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_color_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GridView gridView = view.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            if (this.onChangedColor != null) {
                this.onChangedColor.onChangedColor((Integer) adapter.getItem(position));
                this.dismiss();
            }
        });

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            this.dismiss();
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            this.dismiss();
        });
    }

    public void setOnChangedColor(OnChangedColor onChangedColor) {
        this.onChangedColor = onChangedColor;
    }

    public interface OnChangedColor {
        void onChangedColor(int color);
    }

    class ColorAdapter extends BaseAdapter {
        private final Activity activity;
        private final LayoutInflater layoutInflater;

        public ColorAdapter(Activity activity) {
            this.activity = activity;
            this.layoutInflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            switch (position) {
                case 0:
                    return Color.BLACK;
                case 1:
                    return Color.DKGRAY;
                case 2:
                    return Color.GRAY;
                case 3:
                    return Color.LTGRAY;
                case 4:
                    return Color.RED;
                case 5:
                    return Color.GREEN;
                case 6:
                    return Color.BLUE;
                case 7:
                    return Color.YELLOW;
                case 8:
                    return Color.CYAN;
                case 9:
                    return Color.MAGENTA;
            }
            return Color.BLACK;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.view_grid_item_color, parent, false);
            view.setBackgroundColor((Integer) getItem(position));
            return view;
        }
    }
}
