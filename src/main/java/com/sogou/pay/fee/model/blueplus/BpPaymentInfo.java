package com.sogou.pay.fee.model.blueplus;

/**
 * Created by nahongxu on 2016/6/26.
 */
public class BpPaymentInfo {

    private String payordid;
    private String orderid;
    private String returntype;
    private String returninfo;

    public String getPayordid() {
        return payordid;
    }

    public void setPayordid(String payordid) {
        this.payordid = payordid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getReturntype() {
        return returntype;
    }

    public void setReturntype(String returntype) {
        this.returntype = returntype;
    }

    public String getReturninfo() {
        return returninfo;
    }

    public void setReturninfo(String returninfo) {
        this.returninfo = returninfo;
    }
}
