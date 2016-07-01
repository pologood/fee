package com.sogou.pay.fee.model.blueplus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sogou.pay.fee.model.FeeType;

/**
 * Created by nahongxu on 2016/6/24.
 */
public enum  BpFeeType {

    ALL(-1),PHONE(1),FLOW(2);
    private int value;
    BpFeeType(int value){
        this.value=value;
    }

    private static BiMap<BpFeeType, FeeType> BP_PROD_TYPE_MAPPING_DICT = HashBiMap.create();

    static {
        BP_PROD_TYPE_MAPPING_DICT.put(ALL, FeeType.ALL);
        BP_PROD_TYPE_MAPPING_DICT.put(PHONE, FeeType.PHONE);
        BP_PROD_TYPE_MAPPING_DICT.put(FLOW, FeeType.FLOW);
    }

    public static FeeType convFromBpFeeType(BpFeeType bpFeeType){
        return BP_PROD_TYPE_MAPPING_DICT.get(bpFeeType);
    }

    public static BpFeeType getTypeByValue(int value){
        for(BpFeeType bpFeeType: BpFeeType.values()){
            if(bpFeeType.getValue()==value){
                return bpFeeType;
            }
        }
        return null;
    }

    public static BpFeeType convToBpFeeType(FeeType feeType){
        return BP_PROD_TYPE_MAPPING_DICT.inverse().get(feeType);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}