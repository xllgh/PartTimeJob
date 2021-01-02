package com.xll.xinsheng.bean;

import java.util.List;

public class DaiBanInfo {

    private List<DaiBanItem> daiBanList;

    private String usercaption;

    private String userid;

    private String userName;

    public List<DaiBanItem> getDaiBanList() {
        return daiBanList;
    }

    public void setDaiBanList(List<DaiBanItem> daiBanList) {
        this.daiBanList = daiBanList;
    }

    public String getUsercaption() {
        return usercaption;
    }

    public void setUsercaption(String usercaption) {
        this.usercaption = usercaption;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
