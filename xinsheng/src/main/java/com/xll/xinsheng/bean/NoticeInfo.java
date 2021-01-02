package com.xll.xinsheng.bean;

import java.util.List;

public class NoticeInfo {

    private int totalRow;

    private int pageNumber;

    private boolean firstPage;

    private boolean lastPage;

    private int totalPage;

    private int pageSize;

    private List<NoticeItem> list;

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<NoticeItem> getList() {
        return list;
    }

    public void setList(List<NoticeItem> list) {
        this.list = list;
    }
}
