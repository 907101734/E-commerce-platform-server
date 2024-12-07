package com.zbkj.common.enums;

/**
 * GiftPropertyEnum
 * @author wtj
 * @date 2024/11/13
 */
public enum GiftPropertyEnum {
    GIFT_PROPERTY_GB(1, "贵宾", 699, true),

    GIFT_PROPERTY_JZ(2, "金尊", 999, true),

    GIFT_PROPERTY_ZZ(3, "至尊", 1999, true),

    GIFT_PROPERTY_SH(4, "奢华", 2999, false);

    private Integer type;
    private String name;
    private Integer value;
    private Boolean enable;

    GiftPropertyEnum(Integer type, String name, Integer value, Boolean enable) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.enable = enable;
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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
