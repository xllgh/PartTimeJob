package com.xll.xinsheng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YiBanInfo {


    @SerializedName("total")
    private int total;

    private boolean sucess;

    @SerializedName("rows")
    private List<YiBanItem> yiBanItemList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    public List<YiBanItem> getYiBanItemList() {
        return yiBanItemList;
    }

    public void setYiBanItemList(List<YiBanItem> yiBanItemList) {
        this.yiBanItemList = yiBanItemList;
    }

    @Override
    public String toString() {
        return "YiBanInfo{" +
                "total=" + total +
                ", sucess=" + sucess +
                ", yiBanItemList=" + yiBanItemList +
                '}';
    }
}
