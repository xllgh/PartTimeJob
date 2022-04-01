package com.xll.xinsheng.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.xll.xinsheng.tools.MyApplication;

public class DoneProcess extends BaseObservable implements Parcelable {
    private String name;

    private String processType;

    private String orderId;

    private String orderName;

    private String orderStatus;

    private boolean bottomVisibility = false;

    private String bottomText;

    private boolean selected = true;

    private int imgRes;

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    private String tag;

    public String getTag() {
        if (!TextUtils.isEmpty(name) && name.length() > 1) {
            return name.substring(1);
        } else {
            return MyApplication.getMyApplication().getString(android.R.string.unknownName);
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public DoneProcess() {
        super();
    }

    protected DoneProcess(Parcel in) {
        name = in.readString();
        processType = in.readString();
        orderId = in.readString();
        orderName = in.readString();
        orderStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(processType);
        dest.writeString(orderId);
        dest.writeString(orderName);
        dest.writeString(orderStatus);

    }

    public static final Creator<DoneProcess> CREATOR = new Creator<DoneProcess>() {
        @Override
        public DoneProcess createFromParcel(Parcel in) {
            return new DoneProcess(in);
        }

        @Override
        public DoneProcess[] newArray(int size) {
            return new DoneProcess[size];
        }
    };



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


    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public boolean isBottomVisibility() {
        return bottomVisibility;
    }

    public void setBottomVisibility(boolean bottomVisibility) {
        this.bottomVisibility = bottomVisibility;
    }
}
