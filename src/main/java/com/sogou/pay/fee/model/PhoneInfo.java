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

    public static enum WarnCode{
        NORMAL(1),BLACKLIST(2),PROVINCE_OPERRATING(3);
        private int value;
        WarnCode(int value){
            this.value=value;
        }

        public int getValue() {
            return value;
        }
    }

    public static enum OpType{
        PHONE_OP(1),FLOW_OP(2),ALL(3),NONE(4);
        private int value;
        OpType(int value){
            this.value=value;
        }

        public int getValue() {
            return value;
        }
    }


    private String phone;
    private Operator operator;
    private String operatorName;
    private String provinceId;
    private String provinceName;
    private WarnCode warnCode;
    private String warnMsg;
    private OpType opType;
    private String opMsg;

    public PhoneInfo(BpPhoneInfo bpPhoneInfo) {
        this.phone = bpPhoneInfo.getPhoneno();
        this.warnCode=BpService.convFromBpWarnCode(bpPhoneInfo.getWarncode());
        switch (this.warnCode){
            case BLACKLIST:
                this.warnMsg="blacklist";
                break;
            case PROVINCE_OPERRATING:
                this.warnMsg="operating";
                this.opType=BpService.convFromBpOpType(bpPhoneInfo.getOperationtype());
            case NORMAL:
                Operator operator = BpService.convFromBpOperatorNew(bpPhoneInfo.getOwnoperator());
                this.operator = operator;
                this.operatorName = operator.getName();
                this.provinceId = ProvinceUtil.getProvinceByName(bpPhoneInfo.getOwnprovincename());
                this.provinceName = bpPhoneInfo.getOwnprovincename();
                break;
            default:
                break;
        }

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

    public WarnCode getWarnCode() {
        return warnCode;
    }

    public void setWarnCode(WarnCode warnCode) {
        this.warnCode = warnCode;
    }

    public String getWarnMsg() {
        return warnMsg;
    }

    public void setWarnMsg(String warnMsg) {
        this.warnMsg = warnMsg;
    }

    public OpType getOpType() {
        return opType;
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }

    public String getOpMsg() {
        return opMsg;
    }

    public void setOpMsg(String opMsg) {
        this.opMsg = opMsg;
    }
}
