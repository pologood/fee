package com.sogou.pay.fee.model.blueplus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sogou.pay.fee.entity.Order;

/**
 * Created by nahongxu on 2016/6/26.
 */
public enum BpPayChannel {
    ZHIFUBAO(1,"ZFB"), YINLIAN(2,"YL"), BAIFUBAO(3,"BFB");
    private int value;
    private String name;

    BpPayChannel(int value,String name) {
        this.value = value;
        this.name=name;
    }

    private static BiMap<BpPayChannel, Order.Channel> BP_PAY_CHANNEL_DICT = HashBiMap.create();

    static {
        BP_PAY_CHANNEL_DICT.put(ZHIFUBAO, Order.Channel.ZHIFUBAO);
        BP_PAY_CHANNEL_DICT.put(YINLIAN, Order.Channel.YINLIAN);
        BP_PAY_CHANNEL_DICT.put(BAIFUBAO, Order.Channel.BAIFUBAO);
    }

    public static BpPayChannel convToBpChannel(Order.Channel payChannel) {
        return BP_PAY_CHANNEL_DICT.inverse().get(payChannel);
    }

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
