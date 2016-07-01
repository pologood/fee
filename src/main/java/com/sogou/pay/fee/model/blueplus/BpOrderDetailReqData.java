package com.sogou.pay.fee.model.blueplus;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by nahongxu on 2016/6/22.
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class BpOrderDetailReqData extends BluePlusBaseData {
    public static final String BP_ORDER_DETAIL_CODE = "N1006";
    public static final String BP_ORDER_DETAIL_METHOD = "getOrderDetail";

    private String orderid;
    private String month;

    public BpOrderDetailReqData(String orderid, String month) {
        super(BP_ORDER_DETAIL_CODE, BP_ORDER_DETAIL_METHOD);
        this.orderid = orderid;
        this.month = month;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
