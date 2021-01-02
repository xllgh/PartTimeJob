package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class SearchModel extends BaseObservable {

    private String orderId;

    private String name;

    private String initiator;

    private String content;

    @Bindable
    private boolean searchVisible;

    public boolean getSearchVisible() {
        return searchVisible;
    }


    public void setSearchVisible(boolean searchVisible) {
        this.searchVisible = searchVisible;
    }

    public String getOrderId() {
        return orderId;
    }

    @Bindable
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Bindable
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getInitiator() {
        return initiator;
    }

    @Bindable
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getContent() {
        return content;
    }

    @Bindable
    public void setContent(String content) {
        this.content = content;
    }
}
