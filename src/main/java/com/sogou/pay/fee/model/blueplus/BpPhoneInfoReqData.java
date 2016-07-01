package com.sogou.pay.fee.model.blueplus;

import java.util.List;

/**
 * Created by nahongxu on 2016/6/22.
 */
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class BpPhoneInfoReqData extends BluePlusBaseData {
    public static final String BP_PHONE_INFO_CODE="N1001";
    public static final String BP_PHONE_INFO_METHOD="getPhoneInfo";

    private List<String> phones;

    public BpPhoneInfoReqData(List<String> phones){
        super(BP_PHONE_INFO_CODE,BP_PHONE_INFO_METHOD);
        this.phones=phones;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
