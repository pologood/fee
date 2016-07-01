package com.sogou.pay.fee.model.blueplus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sogou.pay.fee.model.PayReturnType;

/**
 * Created by nahongxu on 2016/6/26.
 */
public enum BpReturnType {
    STR(0),URL(1),HTML(2);

    private static BiMap<Integer,PayReturnType> BP_PAY_RET_TYPE_DICT= HashBiMap.create();
    static {
        BP_PAY_RET_TYPE_DICT.put(STR.getValue(),PayReturnType.STR);
        BP_PAY_RET_TYPE_DICT.put(URL.getValue(),PayReturnType.URL);
        BP_PAY_RET_TYPE_DICT.put(HTML.getValue(),PayReturnType.HTML);
    }

    private int value;
    BpReturnType(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static PayReturnType convFromBpRetType(int bpRetType){
        return BP_PAY_RET_TYPE_DICT.get(bpRetType);
    }
}
