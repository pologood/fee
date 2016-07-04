package com.sogou.pay.fee.service.blueplus;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by nahongxu on 2016/6/22.
 */

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class BluePlusBaseData {
    private String interfacecode;
    private String intefacemethod;

    public BluePlusBaseData(){

    }

    public BluePlusBaseData(String interfacecode, String intefacemethod) {
        this.interfacecode = interfacecode;
        this.intefacemethod = intefacemethod;
    }

    public String getInterfacecode() {
        return interfacecode;
    }

    public void setInterfacecode(String interfacecode) {
        this.interfacecode = interfacecode;
    }

    public String getIntefacemethod() {
        return intefacemethod;
    }

    public void setIntefacemethod(String intefacemethod) {
        this.intefacemethod = intefacemethod;
    }
}
