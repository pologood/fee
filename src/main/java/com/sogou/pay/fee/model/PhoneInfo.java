package com.sogou.pay.fee.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sogou.pay.fee.service.ProvinceUtil;
import com.sogou.pay.fee.service.blueplus.BpPhoneInfo;
import com.sogou.pay.fee.service.blueplus.BpService;

/**
 * Created by nahongxu on 2016/6/22.
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class PhoneInfo {

    public static enum Operator {
        CHINA_MOBILE(1, "中国移动"), CHINA_UNICOM(2, "中国联通"),
        CHINA_TELECOM(3, "中国电信"), UNKNOWN(4, "未知");
        private int value;
        private String name;

        Operator(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }


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
        Operator operator = BpService.convFromBpOperatorNew(bpPhoneInfo.getOwnoperator());
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
