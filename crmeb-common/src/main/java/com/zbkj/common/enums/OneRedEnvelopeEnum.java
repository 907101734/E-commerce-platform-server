package com.zbkj.common.enums;

/**
 * RedEnvelopeEnum
 * @author wtj
 * @date 2024/11/16
 */
public enum OneRedEnvelopeEnum {

    ONE_699(3.0, 3.2),
    ONE_999(4.1, 4.3),
    ONE_1999(8.6, 8.8);
    private Double minNumber;
    private Double maxNumber;

    OneRedEnvelopeEnum(Double minNumber, Double maxNumber) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
    }

    public static OneRedEnvelopeEnum get(Integer type) {
        if (type.equals(1)) {
            return OneRedEnvelopeEnum.ONE_699;
        } else if (type.equals(2)) {
            return OneRedEnvelopeEnum.ONE_999;
        } else if (type.equals(3)) {
            return OneRedEnvelopeEnum.ONE_1999;
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
