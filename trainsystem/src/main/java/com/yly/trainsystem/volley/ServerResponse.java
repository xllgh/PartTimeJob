package com.yly.trainsystem.volley;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {

    public static final int FAILED = 0;

    public static final int SUCCESS = 1;

    private T data;

    private int code;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
