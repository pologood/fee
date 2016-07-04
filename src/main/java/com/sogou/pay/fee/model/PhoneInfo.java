package com.sogou.pay.fee.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sogou.pay.fee.entity.ProvinceUtil;
import com.sogou.pay.fee.service.blueplus.BpOperator;
import com.sogou.pay.fee.service.blueplus.BpPhoneInfo;

/**
 * Created by nahongxu on 2016/6/22.
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class PhoneInfo {
    private String phone;
    private Operator operator;
    private String operatorName;
    private String provinceId;
    private String provinceName;

    public PhoneInfo(String phone, Operator operator, String provinceId) {
        this.phone = phone;
        this.operator = operator;
        this.operatorName = operator.getName();
        this.provinceId = provinceId;
        this.provinceName = ProvinceUtil.getProvinceByCode(provinceId);
    }

    public PhoneInfo(BpPhoneInfo bpPhoneInfo) {
        this.phone = bpPhoneInfo.getPhoneno();
        Operator operator = BpOperator.convFromBpOperator(BpOperator.getBpOperatorByCode(bpPhoneInfo.getOwnoperator()));
        this.operator = operator;
        this.operatorName = operator.getName();
        this.provinceId = ProvinceUtil.getProvinceByName(bpPhoneInfo.getOwnprovincename());
        this.provinceName = bpPhoneInfo.getOwnprovincename();

    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
