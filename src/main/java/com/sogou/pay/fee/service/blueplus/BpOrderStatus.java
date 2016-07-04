package com.sogou.pay.fee.service.blueplus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sogou.pay.fee.entity.Order;

/**
 * Created by nahongxu on 2016/6/26.
 */
public enum BpOrderStatus {
    TOPAY(1), PAY_SUCCESS(2),PAY_FAILED(3), FEEING(4),FEE_SUCCESS(5),FEE_FAILED(6),
    REFUNDING(7),REFUND_SUCCESS(8), EXPIERED(9);

    private static BiMap<Integer,Order.Status> BP_ORDER_STATUS_MAPPING_DICT= HashBiMap.create();
    static {
        BP_ORDER_STATUS_MAPPING_DICT.put(TOPAY.getValue(), Order.Status.TOPAY);
        BP_ORDER_STATUS_MAPPING_DICT.put(PAY_SUCCESS.getValue(), Order.Status.PAY_SUCCESS);
        BP_ORDER_STATUS_MAPPING_DICT.put(PAY_FAILED.getValue(), Order.Status.PAY_FAILED);
        BP_ORDER_STATUS_MAPPING_DICT.put(FEEING.getValue(), Order.Status.FEEING);
        BP_ORDER_STATUS_MAPPING_DICT.put(FEE_SUCCESS.getValue(), Order.Status.FEE_SUCCESS);
        BP_ORDER_STATUS_MAPPING_DICT.put(FEE_FAILED.getValue(), Order.Status.FEE_FAILED);
        BP_ORDER_STATUS_MAPPING_DICT.put(REFUNDING.getValue(), Order.Status.REFUNDING);
        BP_ORDER_STATUS_MAPPING_DICT.put(REFUND_SUCCESS.getValue(), Order.Status.REFUND_SUCCESS);
        BP_ORDER_STATUS_MAPPING_DICT.put(EXPIERED.getValue(), Order.Status.EXPIERED);
    }
    private int value;

    BpOrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static Order.Status convFromBpStatus(int value){
        return BP_ORDER_STATUS_MAPPING_DICT.get(value);

    }
}



