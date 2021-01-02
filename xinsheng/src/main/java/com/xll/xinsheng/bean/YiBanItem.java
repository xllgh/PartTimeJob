package com.xll.xinsheng.bean;

import com.google.gson.annotations.SerializedName;

public class YiBanItem {

    private String name;

    @SerializedName("process_type")
    private String processType;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("order_name")
    private String orderName;

    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
