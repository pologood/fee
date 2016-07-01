package com.sogou.pay.fee.model.blueplus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sogou.pay.fee.model.Operator;

/**
 * Created by nahongxu on 2016/6/24.
 */
public enum  BpOperator {

    TELECOM(1,"001"),UNICOM(2,"010"),MOBILE(3,"100");
    private int value;
    private String code;
    BpOperator(int value,String code){
        this.value=value;
        this.code=code;
    }

    private static BiMap<BpOperator, Operator> BP_OP_MAPPING_DICT = HashBiMap.create();

    static {
        BP_OP_MAPPING_DICT.put(TELECOM,Operator.CHINA_TELECOM);
        BP_OP_MAPPING_DICT.put(UNICOM,Operator.CHINA_UNICOM);
        BP_OP_MAPPING_DICT.put(MOBILE,Operator.CHINA_MOBILE);
    }


    public static Operator convFromBpOperator(BpOperator bpOperator){
       return BP_OP_MAPPING_DICT.get(bpOperator);
    }

    public static BpOperator convToBpOperator(Operator feeOperator){
        return BP_OP_MAPPING_DICT.inverse().get(feeOperator);
    }

    public static BpOperator getBpOperatorByCode(String code){
        for(BpOperator bpOperator: BpOperator.values()){
            if(bpOperator.getCode().equals(code)){
                return bpOperator;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
