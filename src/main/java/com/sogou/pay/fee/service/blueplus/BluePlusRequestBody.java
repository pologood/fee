package com.sogou.pay.fee.service.blueplus;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by nahongxu on 2016/6/22.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BluePlusRequestBody {
    private String partid;
    private String data;
    private String sign;
    private String time;


    public BluePlusRequestBody(String partid, String data,String time,String sign) {
        this.partid = partid;
        this.data = data;
        this.sign = sign;
        this.time = time;
    }

    public String getPartid() {
        return partid;
    }

    public void setPartid(String partid) {
        this.partid = partid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
