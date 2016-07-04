package com.sogou.pay.fee.service.blueplus;

import java.util.TreeMap;

/**
 * Created by nahongxu on 2016/7/4.
 */
public class BpReqDataBody extends TreeMap<String,Object> {
    public BpReqDataBody(String interfacecode,String intefacemethod){
        put("interfacecode",interfacecode);
        put("intefacemethod",intefacemethod);
    }
}
