package com.sogou.pay.fee.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Order {

    public static final List<Status> STATUS_DONE = Arrays.asList(Status.PAY_FAILED, Status.FEE_SUCCESS,
            Status.FEE_FAILED, Status.REFUND_SUCCESS, Status.FINISHED,
            Status.CANCEL, Status.EXPIERED);

    public static enum Status {
        CONFIRMED(1), TOPAY(2), PAY_SUCCESS(3), PAY_FAILED(4),
        FEEING(5), FEE_SUCCESS(6), FEE_FAILED(7), REFUNDING(8),
        REFUND_SUCCESS(9), FINISHED(10), CANCEL(11), EXPIERED(12), UNKNOWN(13);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public static enum QueryOrderType {
        BY_PHONE(1), BY_USERID(2);
        private int value;

        QueryOrderType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public static enum Channel {
        WEIXIN(1), ZHIFUBAO(2), YINLIAN(3), BAIFUBAO(4);

        private int value;

        Channel(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum PayTerminal {
        WAP(1), APP(2), PC(3), OTHERS(4);
        private int value;

        PayTerminal(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    private long orderId;
    private Product.FeeType feeType;
    private String userId;
    private String phone;
    private String specifiedNo;
    private String outerId;
    private long productId;
    private long totalAmount;
    private long curPrice;
    private int quantity;
    private Channel payChanel;
    private PayTerminal payTerminal;
    private Status status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Product.FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(Product.FeeType feeType) {
        this.feeType = feeType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecifiedNo() {
        return specifiedNo;
    }

    public void setSpecifiedNo(String specifiedNo) {
        this.specifiedNo = specifiedNo;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(long curPrice) {
        this.curPrice = curPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Channel getPayChanel() {
        return payChanel;
    }

    public void setPayChanel(Channel payChanel) {
        this.payChanel = payChanel;
    }

    public PayTerminal getPayTerminal() {
        return payTerminal;
    }

    public void setPayTerminal(PayTerminal payTerminal) {
        this.payTerminal = payTerminal;
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
