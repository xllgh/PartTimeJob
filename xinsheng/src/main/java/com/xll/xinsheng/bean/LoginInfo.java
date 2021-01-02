package com.xll.xinsheng.bean;

import java.io.Serializable;

public class LoginInfo implements Serializable {

    private ShiroToken shiroToken;

    private String ctx;

    private Session session;

    private Object shiroFILTERED;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public ShiroToken getShiroToken() {
        return shiroToken;
    }

    public void setShiroToken(ShiroToken shiroToken) {
        this.shiroToken = shiroToken;
    }

    public String getCtx() {
        return ctx;
    }

    public void setCtx(String ctx) {
        this.ctx = ctx;
    }

    public Object getShiroFILTERED() {
        return shiroFILTERED;
    }

    public void setShiroFILTERED(Object shiroFILTERED) {
        this.shiroFILTERED = shiroFILTERED;
    }
}
