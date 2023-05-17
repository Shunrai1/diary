package com.shunrai.note.po;

public class PageBean {
    private int page; // 第几页
    private int pageSize; // 每页记录数
    private int start;  // 起始页
    public PageBean() {
        // TODO Auto-generated constructor stub
    }
    public PageBean(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return (page - 1) * pageSize;//算出起始页 (2 - 1)  * 10;
    }
}
