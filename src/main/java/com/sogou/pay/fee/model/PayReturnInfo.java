package com.sogou.pay.fee.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sogou.pay.fee.entity.Order;

/**
 * Created by nahongxu on 2016/6/26.
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class PayReturnInfo {
    private Order order;
    private String paymentInfo;
    private PayReturnType payReturnType;

    public PayReturnInfo(Order order, String paymentInfo, PayReturnType payReturnType) {
        this.order = order;
        this.paymentInfo = paymentInfo;
        this.payReturnType = payReturnType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public PayReturnType getPayReturnType() {
        return payReturnType;
    }

    public void setPayReturnType(PayReturnType payReturnType) {
        this.payReturnType = payReturnType;
    }
}
