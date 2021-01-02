package com.xll.xinsheng.bean;

import java.io.Serializable;

public class Session implements Serializable {

    private String lastAccessTime;

    private String host;

    private String id;

    private Object attributeKeys;

    private String startTimestamp;

    private long timeout;

    public Object getAttributeKeys() {
        return attributeKeys;
    }

    public void setAttributeKeys(Object attributeKeys) {
        this.attributeKeys = attributeKeys;
    }


    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(String lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
