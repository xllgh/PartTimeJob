package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class PendingDealModel extends BaseObservable {

    private String comment;

    @Bindable
    public String getComment() {
        return comment;
    }

    @Bindable
    public void setComment(String comment) {
        this.comment = comment;
    }
}
