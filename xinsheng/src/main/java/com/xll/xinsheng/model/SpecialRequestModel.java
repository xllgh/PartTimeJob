package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class SpecialRequestModel extends BaseObservable {
    private String[] projectEntries;

    private String[] requestEntries;

    private String reimburseReason;

    private String uploadFileName;

    private String ticketId;

    private String specialFee;



    @Bindable
    public String[] getProjectEntries() {
        return projectEntries;
    }



    public SpecialRequestModel setProjectEntries(String[] projectEntries) {
        this.projectEntries = projectEntries;
        notifyPropertyChanged(BR.processEntries);
        return  this;
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
    public String getReimburseReason() {
        return reimburseReason;
    }

    public void setReimburseReason(String reimburseReason) {
        this.reimburseReason = reimburseReason;
        notifyPropertyChanged(BR.reimburseReason);
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
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
        notifyPropertyChanged(BR.ticketId);
    }

    @Bindable
    public String getSpecialFee() {
        return specialFee;
    }

    public void setSpecialFee(String specialFee) {
        this.specialFee = specialFee;
    }
}
