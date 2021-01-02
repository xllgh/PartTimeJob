package com.xll.xinsheng.bean;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

public class OrderDetailItem  extends BaseObservable {

    //bx 报销

    @SerializedName("bx_dx")
    private String bxDx;

    @SerializedName("bx_project")
    private String bxProject;

    @SerializedName("fees")
    private String fee;

    @SerializedName("bx_date")
    private String bxDate;

    private String remark;

    @SerializedName("bx_dx_desc")
    private String bxDxDesc;

    @SerializedName("bx_user")
    private String bxUser;

    @SerializedName("fp_count")
    private  int fpCount;

    @SerializedName("bx_xx")
    private String bxXx;

    @SerializedName("bx_id")
    private String bxId;

    @SerializedName("id")
    private String id;

    @SerializedName("bx_xx_desc")
    private String bxXxDesc;

    @SerializedName("bx_fee")
    private String bxFee;


    private String invoiceType;

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getBxDx() {
        return bxDx;
    }

    public void setBxDx(String bxDx) {
        this.bxDx = bxDx;
    }

    public String getBxProject() {
        return bxProject;
    }

    public void setBxProject(String bxProject) {
        this.bxProject = bxProject;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBxDate() {
        return bxDate;
    }

    public void setBxDate(String bxDate) {
        this.bxDate = bxDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBxDxDesc() {
        return bxDxDesc;
    }

    public void setBxDxDesc(String bxDxDesc) {
        this.bxDxDesc = bxDxDesc;
    }

    public String getBxUser() {
        return bxUser;
    }

    public void setBxUser(String bxUser) {
        this.bxUser = bxUser;
    }

    public int getFpCount() {
        return fpCount;
    }

    public void setFpCount(int fpCount) {
        this.fpCount = fpCount;
    }

    public String getBxXx() {
        return bxXx;
    }

    public void setBxXx(String bxXx) {
        this.bxXx = bxXx;
    }

    public String getBxId() {
        return bxId;
    }

    public void setBxId(String bxId) {
        this.bxId = bxId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBxXxDesc() {
        return bxXxDesc;
    }

    public void setBxXxDesc(String bxXxDesc) {
        this.bxXxDesc = bxXxDesc;
    }

    public String getBxFee() {
        return bxFee;
    }

    public void setBxFee(String bxFee) {
        this.bxFee = bxFee;
    }
}
