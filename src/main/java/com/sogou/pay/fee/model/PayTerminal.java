package com.sogou.pay.fee.model;

/**
 * Created by nahongxu on 2016/6/24.
 */
public enum PayTerminal {
    WAP(1),APP(2),PC(3),OTHERS(4);
    private int value;
    PayTerminal(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
