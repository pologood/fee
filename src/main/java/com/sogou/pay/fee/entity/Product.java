package com.sogou.pay.fee.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sogou.pay.fee.service.blueplus.BpProductInfo;
import com.sogou.pay.fee.service.blueplus.BpService;

import java.time.LocalDateTime;

/**
 * Created by nahongxu on 2016/6/21.
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Product {

    public static enum Status {
        ONSALE(1), INVALID(2);
        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static enum FeeType {
        PHONE(1), FLOW(2), ALL(3), UNKNOWN(4);
        private int value;

        FeeType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


    private long productId;
    private String productName;
    private String outerId;
    private int providerId;
    private FeeType feeType;
    private long standardPrice;
    private long realPrice;
    private long denominationprice;
    private Status status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Product() {

    }

    public Product(BpProductInfo bpProductInfo) {
        this.productName = bpProductInfo.getProdname();
        this.outerId = bpProductInfo.getProdid();
        this.providerId = BpService.BLUEPLUS_PROVIDER_ID;
        this.feeType = BpService.convFromBpFeeTypeNew(bpProductInfo.getProdtype());
        this.standardPrice = bpProductInfo.getStandardamount();
        this.realPrice = bpProductInfo.getProdamount();
        this.denominationprice = bpProductInfo.getProddenominationprice();
        this.status = BpProductInfo.convFromBpStatus(bpProductInfo.getProdstate());
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public long getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(long standardPrice) {
        this.standardPrice = standardPrice;
    }

    public long getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(long realPrice) {
        this.realPrice = realPrice;
    }

    public long getDenominationprice() {
        return denominationprice;
    }

    public void setDenominationprice(long denominationprice) {
        this.denominationprice = denominationprice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
