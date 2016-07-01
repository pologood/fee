package com.sogou.pay.fee.model.blueplus;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by nahongxu on 2016/6/22.
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class BpPaymentReqData extends BluePlusBaseData {
    public static final String BP_PAYMENT_CODE = "N1007";
    public static final String BP_PAYMENT_METHOD = "payment";


    private String requesttype;
    private String forwardtype;
    private String billid;
    private String acctcode;
    private String billtype = "1";//default value=1;
    private String prodid;
    private String prodname;
    private String prodtype;
    private String prodamount;
    private String ownoperator;
    private String ownprovince;
    private String proddenominationprice;
    private String payappcode;
    private String returnurl;

    public BpPaymentReqData(String requesttype, String forwardtype, String billid,
                            String acctcode, String billtype, String prodid,
                            String prodname, String prodtype, String prodamount,
                            String ownoperator, String ownprovince, String proddenominationprice,
                            String payappcode, String returnurl) {
        super(BP_PAYMENT_CODE, BP_PAYMENT_METHOD);
        this.requesttype = requesttype;
        this.forwardtype = forwardtype;
        this.billid = billid;
        this.acctcode = acctcode;
        this.billtype = billtype;
        this.prodid = prodid;
        this.prodname = prodname;
        this.prodtype = prodtype;
        this.prodamount = prodamount;
        this.ownoperator = ownoperator;
        this.ownprovince = ownprovince;
        this.proddenominationprice = proddenominationprice;
        this.payappcode = payappcode;
        this.returnurl = returnurl;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getForwardtype() {
        return forwardtype;
    }

    public void setForwardtype(String forwardtype) {
        this.forwardtype = forwardtype;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getAcctcode() {
        return acctcode;
    }

    public void setAcctcode(String acctcode) {
        this.acctcode = acctcode;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getProdtype() {
        return prodtype;
    }

    public void setProdtype(String prodtype) {
        this.prodtype = prodtype;
    }

    public String getProdamount() {
        return prodamount;
    }

    public void setProdamount(String prodamount) {
        this.prodamount = prodamount;
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

    public String getProddenominationprice() {
        return proddenominationprice;
    }

    public void setProddenominationprice(String proddenominationprice) {
        this.proddenominationprice = proddenominationprice;
    }

    public String getPayappcode() {
        return payappcode;
    }

    public void setPayappcode(String payappcode) {
        this.payappcode = payappcode;
    }

    public String getReturnurl() {
        return returnurl;
    }

    public void setReturnurl(String returnurl) {
        this.returnurl = returnurl;
    }
}
