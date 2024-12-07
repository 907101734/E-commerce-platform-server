package com.zbkj.common.enums;

/**
 * RegionEnum
 * @author wtj
 * <p>
 * 所属区域，所属区域，8-保单区 9-生活区 10-门店区
 * @date 2024/11/13
 */
public enum RegionEnum {
    REGION_BDQ("报单区", 8),
    REGION_SHQ("生活区", 9),
    REGION_MDQ("门店区", 10),
    REGION_TGQ("团购区", 11);
    private String name;

    private Integer type;

    RegionEnum(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
