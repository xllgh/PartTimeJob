package com.xll.xinsheng.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.xll.xinsheng.tools.MyApplication;

public class PendingProcess extends BaseObservable implements Parcelable {
    private String name;

    private String processType;

    private String orderId;

    private String orderName;

    private String orderStatus;

    private String orderCreator;

    private String orderCreateTime;

    private String orderType;

    private String orderLastNode;



    private String tag;

    public String getTag() {
        if (!TextUtils.isEmpty(orderCreator) && orderCreator.length() > 1) {
            return orderCreator.substring(1);
        } else {
            return MyApplication.getMyApplication().getString(android.R.string.unknownName);
        }
    }

    public void setTag(String tag) {
       this.tag = tag;
    }

    private int imgRes;

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getOrderLastNode() {
        return orderLastNode;
    }

    public void setOrderLastNode(String orderLastNode) {
        this.orderLastNode = orderLastNode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOrderCreator() {
        return orderCreator;
    }

    public void setOrderCreator(String orderCreator) {
        this.orderCreator = orderCreator;
    }

    private boolean selected = true;

    public boolean isSelected() {
        return selected;
    }

    @Bindable
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    @Bindable
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Bindable
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @Bindable
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(processType);
        dest.writeString(orderId);
        dest.writeString(orderName);
        dest.writeString(orderStatus);
        dest.writeString(orderCreator);
        dest.writeString(orderCreateTime);
        dest.writeString(orderType);
        dest.writeString(orderLastNode);

    }

    public  PendingProcess() {
        super();
    }

    private PendingProcess(Parcel in)
    {
        name = in.readString();
        processType = in.readString();
        orderId = in.readString();
        orderName = in.readString();
        orderStatus = in.readString();
        orderCreator = in.readString();
        orderCreateTime = in.readString();
        orderType = in.readString();
        orderLastNode = in.readString();
    }

    public static final Parcelable.Creator<PendingProcess> CREATOR = new Parcelable.Creator<PendingProcess>()
    {
        public PendingProcess createFromParcel(Parcel in)
        {
            return new PendingProcess(in);
        }

        public PendingProcess[] newArray(int size)
        {
            return new PendingProcess[size];
        }
    };
}
