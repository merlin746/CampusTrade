package com.example.schooltrade.vo;

import java.util.List;

/**
 * 分页响应封装
 */
public class PageVO<T> {

    private List<T> records;    // 数据列表
    private Long total;         // 总记录数
    private Integer page;       // 当前页码
    private Integer size;       // 每页大小
    private Integer pages;      // 总页数

    public PageVO() {}

    public PageVO(List<T> records, Long total, Integer page, Integer size, Integer pages) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
        this.pages = pages;
    }

    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records; }

    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

}