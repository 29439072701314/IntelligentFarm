package com.example.intelligentfarmcore.pojo.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageRes<T> {
    private Long total;          // 总数据条数
    private Integer totalPages;  // 总页数
    private Integer pageNumber;  // 当前页编号
    private Integer pageSize;    // 每页数据量
    private List<T> content;     // 当前页数据列表

    public PageRes(Long total, Integer totalPages, Integer pageNumber, Integer pageSize, List<T> content) {
        this.total = total;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber + 1;
        this.pageSize = pageSize;
        this.content = content;
    }

    public PageRes(Page page) {
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageNumber = page.getPageable().getPageNumber() + 1;
        this.pageSize = page.getPageable().getPageSize();
        this.content = page.getContent();
    }

    public PageRes(Page page, List<T> content) {
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageNumber = page.getPageable().getPageNumber() + 1;
        this.pageSize = page.getPageable().getPageSize();
        this.content = content;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
