package com.sogou.pay.fee.model.blueplus;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by nahongxu on 2016/6/22.
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class BpProductsReqData extends BluePlusBaseData {
    public static final String BP_PHONE_INFO_CODE = "N1002";
    public static final String BP_PHONE_INFO_METHOD = "getProdList";


    private String prodtype;
    private String ownoperator;
    private String ownprovince;

    public BpProductsReqData(String prodtype, String ownoperator, String ownprovince) {
        super(BP_PHONE_INFO_CODE, BP_PHONE_INFO_METHOD);
        this.prodtype = prodtype;
        this.ownoperator = ownoperator;
        this.ownprovince = ownprovince;
    }

    public String getProdtype() {
        return prodtype;
    }

    public void setProdtype(String prodtype) {
        this.prodtype = prodtype;
    }

    public String getOwnoperator() {
        return ownoperator;
    }

    public void setOwnoperator(String ownoperator) {
        this.ownoperator = ownoperator;
    }

    public String getOwnprovince() {
        return ownprovince;
    }

    public void setOwnprovince(String ownprovince) {
        this.ownprovince = ownprovince;
    }
}
