package com.xll.xinsheng.model;

import android.view.View;

import androidx.databinding.ObservableField;

public class WorkViewModel {

    public ObservableField<String> text = new ObservableField<>();

    public ObservableField<Integer> imageRes = new ObservableField<>();

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private View.OnClickListener listener;



    public WorkViewModel(String strText, int imgRes) {
        text.set(strText);
        imageRes.set(imgRes);
    }

    public WorkViewModel(String strText, int imgRes, View.OnClickListener listener) {
        text.set(strText);
        imageRes.set(imgRes);
        this.listener = listener;
    }



}
