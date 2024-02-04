package com.xll.xinsheng.model;

public class DelFileResponse {
    //{"total":0,"code":"","data":null,"success":true,"rows":null,"message":"删除成功"}

    private int total;

    private String code;

    private boolean success;

    private String message;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
