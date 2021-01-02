package com.xll.xinsheng.bean;

import androidx.databinding.BaseObservable;

import java.util.List;

public class PendingDetailInfo extends BaseObservable  {

    private List<OrderDetailItem> detailList;

    private List<OrderItem> itemList;

    private List<OrderLog> orderLogList;

    private List<PaymentItem> paymentList;

    private List<FileInfo> fileList;

    private List<BankInfo> bankList;

    private List<ItemType> itemTypeList;

    public List<ItemType> getItemTypeList() {
        return itemTypeList;
    }

    public void setItemTypeList(List<ItemType> itemTypeList) {
        this.itemTypeList = itemTypeList;
    }

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<BankInfo> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankInfo> bankList) {
        this.bankList = bankList;
    }

    public List<OrderDetailItem> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<OrderDetailItem> detailList) {
        this.detailList = detailList;
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    public List<OrderLog> getOrderLogList() {
        return orderLogList;
    }

    public void setOrderLogList(List<OrderLog> orderLogList) {
        this.orderLogList = orderLogList;
    }

    public List<PaymentItem> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentItem> paymentList) {
        this.paymentList = paymentList;
    }

    public List<FileInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }
}
