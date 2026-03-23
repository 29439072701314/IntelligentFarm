package com.example.intelligentfarmcore.pojo.request;

import com.example.intelligentfarmcore.pojo.model.SortDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageReq {
    // 页码
    private Integer pageNumber;
    // 每页数量
    private Integer pageSize;
    // 查询条件
    private Map<String, Object> condition = new HashMap<>();
    // 排序字段
    private List<SortDTO> sort;

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

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public List<SortDTO> getSort() {
        return sort;
    }

    public void setSort(List<SortDTO> sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", condition=" + condition +
                ", sort=" + sort +
                '}';
    }
}
