package com.sogou.pay.fee.service;

import org.springframework.core.env.Environment;

/**
 * Created by nahongxu on 2016/6/22.
 */
public class BpRestService {

    private String partid;
    private String secret;

    public BpRestService(Environment env) {
        this.partid = env.getProperty("bpPartid");
        this.secret = env.getProperty("bpSecret");
    }

    public String getPartid() {
        return partid;
    }

    public void setPartid(String partid) {
        this.partid = partid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
