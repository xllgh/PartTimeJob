package com.xll.xinsheng.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Project implements Serializable {

    @SerializedName("project_type")
    private String projectType;

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("plan_buy_fee")
    private float planBuyFee;

    @SerializedName("plan_business_cost")
    private float planBusinessCost;

    @SerializedName("project_name")
    private String projectName;

    @SerializedName("project_type1")
    private String projectType1;

    @SerializedName("plan_daily_cost")
    private float planDailyCost;

    @SerializedName("plan_quipmen_leasing_cost")
    private float planQuipmenLeasingCost;

    @SerializedName("dept_no")
    private String deptNo;

    @SerializedName("project_id")
    private String projectId;

    @SerializedName("plan_human_cost")
    private float planHumanCost;

    @SerializedName("plan_all_fee")
    private float planAllFee;

    @SerializedName("create_user")
    private String createUser;

    @SerializedName("status")
    private String status;

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getPlanBuyFee() {
        return planBuyFee;
    }

    public void setPlanBuyFee(float planBuyFee) {
        this.planBuyFee = planBuyFee;
    }

    public float getPlanBusinessCost() {
        return planBusinessCost;
    }

    public void setPlanBusinessCost(float planBusinessCost) {
        this.planBusinessCost = planBusinessCost;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType1() {
        return projectType1;
    }

    public void setProjectType1(String projectType1) {
        this.projectType1 = projectType1;
    }

    public float getPlanDailyCost() {
        return planDailyCost;
    }

    public void setPlanDailyCost(float planDailyCost) {
        this.planDailyCost = planDailyCost;
    }

    public float getPlanQuipmenLeasingCost() {
        return planQuipmenLeasingCost;
    }

    public void setPlanQuipmenLeasingCost(float planQuipmenLeasingCost) {
        this.planQuipmenLeasingCost = planQuipmenLeasingCost;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public float getPlanHumanCost() {
        return planHumanCost;
    }

    public void setPlanHumanCost(float planHumanCost) {
        this.planHumanCost = planHumanCost;
    }

    public float getPlanAllFee() {
        return planAllFee;
    }

    public void setPlanAllFee(float planAllFee) {
        this.planAllFee = planAllFee;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
