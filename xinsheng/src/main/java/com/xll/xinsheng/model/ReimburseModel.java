package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ReimburseModel extends BaseObservable {

    @Bindable
    private String orderNo;

    private String userName;

    private String userDepartment;

    private String feeDepartment;

    private String reimburseLimit;

    private String selectProject;

    public String getSelectProject() {
        return selectProject;
    }

    @Bindable
    public void setSelectProject(String selectProject) {
        this.selectProject = selectProject;
    }

    private String feeType;

    public String getReimburseLimit() {
        return reimburseLimit;
    }

    public void setReimburseLimit(String reimburseLimit) {
        this.reimburseLimit = reimburseLimit;
    }

    private String[] projectList;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getFeeDepartment() {
        return feeDepartment;
    }

    public void setFeeDepartment(String feeDepartment) {
        this.feeDepartment = feeDepartment;
    }

    public String[] getProjectList() {
        return projectList;
    }

    public void setProjectList(String[] projectList) {
        this.projectList = projectList;
    }

    @Bindable
    public String getFeeType() {
        return feeType;
    }

    @Bindable
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
}
