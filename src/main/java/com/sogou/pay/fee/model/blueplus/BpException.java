package com.sogou.pay.fee.model.blueplus;

/**
 * Created by nahongxu on 2016/6/22.
 */
public class BpException extends RuntimeException {
    public BpException(String msg){
        super(msg);
    }

    public BpException(Throwable throwable){
        super(throwable);
    }
}
