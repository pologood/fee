package com.sogou.pay.fee.model;

/**
 * Created by nahongxu on 2016/6/27.
 */
public enum  QueryOrderType {
    BY_PHONE(1),BY_USERID(2);
    private int value;
    QueryOrderType(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
