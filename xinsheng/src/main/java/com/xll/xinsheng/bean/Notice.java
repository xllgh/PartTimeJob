package com.xll.xinsheng.bean;

public class Notice {

    private NoticeInfo NoticeList;

    private String usercaption;

    private String userid;

    private String username;

    public NoticeInfo getNoticeList() {
        return NoticeList;
    }

    public void setNoticeList(NoticeInfo noticeList) {
        NoticeList = noticeList;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
