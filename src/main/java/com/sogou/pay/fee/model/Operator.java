package com.sogou.pay.fee.model;

/**
 * Created by nahongxu on 2016/6/24.
 */
public enum  Operator {
    CHINA_MOBILE(1,"中国移动"), CHINA_UNICOM(2,"中国联通"),
    CHINA_TELECOM(3,"中国电信"),UNKNOWN(4,"未知");
    private int value;
    private String name;

    Operator(int value,String name) {
        this.value = value;
        this.name=name;
    }

//    public static String getOperatorNameByValue(int value){
//        for(Operator operator:Operator.values()){
//            if(value==operator.getValue()){
//                return operator.getName();
//            }
//        }
//
//        return null;
//    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

