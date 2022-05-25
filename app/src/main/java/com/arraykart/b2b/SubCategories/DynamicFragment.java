package com.arraykart.b2b.SubCategories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraykart.b2b.R;


public class DynamicFragment extends Fragment {

    public static Fragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        TextView textView = view.findViewById(R.id.commonTextView);
        textView.setText(String.valueOf("Category :  " + getArguments().getInt("position")));
    }

}