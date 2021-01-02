package com.xll.xinsheng.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class LoginModel extends BaseObservable {

    private String userName;

    private String password;

    private boolean isCheck;

    @Bindable
    public boolean getCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    @Bindable
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    private boolean loading = false;

    @Bindable
    public boolean isEnable() {
        return enable;
    }

    @Bindable
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private boolean enable = true;

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
