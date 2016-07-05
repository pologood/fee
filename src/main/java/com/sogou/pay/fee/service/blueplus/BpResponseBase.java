package com.sogou.pay.fee.service.blueplus;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by nahongxu on 2016/6/24.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BpResponseBase<Data> {
    public static final String SUCCESS="1";
    public static final String FAILED="0";

    private String result;
    private String errcode;
    private String errdesc;
    private Data detailinfo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrdesc() {
        return errdesc;
    }

    public void setErrdesc(String errdesc) {
        this.errdesc = errdesc;
    }

    public Data getDetailinfo() {
        return detailinfo;
    }

    public void setDetailinfo(Data detailinfo) {
        this.detailinfo = detailinfo;
    }
}
