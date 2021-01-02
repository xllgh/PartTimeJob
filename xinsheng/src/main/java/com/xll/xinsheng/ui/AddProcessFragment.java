package com.xll.xinsheng.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.xinsheng.R;

public class AddProcessFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.add_process, rootKey);
    }
}
