package com.sogou.pay.fee.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nahongxu on 2016/6/21.
 */
public class ProvinceUtil {

    private static Map<String,String> PROVINCE_MAP=new HashMap<String,String>();

    static {
        //省编码对照表
        PROVINCE_MAP.put("110000", "北京");
        PROVINCE_MAP.put("120000", "天津");
        PROVINCE_MAP.put("130000", "河北");
        PROVINCE_MAP.put("140000", "山西");
        PROVINCE_MAP.put("150000", "内蒙古");
        PROVINCE_MAP.put("210000", "辽宁");
        PROVINCE_MAP.put("220000", "吉林");
        PROVINCE_MAP.put("230000", "黑龙江");
        PROVINCE_MAP.put("310000", "上海");
        PROVINCE_MAP.put("320000", "江苏");
        PROVINCE_MAP.put("330000", "浙江");
        PROVINCE_MAP.put("340000", "安徽");
        PROVINCE_MAP.put("350000", "福建");
        PROVINCE_MAP.put("360000", "江西");
        PROVINCE_MAP.put("370000", "山东");
        PROVINCE_MAP.put("410000", "河南");
        PROVINCE_MAP.put("420000", "湖北");
        PROVINCE_MAP.put("430000", "湖南");
        PROVINCE_MAP.put("440000", "广东");
        PROVINCE_MAP.put("450000", "广西");
        PROVINCE_MAP.put("460000", "海南");
        PROVINCE_MAP.put("500000", "重庆");
        PROVINCE_MAP.put("510000", "四川");
        PROVINCE_MAP.put("520000", "贵州");
        PROVINCE_MAP.put("530000", "云南");
        PROVINCE_MAP.put("540000", "西藏");
        PROVINCE_MAP.put("610000", "陕西");
        PROVINCE_MAP.put("620000", "甘肃");
        PROVINCE_MAP.put("630000", "青海");
        PROVINCE_MAP.put("640000", "宁夏");
        PROVINCE_MAP.put("650000", "新疆");
        PROVINCE_MAP.put("710000", "台湾");
        PROVINCE_MAP.put("810000", "香港");
        PROVINCE_MAP.put("820000", "澳门");
        PROVINCE_MAP.put("990000", "其他国家");
    }

    public static String getProvinceByCode(String code){
        return PROVINCE_MAP.get(code);
    }

    public static  String getProvinceByName(String name){
        String provincecode=null;
        for(Map.Entry<String,String> entry:PROVINCE_MAP.entrySet()){
            if(name.equals(entry.getValue())){
                provincecode=entry.getKey();
                break;
            }
        }
        return provincecode;
    }
}
