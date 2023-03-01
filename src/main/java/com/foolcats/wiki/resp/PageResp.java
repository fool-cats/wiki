package com.foolcats.wiki.resp;

import java.util.List;

public class PageResp<T> {
    private long total;
    private List<T> list;

    public long getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}