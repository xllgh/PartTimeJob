package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.xinsheng.BR;
import com.xll.xinsheng.bean.InvoiceType;
import com.xll.xinsheng.bean.ItemType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemReimburseFee extends BaseObservable {

    private String reimburseFee;

    private String reimburseLimitFee;

    private String reimburseDate;

    private List<ItemType> feeTypeList;

    private List<InvoiceType.InvoiceItem> invoiceItemList;

    private String[] itemTypeName;

    private String[] invoiceTypeName;

    private int invoiceTypePos;

    private int itemTypePos;

    private String fpCount;

    private String remarks;

    //发票科目
    private String invoiceSubject;

    private InvoiceType invoiceType;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Bindable
    public String getReimburseFee() {
        return reimburseFee;
    }

    public void setReimburseFee(String reimburseFee) {
        this.reimburseFee = reimburseFee;
        notifyPropertyChanged(BR.reimburseFee);
    }

    public String[] getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String[] itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    @Bindable
    public String getReimburseDate() {
        return reimburseDate;
    }

    public void setReimburseDate(String reimburseDate) {
        this.reimburseDate = reimburseDate;
        notifyPropertyChanged(BR.reimburseDate);
    }

    @Bindable
    public List<ItemType> getFeeTypeList() {
        return feeTypeList;
    }

    public void setFeeTypeList(List<ItemType> feeTypeList) {
        this.feeTypeList = feeTypeList;
        notifyPropertyChanged(BR.feeTypeList);
    }

    @Bindable
    public String[] getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String[] invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
        notifyPropertyChanged(BR.invoiceTypeName);
    }

    public int getInvoiceTypePos() {
        return invoiceTypePos;
    }

    public void setInvoiceTypePos(int invoiceTypePos) {
        this.invoiceTypePos = invoiceTypePos;
    }

    public int getItemTypePos() {
        return itemTypePos;
    }

    public void setItemTypePos(int itemTypePos) {
        this.itemTypePos = itemTypePos;
    }

    public List<InvoiceType.InvoiceItem> getInvoiceItemList() {
        return invoiceItemList;
    }

    public void setInvoiceItemList(List<InvoiceType.InvoiceItem> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
    }

    @Bindable
    public String getFpCount() {
        return fpCount;
    }

    public void setFpCount(String fpCount) {
        this.fpCount = fpCount;
        notifyPropertyChanged(BR.fpCount);
    }

    @Bindable
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
        notifyPropertyChanged(BR.remarks);
    }

    @Bindable
    public String getReimburseLimitFee() {
        return reimburseLimitFee;
    }

    public void setReimburseLimitFee(String reimburseLimitFee) {
        this.reimburseLimitFee = reimburseLimitFee;
        notifyPropertyChanged(BR.reimburseLimitFee);
    }

    public String getInvoiceSubject() {
        return invoiceSubject;
    }

    public void setInvoiceSubject(String invoiceSubject) {
        this.invoiceSubject = invoiceSubject;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    @Override
    public String toString() {
        return "ItemReimburseFee{" +
                "reimburseFee='" + reimburseFee + '\'' +
                ", reimburseLimitFee='" + reimburseLimitFee + '\'' +
                ", reimburseDate='" + reimburseDate + '\'' +
                ", feeTypeList=" + feeTypeList +
                ", invoiceItemList=" + invoiceItemList +
                ", itemTypeName=" + Arrays.toString(itemTypeName) +
                ", invoiceTypeName=" + Arrays.toString(invoiceTypeName) +
                ", invoiceTypePos=" + invoiceTypePos +
                ", itemTypePos=" + itemTypePos +
                ", fpCount='" + fpCount + '\'' +
                ", remarks='" + remarks + '\'' +
                ", invoiceSubject='" + invoiceSubject + '\'' +
                ", invoiceType=" + invoiceType +
                ", position=" + position +
                '}';
    }
}
