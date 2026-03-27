package com.example.intelligentfarmcore.pojo.dto;

/**
 * Farm摘要数据传输对象，用于避免循环引用
 */
public class FarmSummaryDTO {
    // 农场ID
    private Long farmId;

    // 农场名称
    private String farmName;

    // 农场地址
    private String address;

    public FarmSummaryDTO() {
    }

    public FarmSummaryDTO(Long farmId, String farmName, String address) {
        this.farmId = farmId;
        this.farmName = farmName;
        this.address = address;
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
