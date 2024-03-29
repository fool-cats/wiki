package com.foolcats.wiki.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

// 分页参数
public class PageReq {
    @NotNull(message="页码不能为空")
    private int page;
    @NotNull(message = "每页条数不能为空")
    @Max(value= 1000,message="每页条数不能超过1000")
    private int size;

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}