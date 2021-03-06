package com.sogou.pay.fee.service.blueplus;

/**
 * Created by nahongxu on 2016/6/22.
 */

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BpAesUtil {
    public final static String DEFAULT_CHARSET = "UTF-8";

    /**
     * AES 加密
     *
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String password)
            throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes(DEFAULT_CHARSET));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] byteContent = content.getBytes(DEFAULT_CHARSET);

        byte[] ivBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] result = cipher.doFinal(byteContent);

        if (result != null && result.length > 0) {
            return Base64.encodeBase64URLSafeString(result);
        }
        return null;
    }

    /**
     * AES 解密
     *
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String password)
            throws Exception {
        // hash password with SHA-256 and crop the output
        // to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes(DEFAULT_CHARSET));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] ivBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] result = cipher.doFinal(Base64.decodeBase64(content));
        if (result != null && result.length > 0) {
            return new String(result, DEFAULT_CHARSET);
        }
        return null;
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
}
