package com.yly.trainsystem.volley;

import java.io.Serializable;
import java.util.List;

public class ServerResponseArray<T> implements Serializable {

    public static final int FAILED = 0;

    public static final int SUCCESS = 1;

    private List<T> data;

    private int code;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
