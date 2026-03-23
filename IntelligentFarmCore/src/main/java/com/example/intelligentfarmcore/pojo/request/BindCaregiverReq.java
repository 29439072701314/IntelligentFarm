package com.example.intelligentfarmcore.pojo.request;

import java.util.Set;

public class BindCaregiverReq {
    private Long elderId;
    private Set<Long> caregiverIds;

    public Long getElderId() {
        return elderId;
    }

    public void setElderId(Long elderId) {
        this.elderId = elderId;
    }

    public Set<Long> getCaregiverIds() {
        return caregiverIds;
    }

    public void setCaregiverIds(Set<Long> caregiverIds) {
        this.caregiverIds = caregiverIds;
    }
}
