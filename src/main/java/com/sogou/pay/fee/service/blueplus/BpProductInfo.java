package com.sogou.pay.fee.service.blueplus;

import com.sogou.pay.fee.entity.Product;

/**
 * Created by nahongxu on 2016/6/24.
 */
public class BpProductInfo {

    public static final String BP_PRO_STATUS_INVALID = "0";
    public static final String BP_PRO_STATUS_ONSALE = "1";

    private String prodstate;
    private long standardamount;
    private long prodamount;
    private String isdefault;
    private String prodtype;
    private String prodtypename;
    private String prodid;
    private String prodname;
    private long proddenominationprice;
    private String ownoperator;
    private String ownoperatorname;
    private String ownprovince;
    private String ownprovincename;

    public static Product.Status convFromBpStatus(String bpStatus) {
        if (bpStatus.equals(BP_PRO_STATUS_ONSALE)) {
            return Product.Status.ONSALE;
        } else {
            return Product.Status.INVALID;
        }
    }

    public String getProdstate() {
        return prodstate;
    }

    public void setProdstate(String prodstate) {
        this.prodstate = prodstate;
    }

    public long getStandardamount() {
        return standardamount;
    }

    public void setStandardamount(long standardamount) {
        this.standardamount = standardamount;
    }

    public long getProdamount() {
        return prodamount;
    }

    public void setProdamount(long prodamount) {
        this.prodamount = prodamount;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getProdtype() {
        return prodtype;
    }

    public void setProdtype(String prodtype) {
        this.prodtype = prodtype;
    }

    public String getProdtypename() {
        return prodtypename;
    }

    public void setProdtypename(String prodtypename) {
        this.prodtypename = prodtypename;
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

    public long getProddenominationprice() {
        return proddenominationprice;
    }

    public void setProddenominationprice(long proddenominationprice) {
        this.proddenominationprice = proddenominationprice;
    }

    public String getOwnoperator() {
        return ownoperator;
    }

    public void setOwnoperator(String ownoperator) {
        this.ownoperator = ownoperator;
    }

    public String getOwnoperatorname() {
        return ownoperatorname;
    }

    public void setOwnoperatorname(String ownoperatorname) {
        this.ownoperatorname = ownoperatorname;
    }

    public String getOwnprovince() {
        return ownprovince;
    }

    public void setOwnprovince(String ownprovince) {
        this.ownprovince = ownprovince;
    }

    public String getOwnprovincename() {
        return ownprovincename;
    }

    public void setOwnprovincename(String ownprovincename) {
        this.ownprovincename = ownprovincename;
    }
}
