package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;

public class CompoText extends BaseObservable {

    public  String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
