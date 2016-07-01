package com.sogou.pay.fee.model;

/**
 * Created by nahongxu on 2016/6/24.
 */
public enum FeeType {
    PHONE(1), FLOW(2), ALL(3);
    private int value;

    FeeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}