package com.sogou.pay.fee.service.blueplus;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by nahongxu on 2016/6/24.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BpPhoneInfo {
    private String ownprovince;
    private String ownoperatorname;
    private String ownoperator;
    private String ownprovincename;
    private String warncode;
    private String phoneno;
    private String warndesc;
    private String operationtype;

    public String getOwnprovince() {
        return ownprovince;
    }

    public void setOwnprovince(String ownprovince) {
        this.ownprovince = ownprovince;
    }

    public String getOwnoperatorname() {
        return ownoperatorname;
    }

    public void setOwnoperatorname(String ownoperatorname) {
        this.ownoperatorname = ownoperatorname;
    }

    public String getOwnoperator() {
        return ownoperator;
    }

    public void setOwnoperator(String ownoperator) {
        this.ownoperator = ownoperator;
    }

    public String getOwnprovincename() {
        return ownprovincename;
    }

    public void setOwnprovincename(String ownprovincename) {
        this.ownprovincename = ownprovincename;
    }

    public String getWarncode() {
        return warncode;
    }

    public void setWarncode(String warncode) {
        this.warncode = warncode;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getWarndesc() {
        return warndesc;
    }

    public void setWarndesc(String warndesc) {
        this.warndesc = warndesc;
    }

    public String getOperationtype() {
        return operationtype;
    }

    public void setOperationtype(String operationtype) {
        this.operationtype = operationtype;
    }
}
