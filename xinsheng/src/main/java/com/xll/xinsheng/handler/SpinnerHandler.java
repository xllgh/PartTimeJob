package com.xll.xinsheng.handler;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.databinding.InverseBindingListener;
import androidx.databinding.adapters.AdapterViewBindingAdapter;

public class SpinnerHandler {
    private static final String TAG = "SpinnerHandler";

    public void onItemSelect(AdapterView view, final AdapterViewBindingAdapter.OnItemSelected selected,
                              final AdapterViewBindingAdapter.OnNothingSelected nothingSelected, final InverseBindingListener attrChanged) {
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "position:" + position);
            }
        });


    }
}
