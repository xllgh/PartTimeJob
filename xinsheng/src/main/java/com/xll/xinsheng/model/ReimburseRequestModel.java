package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ReimburseRequestModel extends BaseObservable {
    private String[] projectEntries;

    private String[] requestEntries;

    private String reimburseReason;

    private String uploadFileName;

    private String ticketId;



    @Bindable
    public String[] getProjectEntries() {
        return projectEntries;
    }



    public ReimburseRequestModel setProjectEntries(String[] projectEntries) {
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
}
