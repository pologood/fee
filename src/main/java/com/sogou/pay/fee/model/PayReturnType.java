package com.sogou.pay.fee.model;

/**
 * Created by nahongxu on 2016/6/26.
 */
public enum  PayReturnType {
    STR(0),URL(1),HTML(2);
    private int value;
    PayReturnType(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
