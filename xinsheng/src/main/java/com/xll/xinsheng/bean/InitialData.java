package com.xll.xinsheng.bean;

import java.io.Serializable;
import java.util.List;

public class InitialData implements Serializable {

    private List<Project> projectList;

    private List<UserInfo> userInfoList;

    private List<BankType> bankList;

    private Object pageTitleBread;

    private List<ItemType> itemTypeList;

    private String userid;

    private String username;

    private String usercaption;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<BankType> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankType> bankList) {
        this.bankList = bankList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }


    public Object getPageTitleBread() {
        return pageTitleBread;
    }

    public void setPageTitleBread(Object pageTitleBread) {
        this.pageTitleBread = pageTitleBread;
    }

    public List<ItemType> getItemTypeList() {
        return itemTypeList;
    }

    public void setItemTypeList(List<ItemType> itemTypeList) {
        this.itemTypeList = itemTypeList;
    }

    public String getUsercaption() {
        return usercaption;
    }

    public void setUsercaption(String usercaption) {
        this.usercaption = usercaption;
    }


}
