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

    public BpPaymentReqData(){
        super(BP_PAYMENT_CODE, BP_PAYMENT_METHOD);
    }


    public String getRequesttype() {
        return requesttype;
    }

    public BpPaymentReqData setRequesttype(String requesttype) {
        this.requesttype = requesttype;
        return this;
    }

    public String getForwardtype() {
        return forwardtype;
    }

    public BpPaymentReqData setForwardtype(String forwardtype) {
        this.forwardtype = forwardtype;
        return this;
    }

    public String getBillid() {
        return billid;
    }

    public BpPaymentReqData setBillid(String billid) {
        this.billid = billid;
        return this;
    }

    public String getAcctcode() {
        return acctcode;
    }

    public BpPaymentReqData setAcctcode(String acctcode) {
        this.acctcode = acctcode;
        return this;
    }

    public String getBilltype() {
        return billtype;
    }

    public BpPaymentReqData setBilltype(String billtype) {
        this.billtype = billtype;
        return this;
    }

    public String getProdid() {
        return prodid;
    }

    public BpPaymentReqData setProdid(String prodid) {
        this.prodid = prodid;
        return this;
    }

    public String getProdname() {
        return prodname;
    }

    public BpPaymentReqData setProdname(String prodname) {
        this.prodname = prodname;
        return this;
    }

    public String getProdtype() {
        return prodtype;
    }

    public BpPaymentReqData setProdtype(String prodtype) {
        this.prodtype = prodtype;
        return this;
    }

    public String getProdamount() {
        return prodamount;
    }

    public BpPaymentReqData setProdamount(String prodamount) {
        this.prodamount = prodamount;
        return this;
    }

    public String getOwnoperator() {
        return ownoperator;
    }

    public BpPaymentReqData setOwnoperator(String ownoperator) {
        this.ownoperator = ownoperator;
        return this;
    }

    public String getOwnprovince() {
        return ownprovince;
    }

    public BpPaymentReqData setOwnprovince(String ownprovince) {
        this.ownprovince = ownprovince;
        return this;
    }

    public String getProddenominationprice() {
        return proddenominationprice;
    }

    public BpPaymentReqData setProddenominationprice(String proddenominationprice) {
        this.proddenominationprice = proddenominationprice;
        return this;
    }

    public String getPayappcode() {
        return payappcode;
    }

    public BpPaymentReqData setPayappcode(String payappcode) {
        this.payappcode = payappcode;
        return this;
    }

    public String getReturnurl() {
        return returnurl;
    }

    public BpPaymentReqData setReturnurl(String returnurl) {
        this.returnurl = returnurl;
        return this;
    }
}
