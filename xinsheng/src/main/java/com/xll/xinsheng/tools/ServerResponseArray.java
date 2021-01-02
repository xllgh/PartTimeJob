package com.xll.xinsheng.tools;

import java.io.Serializable;
import java.util.List;

public class ServerResponseArray<T> implements Serializable {

    public static final int FAILED = 0;

    public static final int SUCCESS = 1;

    private List<T> rows;

    private String code;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private Boolean success;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
