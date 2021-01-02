package com.xll.xinsheng.bean;

import java.io.Serializable;

public class ShiroToken implements Serializable {

    private String principal;

    private Object password;

    private String userName;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
