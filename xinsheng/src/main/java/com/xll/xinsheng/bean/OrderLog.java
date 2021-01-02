package com.xll.xinsheng.bean;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

public class OrderLog extends BaseObservable {

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("do_type")
    private String doType;

    @SerializedName("name")
    private String name;

    @SerializedName("order_desc")
    private String orderDesc;


    private int imageRes;

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDoType() {
        return doType;
    }

    public void setDoType(String doType) {
        this.doType = doType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }
}
