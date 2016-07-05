package com.sogou.pay.fee.service.blueplus;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by nahongxu on 2016/6/24.
 */
public class BpProvince {
    private static BiMap<String, String> BP_PROVINCE_MAP = HashBiMap.create();

    static {
        BP_PROVINCE_MAP.put("1000000000000000000000000000000000", "北京");
        BP_PROVINCE_MAP.put("0100000000000000000000000000000000", "广东");
        BP_PROVINCE_MAP.put("0010000000000000000000000000000000", "上海");
        BP_PROVINCE_MAP.put("0001000000000000000000000000000000", "天津");
        BP_PROVINCE_MAP.put("0000100000000000000000000000000000", "重庆");
        BP_PROVINCE_MAP.put("0000010000000000000000000000000000", "辽宁");
        BP_PROVINCE_MAP.put("0000001000000000000000000000000000", "江苏");
        BP_PROVINCE_MAP.put("0000000100000000000000000000000000", "湖北");
        BP_PROVINCE_MAP.put("0000000010000000000000000000000000", "四川");
        BP_PROVINCE_MAP.put("0000000001000000000000000000000000", "陕西");
        BP_PROVINCE_MAP.put("0000000000100000000000000000000000", "河北");
        BP_PROVINCE_MAP.put("0000000000010000000000000000000000", "山西");
        BP_PROVINCE_MAP.put("0000000000001000000000000000000000", "河南");
        BP_PROVINCE_MAP.put("0000000000000100000000000000000000", "吉林");
        BP_PROVINCE_MAP.put("0000000000000010000000000000000000", "黑龙江");
        BP_PROVINCE_MAP.put("0000000000000001000000000000000000", "内蒙古");
        BP_PROVINCE_MAP.put("0000000000000000100000000000000000", "山东");
        BP_PROVINCE_MAP.put("0000000000000000010000000000000000", "安徽");
        BP_PROVINCE_MAP.put("0000000000000000001000000000000000", "浙江");
        BP_PROVINCE_MAP.put("0000000000000000000100000000000000", "福建");
        BP_PROVINCE_MAP.put("0000000000000000000010000000000000", "湖南");
        BP_PROVINCE_MAP.put("0000000000000000000001000000000000", "广西");
        BP_PROVINCE_MAP.put("0000000000000000000000100000000000", "江西");
        BP_PROVINCE_MAP.put("0000000000000000000000010000000000", "贵州");
        BP_PROVINCE_MAP.put("0000000000000000000000001000000000", "云南");
        BP_PROVINCE_MAP.put("0000000000000000000000000100000000", "西藏");
        BP_PROVINCE_MAP.put("0000000000000000000000000010000000", "海南");
        BP_PROVINCE_MAP.put("0000000000000000000000000001000000", "甘肃");
        BP_PROVINCE_MAP.put("0000000000000000000000000000100000", "宁夏");
        BP_PROVINCE_MAP.put("0000000000000000000000000000010000", "青海");

    }

    public static String getProCodeByBpName(String provinceName) {
        return BP_PROVINCE_MAP.inverse().get(provinceName);
    }
}
