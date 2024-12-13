package com.zbkj.common.enums;

import java.math.BigDecimal;

/**
 * GiftPropertyEnum
 * @author wtj
 * @date 2024/11/13
 */
public enum GiftPropertyEnum {
    GIFT_PROPERTY_GB(1, "贵宾", new BigDecimal(699), true),

    GIFT_PROPERTY_JZ(2, "金尊", new BigDecimal(999), true),

    GIFT_PROPERTY_ZZ(3, "至尊", new BigDecimal(1999), true),

    GIFT_PROPERTY_SH(4, "奢华", new BigDecimal(2999), false);

    private Integer type;
    private String name;
    private BigDecimal value;
    private Boolean enable;

    GiftPropertyEnum(Integer type, String name, BigDecimal value, Boolean enable) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.enable = enable;
    }

    public static GiftPropertyEnum getGiftPropertyByType(Integer type) {
        switch (type) {
            case 1:
                return GiftPropertyEnum.GIFT_PROPERTY_GB;
            case 2:
                return GiftPropertyEnum.GIFT_PROPERTY_JZ;
            case 3:
                return GiftPropertyEnum.GIFT_PROPERTY_ZZ;
            case 4:
                return GiftPropertyEnum.GIFT_PROPERTY_SH;
            default:
                return null;
        }
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
