package com.zbkj.common.enums;

/**
 * RedEnvelopeEnum
 * @author wtj
 * @date 2024/11/16
 */
public enum TwoRedEnvelopeEnum {

    TWO_699(3.5, 3.8),
    TWO_999(4.7, 4.9),
    TWO_1999(9.8, 10.0);
    private Double minNumber;
    private Double maxNumber;

    TwoRedEnvelopeEnum(Double minNumber, Double maxNumber) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
    }

    public static TwoRedEnvelopeEnum get(Integer type) {
        if (type.equals(1)) {
            return TwoRedEnvelopeEnum.TWO_699;
        } else if (type.equals(2)) {
            return TwoRedEnvelopeEnum.TWO_999;
        } else if (type.equals(3)) {
            return TwoRedEnvelopeEnum.TWO_1999;
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
