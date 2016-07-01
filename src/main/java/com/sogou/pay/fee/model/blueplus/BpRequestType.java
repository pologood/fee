package com.sogou.pay.fee.model.blueplus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sogou.pay.fee.model.PayTerminal;

/**
 * Created by nahongxu on 2016/6/26.
 */
public enum BpRequestType {
    PC(0), SDK(1), WAP(2), OTHERS(3);

    private static BiMap<BpRequestType, PayTerminal> BP_RQ_TYPE_MAPPING_DICT = HashBiMap.create();

    static {
        BP_RQ_TYPE_MAPPING_DICT.put(PC, PayTerminal.PC);
        BP_RQ_TYPE_MAPPING_DICT.put(WAP, PayTerminal.WAP);
        BP_RQ_TYPE_MAPPING_DICT.put(SDK, PayTerminal.APP);
        BP_RQ_TYPE_MAPPING_DICT.put(OTHERS, PayTerminal.OTHERS);
    }

    private int value;

    BpRequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static BpRequestType convToBpRqType(PayTerminal feePayTemin) {
        return BP_RQ_TYPE_MAPPING_DICT.inverse().get(feePayTemin);
    }
}
