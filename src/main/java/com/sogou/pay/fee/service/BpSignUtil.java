package com.sogou.pay.fee.service;

import commons.utils.DigestHelper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by nahongxu on 2016/6/22.
 */
public class BpSignUtil {


    public static String strToMd5(String str, String rdered) {
        String md5Str = null;
        if (str != null && str.length() != 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes(rdered));
                byte b[] = md.digest();
                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    byte i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                md5Str = buf.toString();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("MD5加密发生异常。加密串：" + str);
            } catch (UnsupportedEncodingException e2) {
                System.out.println("MD5加密发生异常。加密串：" + str);
            }
        }
        return md5Str;
    }

    /**
     * 按照字典序逆序拼接参数
     *
     * @param params
     * @return
     */
    public static String getSign(String... params) {
        List<String> srcList = new ArrayList<String>();
        for (String param : params) {
            srcList.add(param);
        }
        // 按照字典序逆序拼接参数
        Arrays.sort(params);
        Collections.sort(srcList, String.CASE_INSENSITIVE_ORDER);
        Collections.reverse(srcList);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < srcList.size(); i++) {
            sb.append(srcList.get(i));
        }
        return sb.toString();
    }

    /***
     * MD5加密调用
     */
    public static String getSignAndMD5(String... params) throws Exception {
        String sign = getSign(params);
        return DigestHelper.md5(sign.getBytes("utf-8"));
    }
}