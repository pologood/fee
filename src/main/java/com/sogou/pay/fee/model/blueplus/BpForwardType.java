package com.sogou.pay.fee.model.blueplus;

/**
 * Created by nahongxu on 2016/6/26.
 */
public enum BpForwardType {
    NO_FORWARD(0), PAY_FORWARD(1);
    private int value;

    BpForwardType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
