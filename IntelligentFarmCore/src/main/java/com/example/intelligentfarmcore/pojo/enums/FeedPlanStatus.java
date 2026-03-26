package com.example.intelligentfarmcore.pojo.enums;

public enum FeedPlanStatus {
    PENDING("待执行"),
    COMPLETED("已完成"),
    CANCELLED("取消");

    private final String value;

    FeedPlanStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 根据值获取枚举
    public static FeedPlanStatus fromValue(String value) {
        for (FeedPlanStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    // 获取下一个状态
    public FeedPlanStatus getNextStatus() {
        switch (this) {
            case PENDING:
                return COMPLETED;
            case COMPLETED:
                return CANCELLED;
            case CANCELLED:
                return PENDING;
            default:
                return this;
        }
    }
}
