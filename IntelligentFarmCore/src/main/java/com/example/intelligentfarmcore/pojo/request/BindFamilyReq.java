package com.example.intelligentfarmcore.pojo.request;

import java.util.Set;

public class BindFamilyReq {
    private Long elderId;
    private Set<Long> familyIds;

    public Long getElderId() {
        return elderId;
    }

    public void setElderId(Long elderId) {
        this.elderId = elderId;
    }

    public Set<Long> getFamilyIds() {
        return familyIds;
    }

    public void setFamilyIds(Set<Long> familyIds) {
        this.familyIds = familyIds;
    }
}
