package commons.saas;

import commons.utils.DigestHelper;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class JiYiFuPayService extends PayService {
  static final Charset charset = Charset.forName("UTF-8");
  static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
  static HttpComponentsClientHttpRequestFactory factory =
    new HttpComponentsClientHttpRequestFactory();

  static {
    factory.setConnectTimeout(1000);
    factory.setReadTimeout(10000);
  }

  String channelToString(Channel channel) {
    switch(channel) {
    case WEIXIN: return "WECHAT";
    case ZHIFUBAO: return "ALIPAY";
    }
    return null;
  }
  
  Map<String, String> buildPrePayParam(String platform) {
    Map<String, String> params = new TreeMap<>();  // key order is important

    params.put("appId",          config.appid);
    params.put("version",        config.version);
    params.put("pageUrl",        config.callbackUrl);
    params.put("bgUrl",          config.notifyUrl);
    params.put("orderId",        orderId);
    params.put("orderAmount",    money);
    params.put("orderTime",      fmt.format(LocalDateTime.now()));
    params.put("productName",    productName);
    params.put("productNum",     "1");
    params.put("bankId",         channelToString(channel));
    params.put("accessPlatform", platform);
    params.put("signType",       config.signType == SignType.MD5 ? "0" : "1");

    String separator = "";
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      builder.append(separator).append(entry.getKey()).append("=").append(entry.getValue());
      separator = "&";
    }
    builder.append(config.appkey);

    // must be the last
    if (config.signType == SignType.MD5) {
      params.put("sign", DigestHelper.md5(builder.toString().getBytes(charset)));
    } else if (config.signType == SignType.SHA) {
      params.put("sign", DigestHelper.sha1(builder.toString().getBytes(charset)));
    }
    return params;
  }
  
  public static class AppRetData {
    public Object orderInfo;
    public String aliPublicKey;
  }
  
  public static class AppRet {
    public String     status;
    public String     message;
    public AppRetData data;
  }
  
  public PrePayCtx appPrePay() {
    Map<String, String> params = buildPrePayParam("3");
    
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.setAll(params);

    RestTemplate rest = new RestTemplate(factory);
    try {
      AppRet ret = rest.postForObject(config.apppayUrl, map, AppRet.class);
      if (!ret.status.equals("SUCCESS")) {
        throw new PayException(ret.message);
      }
      
      PrePayCtx ctx = new PrePayCtx();
      ctx.prePayId  = ret.data.orderInfo;
      ctx.publicKey = ret.data.aliPublicKey;
      return ctx;
    } catch (Exception e) {
      throw new PayException(e);
    }
  }

  // order is important
  static final String postPayResultFields[] = {
    "appId", "isSuccess", "orderId", "orderMoney",
    "payId", "signType", "successTime", "tradeStatus"
  };

  public PostPayCtx postPay(Map<String, String> result) {
    String separator = "";
    StringBuilder builder = new StringBuilder();
    for (String field : postPayResultFields) {
      String value = result.get(field);
      if (value == null) return null;
      builder.append(separator).append(field).append('=').append(value);
      separator = "&";
    }
    
    builder.append(config.appkey);
    String sign = null;
    if ("0".equals(result.get("signType"))) {
      sign = DigestHelper.md5(builder.toString().getBytes(charset));
    } else if ("1".equals(result.get("signType"))) {
      sign = DigestHelper.sha1(builder.toString().getBytes(charset));
    }

    if (sign == null || !sign.equals(result.get("sign"))) {
      return null;
    }

    if (!"SUCCESS".equals(result.get("tradeStatus")) ||
        !config.appid.equals(result.get("appId"))) { 
      return null;
    }

    PostPayCtx ctx = new PostPayCtx();
    ctx.payId   = result.get("payId");
    ctx.orderId = result.get("orderId");
    ctx.money   = result.get("orderMoney");
    return ctx;
  }
}
