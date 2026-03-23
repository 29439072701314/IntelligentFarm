package com.example.intelligentfarmcore.pojo.model;

public class SortDTO {
        // 排序字段
        private String field;
        // 排序方向
        private String order;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "SortDTO{" +
                "field='" + field + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
