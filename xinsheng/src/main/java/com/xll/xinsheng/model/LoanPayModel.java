package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class LoanPayModel extends BaseObservable {

    private String requestMoney;

    private String bankName;

    private String bankAccount;

    private String bankNetwork;

    private String requestReason;

    private String requestMoneyBig;

    private String[] projectEntries;

    private String[] requestEntries;

    private String[] processEntries;

    private String[] bankEntries;

    private String ticketId;

    private String userName;

    private String uploadFileName;

    private int processTypePos;

    public int getProcessTypePos() {
        return processTypePos;
    }

    public void setProcessTypePos(int processTypePos) {
        this.processTypePos = processTypePos;
    }

    @Bindable
    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
        notifyPropertyChanged(BR.uploadFileName);
    }

    @Bindable
    public String getRequestMoney() {
        return requestMoney;
    }

    public void setRequestMoney(String requestMoney) {
        this.requestMoney = requestMoney;
        notifyPropertyChanged(BR.requestMoney);
    }

    @Bindable
    public String[] getProjectEntries() {
        return projectEntries;
    }

    public void setProjectEntries(String[] projectEntries) {
        this.projectEntries = projectEntries;
        notifyPropertyChanged(BR.projectEntries);
    }

    @Bindable
    public String[] getRequestEntries() {
        return requestEntries;
    }

    public void setRequestEntries(String[] requestEntries) {
        this.requestEntries = requestEntries;
        notifyPropertyChanged(BR.requestEntries);
    }

    @Bindable
    public String[] getProcessEntries() {
        return processEntries;
    }

    public void setProcessEntries(String[] processEntries) {
        this.processEntries = processEntries;
        notifyPropertyChanged(BR.processEntries);
    }

    @Bindable
    public String[] getBankEntries() {
        return bankEntries;
    }

    public void setBankEntries(String[] bankEntries) {
        this.bankEntries = bankEntries;
        notifyPropertyChanged(BR.bankEntries);
    }

    @Bindable
    public String getRequestMoneyBig() {
        return requestMoneyBig;
    }

    public void setRequestMoneyBig(String requestMoneyBig) {
        this.requestMoneyBig = requestMoneyBig;
        notifyPropertyChanged(BR.requestMoneyBig);
    }

    @Bindable
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
        notifyPropertyChanged(BR.ticketId);
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
        notifyPropertyChanged(BR.bankName);
    }

    @Bindable
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        notifyPropertyChanged(BR.bankAccount);
    }

    @Bindable
    public String getBankNetwork() {
        return bankNetwork;
    }

    public void setBankNetwork(String bankNetwork) {
        this.bankNetwork = bankNetwork;
        notifyPropertyChanged(BR.bankNetwork);
    }

    @Bindable
    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
        notifyPropertyChanged(BR.requestReason);
    }
}
