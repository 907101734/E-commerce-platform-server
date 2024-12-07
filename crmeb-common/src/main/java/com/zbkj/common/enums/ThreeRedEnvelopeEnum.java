package com.zbkj.common.enums;

/**
 * RedEnvelopeEnum
 * @author wtj
 * @date 2024/11/16
 */
public enum ThreeRedEnvelopeEnum {

    THREE_699(3.9, 4.1),
    THREE_999(5.2, 5.4),
    THREE_1999(10.7, 10.9);
    private Double minNumber;
    private Double maxNumber;

    ThreeRedEnvelopeEnum(Double minNumber, Double maxNumber) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
    }

    public static ThreeRedEnvelopeEnum get(Integer type) {
        if (type.equals(1)) {
            return ThreeRedEnvelopeEnum.THREE_699;
        } else if (type.equals(2)) {
            return ThreeRedEnvelopeEnum.THREE_999;
        } else if (type.equals(3)) {
            return ThreeRedEnvelopeEnum.THREE_1999;
        }
        return null;
    }

    public Double getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(Double minNumber) {
        this.minNumber = minNumber;
    }

    public Double getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Double maxNumber) {
        this.maxNumber = maxNumber;
    }
}
