package com.xll.xinsheng.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DaiBanItem implements Serializable {
    @SerializedName("create_time")
    private String createTime;

    @SerializedName("project_id")
    private String projectId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("name")
    private String name;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("process_type")
    private String processType;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("now_node")
    private String nowNode;

    @SerializedName("order_name")
    private String orderName;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getNowNode() {
        return nowNode;
    }

    public void setNowNode(String nowNode) {
        this.nowNode = nowNode;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
