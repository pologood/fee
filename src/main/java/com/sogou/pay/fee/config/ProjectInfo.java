package com.sogou.pay.fee.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProjectInfo {
  public static final String PKG_PREFIX    = "com.sogou.pay.fee";
  public static final String API_PKG       = "com.sogou.pay.fee.api";
  public static final String MAPPER_PKG    = "com.sogou.pay.fee.mapper";
  public static final List<String> DOC_PKG = Collections.unmodifiableList(
    Arrays.asList("com.sogou.pay.fee.api", "com.sogou.pay.fee.entity", "com.sogou.pay.fee.model"));
}
